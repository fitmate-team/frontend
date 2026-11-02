package com.fitmate.presentation.routine

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitmate.domain.model.DailyRoutine
import com.fitmate.domain.model.EquipmentCategory
import com.fitmate.domain.model.Exercise
import com.fitmate.domain.model.ExerciseLocation
import com.fitmate.domain.model.MemberInfo
import com.fitmate.domain.model.PrescriptionRecommendation
import com.fitmate.domain.model.WeeklyRoutineRecommendation
import com.fitmate.domain.model.WorkoutEnvironment
import com.fitmate.domain.repository.CatalogRepository
import com.fitmate.domain.repository.MemberRepository
import com.fitmate.domain.repository.RoutineRepository
import com.fitmate.presentation.designsystem.component.SearchableExercise
import com.fitmate.presentation.designsystem.component.WORKOUT_PLACE_GYM
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.roundToInt

// AI 추천 결과 / 추천 이유 / 일자별 미리보기 화면에 보여줄 내용
data class AiRoutineResultUi(
    val tags: List<String>,
    val summary: String,
    val days: List<RecommendedDay>,
    val referenceInfo: RoutineReferenceInfo,
    val compositionReasons: List<CompositionReason>,
    val dayReasons: List<DayReason>,
    val dayDetails: Map<String, RoutineDayDetailUi>
)

data class RoutineCreateUiState(
    // 내 정보 / 운동 환경을 불러오기 전이면 null
    val condition: RoutineCondition? = null,
    val savedGyms: List<String> = emptyList(),
    val aiResult: AiRoutineResultUi? = null,
    // 추천 결과에서 연 일자별 미리보기 (요일 문구)
    val previewDay: String = "월",
    // AI와 함께 만들기
    val draft: ManualRoutineDraft = ManualRoutineDraft(),
    val editingDay: String = "월",
    val searchableExercises: List<SearchableExercise> = emptyList(),
    val selectedExerciseNames: List<String> = emptyList(),
    val configSettings: List<ExerciseSetting> = emptyList(),
    val recommendations: Map<String, PrescriptionRecommendation> = emptyMap()
)

sealed interface RoutineCreateEvent {
    data class Message(val text: String) : RoutineCreateEvent
    // 로딩 화면에서 기다리던 요청이 실패. 안내 후 이전 화면으로 돌아간다
    data class LoadingFailed(val text: String) : RoutineCreateEvent
    data object RecommendationReady : RoutineCreateEvent
    data object AiExercisesReady : RoutineCreateEvent
    data object Saved : RoutineCreateEvent
}

// 루틴 만들기 그래프 범위에서 공유한다 (방식 선택 ~ 저장)
@HiltViewModel
class RoutineCreateViewModel @Inject constructor(
    private val routineRepository: RoutineRepository,
    private val memberRepository: MemberRepository,
    private val catalogRepository: CatalogRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RoutineCreateUiState())
    val uiState: StateFlow<RoutineCreateUiState> = _uiState.asStateFlow()

    private val _events = Channel<RoutineCreateEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    private var member: MemberInfo? = null
    private var environments: List<WorkoutEnvironment> = emptyList()
    private var catalog: Map<String, Exercise> = emptyMap()

    // 운동 환경에서 할 수 있는 운동. 검색 목록은 이름으로 고르기 때문에 이름으로 찾는다
    private var availableExercises: Map<String, Exercise> = emptyMap()
    private var availableEnvironmentId: Long? = null

    // 추천받을 때 쓴 조건. 저장할 때 같은 값을 보낸다
    private var recommendation: WeeklyRoutineRecommendation? = null
    private var recommendedEnvironmentId: Long? = null
    private var recommendedMinutes: Int? = null

    private var loadJob: Job? = null
    private var requestJob: Job? = null
    private var prescriptionJob: Job? = null

    init {
        ensureLoaded()
    }

    // 조건 화면에 필요한 내 정보 / 운동 환경. 실패했으면 화면에 다시 들어올 때 재시도한다
    fun ensureLoaded() {
        if (_uiState.value.condition != null || loadJob?.isActive == true) return
        loadJob = viewModelScope.launch {
            val memberResult = async { memberRepository.getMyInfo() }
            val environmentResult = async { memberRepository.getWorkoutEnvironments() }
            val catalogResult = async { catalogRepository.getExercises() }

            val loadedMember = memberResult.await().getOrElse { return@launch sendMessage(it) }
            val loadedEnvironments = environmentResult.await().getOrElse { return@launch sendMessage(it) }
            catalog = catalogResult.await().getOrNull().orEmpty().associateBy { it.code }
            member = loadedMember
            environments = loadedEnvironments.environments

            val gyms = environments.filter { it.location == ExerciseLocation.GYM }.mapNotNull { it.gymName }
            _uiState.update {
                it.copy(
                    savedGyms = gyms,
                    condition = RoutineCondition(
                        goal = "${loadedMember.primaryGoal.label()} · ${loadedMember.goalStrategy.label()}",
                        days = loadedMember.availableDays.map { day -> day.label() }.toSet(),
                        duration = minutesToDurationLabel(loadedMember.sessionMinutes),
                        place = loadedEnvironments.primaryLocation.label(),
                        gymName = gyms.firstOrNull().orEmpty()
                    )
                )
            }
        }
    }

    fun updateCondition(condition: RoutineCondition) {
        _uiState.update { it.copy(condition = condition) }
    }

    // ---------- AI에게 맡기기 ----------

    // 로딩 화면에 들어오면 호출한다. 화면 회전 등으로 다시 불려도 요청은 한 번만 보낸다
    fun recommendWeeklyRoutine() {
        if (requestJob?.isActive == true) return
        val condition = _uiState.value.condition ?: return loadingFailed("루틴 조건을 불러오지 못했어요.")
        val environmentId = environmentIdOf(condition) ?: return loadingFailed("운동 장소를 다시 선택해주세요.")
        val days = condition.days.mapNotNull(::workoutDayOf).toSet()
        val minutes = durationLabelToMinutes(condition.duration)
        if (days.isEmpty() || minutes == null) return loadingFailed("운동 요일과 시간을 선택해주세요.")

        requestJob = viewModelScope.launch {
            routineRepository.recommendWeeklyRoutine(days, minutes, environmentId)
                .onSuccess { result ->
                    recommendation = result
                    recommendedEnvironmentId = environmentId
                    recommendedMinutes = minutes
                    _uiState.update { it.copy(aiResult = result.toUi(condition, minutes)) }
                    _events.send(RoutineCreateEvent.RecommendationReady)
                }
                .onFailure { loadingFailed(it.message ?: "루틴을 만들지 못했어요.") }
        }
    }

    fun openPreview(dayLabel: String) {
        _uiState.update { it.copy(previewDay = dayLabel) }
    }

    fun saveRecommendedRoutine() {
        if (requestJob?.isActive == true) return
        val routine = recommendation ?: return
        val environmentId = recommendedEnvironmentId ?: return
        val minutes = recommendedMinutes ?: return
        requestJob = viewModelScope.launch {
            routineRepository.saveWeeklyRoutine(environmentId, minutes, routine.days)
                .onSuccess { _events.send(RoutineCreateEvent.Saved) }
                .onFailure { sendMessage(it) }
        }
    }

    // ---------- AI와 함께 만들기 ----------

    // 요일을 누르면 그 요일에 이미 고른 운동을 선택한 채로 운동 검색을 연다
    fun startEditingDay(dayLabel: String) {
        _uiState.update { state ->
            state.copy(
                editingDay = dayLabel,
                selectedExerciseNames = state.draft.days[dayLabel].orEmpty().map { it.name }
            )
        }
    }

    // 운동 검색 화면에 들어오면 호출한다. 운동 장소를 아직 못 불러왔으면 불러온 뒤 다시 호출된다
    fun loadAvailableExercises() {
        val environmentId = _uiState.value.condition?.let(::environmentIdOf) ?: return
        viewModelScope.launch { loadAvailableExercises(environmentId) }
    }

    fun openDay(dayLabel: String) {
        _uiState.update { it.copy(editingDay = dayLabel) }
    }

    // AI 추천 로딩 화면에 들어오면 호출한다
    fun recommendExercises() {
        if (requestJob?.isActive == true) return
        val environmentId = _uiState.value.condition?.let(::environmentIdOf)
            ?: return loadingFailed("운동 장소를 찾을 수 없어요.")
        requestJob = viewModelScope.launch {
            loadAvailableExercises(environmentId)
            routineRepository.recommendExercises(environmentId)
                .onSuccess { codes ->
                    val names = codes.mapNotNull { code -> availableExercises.values.firstOrNull { it.code == code }?.name }
                    _uiState.update { it.copy(selectedExerciseNames = names) }
                    _events.send(RoutineCreateEvent.AiExercisesReady)
                }
                .onFailure { loadingFailed(it.message ?: "운동을 추천받지 못했어요.") }
        }
    }

    // 고른 운동으로 설정 화면을 연다. 이미 설정한 운동은 값을 이어서 쓰고, 처방값 추천은 뒤에서 받아 빈 칸을 채운다
    fun configureExercises(names: List<String>) {
        val state = _uiState.value
        val existing = state.draft.days[state.editingDay].orEmpty().associateBy { it.code }
        val settings = names.mapNotNull { name ->
            val exercise = availableExercises[name] ?: return@mapNotNull null
            existing[exercise.code] ?: ExerciseSetting(
                code = exercise.code,
                name = exercise.name,
                recordType = exercise.recordType,
                muscle = exercise.primaryMuscle
            )
        }
        _uiState.update { it.copy(selectedExerciseNames = names, configSettings = settings) }

        prescriptionJob?.cancel()
        prescriptionJob = viewModelScope.launch {
            routineRepository.recommendPrescriptions(settings.map { it.code })
                .onSuccess { recommendations ->
                    _uiState.update { current ->
                        current.copy(
                            recommendations = current.recommendations + recommendations,
                            configSettings = current.configSettings.map { it.fillBlanks(recommendations[it.code]) }
                        )
                    }
                }
        }
    }

    fun onSettingChange(index: Int, setting: ExerciseSetting) {
        _uiState.update { state ->
            state.copy(configSettings = state.configSettings.toMutableList().also { it[index] = setting })
        }
    }

    // 모든 운동을 입력했으면 초안에 넣고 true
    fun finishConfiguration(): Boolean {
        val state = _uiState.value
        if (state.configSettings.isEmpty() || state.configSettings.any { !it.isComplete }) {
            _events.trySend(RoutineCreateEvent.Message("모든 운동의 세트와 횟수(유산소는 시간)를 입력해주세요."))
            return false
        }
        _uiState.update {
            it.copy(draft = it.draft.copy(days = it.draft.days + (it.editingDay to it.configSettings)))
        }
        return true
    }

    // 저장할 요일이 있으면 true. 저장은 로딩 화면에서 saveManualRoutine으로 한다
    fun canSaveManualRoutine(): Boolean {
        if (_uiState.value.draft.days.isNotEmpty()) return true
        _events.trySend(RoutineCreateEvent.Message("운동할 요일을 하나 이상 추가해주세요."))
        return false
    }

    // 요일마다 AI가 대표 부위 / 예상 시간을 정한 뒤 이번 주 루틴으로 저장한다
    fun saveManualRoutine() {
        if (requestJob?.isActive == true) return
        val draft = _uiState.value.draft
        val firstDay = draft.days.keys.firstOrNull()?.let(::workoutDayOf) ?: return loadingFailed("운동할 요일을 추가해주세요.")
        val environmentId = _uiState.value.condition?.let(::environmentIdOf)
            ?: return loadingFailed("운동 장소를 찾을 수 없어요.")

        requestJob = viewModelScope.launch {
            val days = runCatching {
                coroutineScope {
                    draft.days.mapNotNull { (label, settings) ->
                        val day = workoutDayOf(label) ?: return@mapNotNull null
                        async {
                            val exercises = settings.map { it.toPrescription() }
                            val summary = routineRepository.summarizeDailyRoutine(exercises).getOrThrow()
                            DailyRoutine(day, summary.mainTarget, summary.estimatedMinutes, exercises)
                        }
                    }.awaitAll()
                }
            }.getOrElse { return@launch loadingFailed(it.message ?: "루틴을 저장하지 못했어요.") }

            routineRepository.saveCustomRoutine(firstDay, environmentId, days.sortedBy { it.day })
                .onSuccess { _events.send(RoutineCreateEvent.Saved) }
                .onFailure { loadingFailed(it.message ?: "루틴을 저장하지 못했어요.") }
        }
    }

    // ---------- 공통 ----------

    // 헬스장은 고른 헬스장 이름으로, 집 / 야외는 장소로 운동 환경을 찾는다
    private fun environmentIdOf(condition: RoutineCondition): Long? {
        val location = exerciseLocationOf(condition.place) ?: return null
        val candidates = environments.filter { it.location == location }
        val matched = if (condition.place == WORKOUT_PLACE_GYM) {
            candidates.firstOrNull { it.gymName == condition.gymName } ?: candidates.firstOrNull()
        } else {
            candidates.firstOrNull()
        }
        return matched?.id
    }

    private suspend fun loadAvailableExercises(environmentId: Long) {
        if (availableEnvironmentId == environmentId) return
        catalogRepository.getAvailableExercises(environmentId)
            .onSuccess { exercises ->
                availableEnvironmentId = environmentId
                availableExercises = exercises.associateBy { it.name }
                _uiState.update { it.copy(searchableExercises = exercises.map { exercise -> exercise.toSearchable() }) }
            }
            .onFailure { sendMessage(it) }
    }

    private fun WeeklyRoutineRecommendation.toUi(condition: RoutineCondition, minutes: Int): AiRoutineResultUi {
        val recordTypes = catalog.mapValues { it.value.recordType }
        val member = member
        return AiRoutineResultUi(
            tags = listOfNotNull(member?.primaryGoal?.label(), member?.goalStrategy?.label()),
            summary = "주 ${days.size}회 · 회당 약 ${minutes}분",
            days = days.map { day ->
                RecommendedDay(
                    dayLabel = day.day.label(),
                    title = day.mainTarget.label(),
                    exercises = day.exercises.namesPreview(),
                    duration = "약 ${day.estimatedMinutes}분",
                    icon = day.mainTarget.planIcon()
                )
            },
            referenceInfo = RoutineReferenceInfo(
                goal = condition.goal,
                experience = member?.exerciseLevel?.label() ?: "-",
                frequency = "주 ${days.size}회",
                duration = "회당 약 ${minutes}분",
                environment = condition.placeLabel,
                body = member?.let { info ->
                    listOfNotNull("${info.height.roundToInt()}cm", info.weight?.let { "${it.roundToInt()}kg" }).joinToString(" / ")
                } ?: "-"
            ),
            compositionReasons = reasons.map { CompositionReason(it.title, it.reason) },
            dayReasons = days.map { day ->
                DayReason(
                    dayLabel = day.day.label(),
                    title = day.mainTarget.label(),
                    focus = day.focus.orEmpty(),
                    detail = day.reason.orEmpty()
                )
            },
            dayDetails = days.associate { it.day.label() to it.toDetailUi(recordTypes) }
        )
    }

    private suspend fun sendMessage(error: Throwable) {
        _events.send(RoutineCreateEvent.Message(error.message ?: "잠시 후 다시 시도해주세요."))
    }

    private fun loadingFailed(text: String) {
        _events.trySend(RoutineCreateEvent.LoadingFailed(text))
    }
}

private fun Exercise.toSearchable() = SearchableExercise(
    name = name,
    description = "${primaryMuscle.bodyPartLabel()} · ${equipmentCategories.firstOrNull()?.label() ?: "맨몸"}",
    filter = primaryMuscle.filterLabel()
)

private fun EquipmentCategory.label(): String = when (this) {
    EquipmentCategory.FREE_WEIGHT -> "프리웨이트"
    EquipmentCategory.BAND_RESISTANCE -> "밴드/저항"
    EquipmentCategory.BENCH_RACK -> "벤치/랙"
    EquipmentCategory.ACCESSORY -> "보조 기구"
    EquipmentCategory.CARDIO -> "유산소 기구"
    EquipmentCategory.MACHINE_CABLE -> "머신/케이블"
}

// 사용자가 비워둔 칸만 AI 추천값으로 채운다
private fun ExerciseSetting.fillBlanks(recommendation: PrescriptionRecommendation?): ExerciseSetting {
    if (recommendation == null) return this
    return copy(
        sets = sets.ifBlank { recommendation.sets?.toString().orEmpty() },
        reps = reps.ifBlank { recommendation.reps?.toString().orEmpty() },
        weight = weight.ifBlank { recommendation.weightKg?.toString().orEmpty() },
        minutes = minutes.ifBlank { recommendation.durationSeconds?.let { (it / 60).toString() }.orEmpty() },
        rest = rest ?: recommendation.restSeconds?.takeIf { it > 0 }?.let(::restLabel)?.takeIf { it in RestTimeOptions }
    )
}
