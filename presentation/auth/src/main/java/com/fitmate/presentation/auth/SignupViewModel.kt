package com.fitmate.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitmate.domain.model.BodyArea
import com.fitmate.domain.model.CurrentExerciseStatus
import com.fitmate.domain.model.Equipment
import com.fitmate.domain.model.Exercise
import com.fitmate.domain.model.ExerciseLevel
import com.fitmate.domain.model.ExerciseLocation
import com.fitmate.domain.model.Gender
import com.fitmate.domain.model.GoalStrategy
import com.fitmate.domain.model.PrimaryGoal
import com.fitmate.domain.model.RecentExerciseRecord
import com.fitmate.domain.model.SignUpForm
import com.fitmate.domain.repository.AuthRepository
import com.fitmate.domain.repository.CatalogRepository
import com.fitmate.presentation.designsystem.component.SearchableExercise
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class IdCheckState { Unchecked, Checking, Available, Taken }

// 최근 운동 기록 입력값 (입력칸 문자열 그대로)
data class RecordInput(
    val weight: String = "",
    val reps: String = "",
    val sets: String = "",
    val minutes: String = ""
)

data class SignupUiState(
    // 1.1 계정
    val loginId: String = "",
    val password: String = "",
    val passwordConfirm: String = "",
    val idCheck: IdCheckState = IdCheckState.Unchecked,
    // 1.2 기본 정보
    val gender: Gender? = null,
    val age: String = "",
    val height: String = "",
    val weight: String = "",
    // 1.3 신체 측정 (선택)
    val skeletalMuscleMass: String = "",
    val bodyFatPercentage: String = "",
    val bodyFatMass: String = "",
    // 1.4 ~ 1.7
    val exerciseLevel: ExerciseLevel? = null,
    val currentStatus: CurrentExerciseStatus? = null,
    val primaryGoal: PrimaryGoal? = null,
    val goalStrategy: GoalStrategy? = null,
    val availableDays: Set<String> = emptySet(),
    val durationLabel: String? = null,
    // 1.8 ~ 1.9 장소 / 기구
    val location: ExerciseLocation? = null,
    val gymName: String = "",
    val equipments: Map<ExerciseLocation, List<Equipment>> = emptyMap(),
    val isEquipmentLoading: Boolean = false,
    val selectedEquipmentCodes: Set<String> = emptySet(),
    // 1.10 고려해야할 부분
    val hasConcern: Boolean = false,
    val avoidBodyAreas: Set<BodyArea> = emptySet(),
    val avoidExercises: List<String> = emptyList(),
    // 1.11 최근 운동 기록 (운동 이름 -> 입력값)
    val recordExercises: List<String> = emptyList(),
    val records: Map<String, RecordInput> = emptyMap(),
    // 운동 검색용 전체 운동 목록
    val exercises: List<Exercise> = emptyList(),
    val isSubmitting: Boolean = false
) {
    val searchableExercises: List<SearchableExercise>
        get() = exercises.map {
            val equipment = it.equipmentCategories.firstOrNull()?.label() ?: "맨몸"
            SearchableExercise(
                name = it.name,
                description = "${it.primaryMuscle.bodyPartLabel()} · $equipment",
                filter = it.primaryMuscle.filterLabel()
            )
        }

    val recordItems: List<SignupRecordItem>
        get() = recordExercises.map { name ->
            SignupRecordItem(
                name = name,
                recordType = exerciseByName(name)?.recordType?.toRecordType() ?: RecordType.Weight,
                input = records[name] ?: RecordInput()
            )
        }

    fun exerciseByName(name: String): Exercise? = exercises.firstOrNull { it.name == name }
}

sealed interface SignupEvent {
    data class Message(val text: String) : SignupEvent

    // 회원가입 + 자동 로그인 완료
    data object Completed : SignupEvent

    // 가입은 됐지만 자동 로그인에 실패해 로그인 화면으로 보낸다
    data object NeedLogin : SignupEvent
}

private val PasswordRegex = Regex("^(?=.*[A-Za-z])(?=.*\\d).{10,}$")

// 회원가입 위저드 전체 단계가 공유하는 ViewModel (signup 내비게이션 그래프 범위)
@HiltViewModel
class SignupViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val catalogRepository: CatalogRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SignupUiState())
    val uiState: StateFlow<SignupUiState> = _uiState.asStateFlow()

    private val _events = Channel<SignupEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    init {
        loadExercises()
    }

    // 1.1 계정
    fun onLoginIdChange(value: String) =
        _uiState.update { it.copy(loginId = value.trim(), idCheck = IdCheckState.Unchecked) }

    fun onPasswordChange(value: String) = _uiState.update { it.copy(password = value) }
    fun onPasswordConfirmChange(value: String) = _uiState.update { it.copy(passwordConfirm = value) }

    fun checkLoginId() {
        val loginId = _uiState.value.loginId
        if (loginId.length !in 8..16) {
            message("아이디는 8~16자로 입력해주세요.")
            return
        }
        _uiState.update { it.copy(idCheck = IdCheckState.Checking) }
        viewModelScope.launch {
            authRepository.isLoginIdAvailable(loginId)
                .onSuccess { available ->
                    // 확인 중에 아이디를 바꿨다면 결과를 버린다
                    _uiState.update {
                        if (it.loginId != loginId) it
                        else it.copy(idCheck = if (available) IdCheckState.Available else IdCheckState.Taken)
                    }
                }
                .onFailure { error ->
                    _uiState.update { it.copy(idCheck = IdCheckState.Unchecked) }
                    message(error.message)
                }
        }
    }

    // 1.2 / 1.3
    fun onGenderSelect(value: Gender) = _uiState.update { it.copy(gender = value) }
    fun onAgeChange(value: String) = _uiState.update { it.copy(age = value.digitsOnly()) }
    fun onHeightChange(value: String) = _uiState.update { it.copy(height = value.decimalOnly()) }
    fun onWeightChange(value: String) = _uiState.update { it.copy(weight = value.decimalOnly()) }
    fun onSkeletalMuscleMassChange(value: String) = _uiState.update { it.copy(skeletalMuscleMass = value.decimalOnly()) }
    fun onBodyFatPercentageChange(value: String) = _uiState.update { it.copy(bodyFatPercentage = value.decimalOnly()) }
    fun onBodyFatMassChange(value: String) = _uiState.update { it.copy(bodyFatMass = value.decimalOnly()) }

    fun skipOptionalBodyInfo() =
        _uiState.update { it.copy(skeletalMuscleMass = "", bodyFatPercentage = "", bodyFatMass = "") }

    // 1.4 ~ 1.7
    fun onExerciseLevelSelect(value: ExerciseLevel) = _uiState.update { it.copy(exerciseLevel = value) }
    fun onCurrentStatusSelect(value: CurrentExerciseStatus) = _uiState.update { it.copy(currentStatus = value) }

    // 목표가 바뀌면 이전 목표의 운동 방식은 고를 수 없으니 비운다
    fun onPrimaryGoalSelect(value: PrimaryGoal) = _uiState.update {
        it.copy(primaryGoal = value, goalStrategy = it.goalStrategy?.takeIf { strategy -> strategy.goal == value })
    }

    fun onGoalStrategySelect(value: GoalStrategy) = _uiState.update { it.copy(goalStrategy = value) }
    fun onDayToggle(day: String) = _uiState.update { it.copy(availableDays = it.availableDays.toggle(day)) }
    fun onDurationSelect(label: String) = _uiState.update { it.copy(durationLabel = label) }

    // 1.8 / 1.9
    fun onLocationSelect(label: String) {
        val location = PlaceLabels[label] ?: return
        _uiState.update { it.copy(location = location) }
        loadEquipments(location)
    }

    fun onGymNameChange(value: String) = _uiState.update { it.copy(gymName = value) }

    fun onEquipmentToggle(code: String) =
        _uiState.update { it.copy(selectedEquipmentCodes = it.selectedEquipmentCodes.toggle(code)) }

    fun retryEquipments() {
        _uiState.value.location?.let(::loadEquipments)
    }

    // 1.10
    fun onConcernToggle() = _uiState.update { it.copy(hasConcern = !it.hasConcern) }
    fun onBodyAreaToggle(area: BodyArea) = _uiState.update { it.copy(avoidBodyAreas = it.avoidBodyAreas.toggle(area)) }
    fun onAvoidExercisesChange(names: List<String>) = _uiState.update { it.copy(avoidExercises = names) }
    fun onAvoidExerciseRemove(name: String) = _uiState.update { it.copy(avoidExercises = it.avoidExercises - name) }

    // 1.11
    fun onRecordExercisesChange(names: List<String>) = _uiState.update {
        it.copy(recordExercises = names, records = it.records.filterKeys { name -> name in names })
    }

    fun onRecordChange(name: String, input: RecordInput) = _uiState.update {
        it.copy(
            records = it.records + (name to input.copy(
                weight = input.weight.digitsOnly(),
                reps = input.reps.digitsOnly(),
                sets = input.sets.digitsOnly(),
                minutes = input.minutes.digitsOnly()
            ))
        )
    }

    fun retryExercises() = loadExercises()

    // 각 단계의 "다음"에서 호출한다. 통과하지 못하면 안내 문구를 띄우고 false
    fun validate(step: SignupStep): Boolean {
        val error = _uiState.value.validationError(step) ?: return true
        message(error)
        return false
    }

    // 1.11 완료 / 건너뛰기. skipRecords면 최근 운동 기록 없이 가입한다
    fun submit(skipRecords: Boolean) {
        val state = _uiState.value
        if (state.isSubmitting) return
        SignupStep.entries.forEach { step -> if (!validate(step)) return }

        val form = state.toForm(skipRecords) ?: run {
            message("입력한 정보를 다시 확인해주세요.")
            return
        }

        _uiState.update { it.copy(isSubmitting = true) }
        viewModelScope.launch {
            authRepository.signUp(form)
                .onSuccess {
                    authRepository.login(form.loginId, form.password)
                        .onSuccess { _events.send(SignupEvent.Completed) }
                        .onFailure {
                            message("회원가입이 완료됐어요. 로그인해주세요.")
                            _events.send(SignupEvent.NeedLogin)
                        }
                }
                .onFailure { error -> message(error.message) }
            _uiState.update { it.copy(isSubmitting = false) }
        }
    }

    private fun loadExercises() {
        viewModelScope.launch {
            catalogRepository.getExercises()
                .onSuccess { exercises -> _uiState.update { it.copy(exercises = exercises) } }
                .onFailure { error -> message(error.message) }
        }
    }

    private fun loadEquipments(location: ExerciseLocation) {
        if (_uiState.value.equipments.containsKey(location)) return
        _uiState.update { it.copy(isEquipmentLoading = true) }
        viewModelScope.launch {
            catalogRepository.getEquipments(location)
                .onSuccess { list -> _uiState.update { it.copy(equipments = it.equipments + (location to list)) } }
                .onFailure { error -> message(error.message) }
            _uiState.update { it.copy(isEquipmentLoading = false) }
        }
    }

    private fun message(text: String?) {
        _events.trySend(SignupEvent.Message(text ?: "잠시 후 다시 시도해주세요."))
    }
}

enum class SignupStep { Account, BasicInfo, OptionalBodyInfo, Experience, Goal, Method, Schedule, Place, Equipment }

private fun SignupUiState.validationError(step: SignupStep): String? = when (step) {
    SignupStep.Account -> when {
        loginId.length !in 8..16 -> "아이디는 8~16자로 입력해주세요."
        idCheck != IdCheckState.Available -> "아이디 중복확인을 해주세요."
        !PasswordRegex.matches(password) -> "비밀번호는 영문, 숫자를 포함해 10자 이상으로 입력해주세요."
        password != passwordConfirm -> "비밀번호가 일치하지 않아요."
        else -> null
    }
    SignupStep.BasicInfo -> when {
        gender == null -> "성별을 선택해주세요."
        age.toIntOrNull()?.takeIf { it > 0 } == null -> "나이를 입력해주세요."
        height.toDoubleOrNull()?.takeIf { it > 0 } == null -> "키를 입력해주세요."
        weight.toDoubleOrNull()?.takeIf { it > 0 } == null -> "몸무게를 입력해주세요."
        else -> null
    }
    SignupStep.OptionalBodyInfo ->
        if (listOf(skeletalMuscleMass, bodyFatPercentage, bodyFatMass).any { it.isNotEmpty() && it.toDoubleOrNull() == null }) {
            "신체 측정 정보를 숫자로 입력해주세요."
        } else null
    SignupStep.Experience -> when {
        exerciseLevel == null -> "운동 경험을 선택해주세요."
        currentStatus == null -> "현재 운동 상태를 선택해주세요."
        else -> null
    }
    SignupStep.Goal -> if (primaryGoal == null) "운동 목표를 선택해주세요." else null
    SignupStep.Method -> if (goalStrategy == null) "운동 방식을 선택해주세요." else null
    SignupStep.Schedule -> when {
        availableDays.isEmpty() -> "운동 가능한 요일을 선택해주세요."
        durationLabel == null -> "한 번에 운동할 수 있는 시간을 선택해주세요."
        else -> null
    }
    SignupStep.Place -> when {
        location == null -> "운동 장소를 선택해주세요."
        location == ExerciseLocation.GYM && gymName.isBlank() -> "헬스장 이름을 입력해주세요."
        else -> null
    }
    // 기구가 하나도 없어도 맨몸 운동은 추천받을 수 있어서 선택하지 않아도 넘어간다
    SignupStep.Equipment -> null
}

private fun SignupUiState.toForm(skipRecords: Boolean): SignUpForm? {
    val location = location ?: return null
    val locationEquipmentCodes = equipments[location].orEmpty().map { it.code }.toSet()
    return SignUpForm(
        loginId = loginId,
        password = password,
        gender = gender ?: return null,
        age = age.toIntOrNull() ?: return null,
        height = height.toDoubleOrNull() ?: return null,
        weight = weight.toDoubleOrNull() ?: return null,
        skeletalMuscleMass = skeletalMuscleMass.toDoubleOrNull(),
        bodyFatPercentage = bodyFatPercentage.toDoubleOrNull(),
        bodyFatMass = bodyFatMass.toDoubleOrNull(),
        exerciseLevel = exerciseLevel ?: return null,
        currentExerciseStatus = currentStatus ?: return null,
        primaryGoal = primaryGoal ?: return null,
        goalStrategy = goalStrategy ?: return null,
        sessionMinutes = durationLabel?.let(::durationLabelToMinutes) ?: return null,
        availableDays = availableDays.mapNotNull { WorkoutDayLabels[it] }.toSet(),
        exerciseLocation = location,
        gymName = gymName.trim().takeIf { location == ExerciseLocation.GYM },
        // 다른 장소에서 골랐던 기구는 빼고 보낸다
        equipmentCodes = selectedEquipmentCodes intersect locationEquipmentCodes,
        avoidBodyAreas = if (hasConcern) avoidBodyAreas else emptySet(),
        excludedExerciseCodes = if (hasConcern) avoidExercises.mapNotNull { exerciseByName(it)?.code }.toSet() else emptySet(),
        recentExerciseRecords = if (skipRecords) emptyList() else recordExercises.mapNotNull { name -> toRecord(name) }
    )
}

// 아무 값도 입력하지 않은 기록은 보내지 않는다
private fun SignupUiState.toRecord(name: String): RecentExerciseRecord? {
    val exercise = exerciseByName(name) ?: return null
    val input = records[name] ?: return null
    val record = when (exercise.recordType.toRecordType()) {
        RecordType.Weight -> RecentExerciseRecord(
            exerciseCode = exercise.code,
            weight = input.weight.toIntOrNull(),
            reps = input.reps.toIntOrNull(),
            sets = input.sets.toIntOrNull()
        )
        RecordType.Bodyweight -> RecentExerciseRecord(
            exerciseCode = exercise.code,
            reps = input.reps.toIntOrNull(),
            sets = input.sets.toIntOrNull()
        )
        RecordType.Cardio -> RecentExerciseRecord(
            exerciseCode = exercise.code,
            durationSeconds = input.minutes.toIntOrNull()?.times(60)
        )
    }
    val isEmpty = listOf(record.weight, record.reps, record.sets, record.durationSeconds).all { it == null }
    return record.takeUnless { isEmpty }
}

private fun String.digitsOnly(): String = filter { it.isDigit() }

private fun String.decimalOnly(): String {
    val filtered = filter { it.isDigit() || it == '.' }
    val firstDot = filtered.indexOf('.')
    return if (firstDot < 0) filtered else filtered.substring(0, firstDot + 1) + filtered.substring(firstDot + 1).replace(".", "")
}
