package com.fitmate.presentation.workout.today

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitmate.domain.model.DailyRoutine
import com.fitmate.domain.model.DailyRoutineRecommendation
import com.fitmate.domain.model.DailyExerciseReplacement
import com.fitmate.domain.model.ExerciseCatalogItem
import com.fitmate.domain.model.ExercisePrescription
import com.fitmate.domain.model.TodayWorkout
import com.fitmate.domain.model.WorkoutDay
import com.fitmate.domain.model.WorkoutEnvironments
import com.fitmate.domain.model.WorkoutExercise
import com.fitmate.domain.model.WorkoutLocationValidation
import com.fitmate.domain.repository.RoutineRepository
import com.fitmate.domain.repository.WorkoutRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class WorkoutUiState(
    val isLoading: Boolean = false,
    val workout: TodayWorkout? = null,
    val exerciseCatalog: List<ExerciseCatalogItem> = emptyList(),
    val errorMessage: String? = null,
    val isStarting: Boolean = false,
    val workoutRecordId: Long? = null,
    val startErrorMessage: String? = null,
    val environments: WorkoutEnvironments? = null,
    val isLocationLoading: Boolean = false,
    val isLocationSaving: Boolean = false,
    val locationValidation: WorkoutLocationValidation? = null,
    val locationErrorMessage: String? = null,
    val isLocationUpdated: Boolean = false,
    val showLocationChangedDialog: Boolean = false,
    val isRerecommendationReady: Boolean = false,
    // 기존 NavHost 호환을 위한 당일 1개짜리 리스트
    val recommendedDays: List<DailyRoutine> = emptyList(),
    val originalDays: List<DailyRoutine> = emptyList(),
    val recommendedEnvironmentId: Long? = null,
    val recommendedSessionMinutes: Int = 0,
    val dailyRecommendation: DailyRoutineRecommendation? = null,
    val originalWorkoutExercises: List<WorkoutExercise> = emptyList(),
    val isCatalogLoading: Boolean = false,
    val catalogErrorMessage: String? = null,
    val isExerciseSaving: Boolean = false,
    val isExerciseSaved: Boolean = false,
    val exerciseSaveErrorMessage: String? = null,
    val isRestDay: Boolean = false,
    val hasNoWeeklyRoutine: Boolean = false,

)

@HiltViewModel
class WorkoutViewModel @Inject constructor(
    private val workoutRepository: WorkoutRepository,
    private val routineRepository: RoutineRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(WorkoutUiState())
    val uiState: StateFlow<WorkoutUiState> = _uiState.asStateFlow()

    init { loadTodayWorkout() }

    fun loadTodayWorkout() {
        if (_uiState.value.isLoading) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                isRestDay = false,
                hasNoWeeklyRoutine = false,
                workout = null,
                errorMessage = null
            )

            workoutRepository.getTodayWorkout()
                .onSuccess { workout ->
                    val catalog = workoutRepository
                        .getExerciseCatalog()
                        .getOrDefault(emptyList())

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isRestDay = false,
                        hasNoWeeklyRoutine = false,
                        workout = workout,
                        exerciseCatalog = catalog,
                        errorMessage = null
                    )
                }
                .onFailure { error ->

                    // 홈 API를 통해 주간 루틴 및 휴식일 확인
                    val home = routineRepository
                        .getHomeSummary()
                        .getOrNull()

                    when {
                        // 이번 주 루틴이 없는 경우
                        home != null && !home.hasWeeklyRoutine -> {
                            _uiState.value = _uiState.value.copy(
                                isLoading = false,
                                hasNoWeeklyRoutine = true,
                                isRestDay = false,
                                workout = null,
                                errorMessage = null
                            )
                        }

                        // 주간 루틴은 있지만 오늘은 쉬는 날
                        home != null &&
                                home.hasWeeklyRoutine &&
                                home.today == null -> {

                            _uiState.value = _uiState.value.copy(
                                isLoading = false,
                                isRestDay = true,
                                hasNoWeeklyRoutine = false,
                                workout = null,
                                errorMessage = null
                            )
                        }

                        // 실제 API 호출 실패
                        else -> {
                            _uiState.value = _uiState.value.copy(
                                isLoading = false,
                                isRestDay = false,
                                hasNoWeeklyRoutine = false,
                                workout = null,
                                errorMessage = error.message
                                    ?: "오늘의 운동을 불러오지 못했어요."
                            )
                        }
                    }
                }
        }
    }

    fun startWorkout() {
        val dailyRoutineId = _uiState.value.workout?.dailyRoutineId ?: return
        if (_uiState.value.isStarting || _uiState.value.workoutRecordId != null) return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isStarting = true, startErrorMessage = null)
            workoutRepository.startWorkout(dailyRoutineId)
                .onSuccess { recordId ->
                    _uiState.value = _uiState.value.copy(
                        isStarting = false, workoutRecordId = recordId
                    )
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isStarting = false,
                        startErrorMessage = error.message ?: "운동을 시작하지 못했어요."
                    )
                }
        }
    }

    fun clearStartError() {
        _uiState.value = _uiState.value.copy(startErrorMessage = null)
    }

    fun loadWorkoutEnvironments() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLocationLoading = true, locationErrorMessage = null
            )
            workoutRepository.getWorkoutEnvironments()
                .onSuccess { environments ->
                    _uiState.value = _uiState.value.copy(
                        isLocationLoading = false, environments = environments
                    )
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLocationLoading = false,
                        locationErrorMessage = error.message ?: "운동 장소를 불러오지 못했어요."
                    )
                }
        }
    }

    fun validateWorkoutLocation(workoutEnvironmentId: Long) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLocationLoading = true, locationValidation = null,
                locationErrorMessage = null
            )
            workoutRepository.validateWorkoutLocation(workoutEnvironmentId)
                .onSuccess { validation ->
                    _uiState.value = _uiState.value.copy(
                        isLocationLoading = false, locationValidation = validation
                    )
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLocationLoading = false,
                        locationErrorMessage = error.message ?: "운동 장소를 확인하지 못했어요."
                    )
                }
        }
    }

    fun updateWorkoutLocation(workoutEnvironmentId: Long) {
        if (_uiState.value.isLocationSaving) return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLocationSaving = true,
                isLocationUpdated = false,
                isRerecommendationReady = false,
                showLocationChangedDialog = false,
                recommendedDays = emptyList(),
                originalDays = emptyList(),
                recommendedEnvironmentId = null,
                recommendedSessionMinutes = 0,
                dailyRecommendation = null,
                originalWorkoutExercises = emptyList(),
                locationValidation = null,
                locationErrorMessage = null
            )
            try {
                val workout = _uiState.value.workout
                    ?: error("오늘의 운동 정보를 찾을 수 없어요.")
                val validation = workoutRepository
                    .validateWorkoutLocation(workoutEnvironmentId).getOrThrow()

                if (validation.requiresRerecommendation) {
                    // 재추천 전에는 PATCH를 호출하지 않는다.
                    // 기존 운동이 새 장소에서 불가능하면 PATCH가 거절될 수 있기 때문이다.
                    val sessionMinutes = workout.estimatedDurationMinutes
                    val recommendation = routineRepository.recommendDailyRoutine(
                        environmentId = workoutEnvironmentId,
                        sessionMinutes = sessionMinutes
                    ).getOrThrow()

                    if (recommendation.replacements.isEmpty()) {
                        error("AI가 교체할 운동을 추천하지 않았어요.")
                    }

                    val catalog = workoutRepository.getExerciseCatalog()
                        .getOrDefault(_uiState.value.exerciseCatalog)
                    val nameByCode = catalog.associate { it.exerciseCode to it.nameKo }
                    val originalByCode = workout.exercises.associateBy { it.exerciseCode }
                    val today = WorkoutDay.valueOf(
                        java.util.Calendar.getInstance().getDisplayName(
                            java.util.Calendar.DAY_OF_WEEK,
                            java.util.Calendar.LONG,
                            java.util.Locale.US
                        )!!.uppercase(java.util.Locale.US)
                    )

                    // replacements에는 변경된 운동만 포함될 수도 있으므로,
                    // 기존 운동 목록의 순서를 기준으로 전체 당일 루틴을 만든다.
                    val replacementByOriginal = recommendation.replacements.associateBy {
                        it.originalExerciseCode
                    }
                    val displayedExercises = workout.exercises.map { original ->
                        val replacement = replacementByOriginal[original.exerciseCode]
                            ?.replacementExercise
                        val code = replacement?.exerciseCode ?: original.exerciseCode
                        ExercisePrescription(
                            exerciseCode = code,
                            name = nameByCode[code]
                                ?: original.exerciseName ?: code,
                            sets = replacement?.sets ?: original.sets ?: 1,
                            reps = replacement?.reps ?: original.reps,
                            weightKg = if (replacement != null) {
                                replacement.weightKg
                            } else {
                                original.weightKg
                            },

                            durationSeconds = if (replacement != null) {
                                replacement.durationSeconds
                            } else {
                                original.durationSeconds
                            },
                            restSeconds = replacement?.restSeconds
                                ?: original.restSeconds ?: 60
                        )
                    }
                    val target = enumValueOf<com.fitmate.domain.model.RoutineTarget>(
                        recommendation.mainTarget
                    )
                    val daily = DailyRoutine(
                        day = today,
                        mainTarget = target,
                        estimatedMinutes = recommendation.estimatedMinutes,
                        exercises = displayedExercises
                    )
                    val originalDaily = daily.copy(
                        exercises = workout.exercises.map { original ->
                            ExercisePrescription(
                                exerciseCode = original.exerciseCode,
                                name = original.exerciseName ?: original.exerciseCode,
                                sets = original.sets ?: 1,
                                reps = original.reps,
                                weightKg = original.weightKg,
                                durationSeconds = original.durationSeconds,
                                restSeconds = original.restSeconds ?: 60
                            )
                        }
                    )
                    _uiState.value = _uiState.value.copy(
                        isLocationSaving = false,
                        isRerecommendationReady = true,
                        dailyRecommendation = recommendation,
                        originalWorkoutExercises = workout.exercises,
                        originalDays = listOf(originalDaily),
                        recommendedDays = listOf(daily),
                        exerciseCatalog = catalog,
                        recommendedEnvironmentId = workoutEnvironmentId,
                        recommendedSessionMinutes = sessionMinutes
                    )
                } else {
                    workoutRepository.updateWorkoutLocation(workoutEnvironmentId)
                        .getOrThrow()
                    val updatedWorkout = workoutRepository.getTodayWorkout().getOrThrow()
                    _uiState.value = _uiState.value.copy(
                        isLocationSaving = false,
                        isLocationUpdated = false,
                        showLocationChangedDialog = true,
                        workout = updatedWorkout
                    )
                }
            } catch (error: Exception) {
                Log.e("FitMateWorkout", "운동 장소 변경 또는 당일 AI 재추천 실패", error)
                _uiState.value = _uiState.value.copy(
                    isLocationSaving = false,
                    isRerecommendationReady = false,
                    locationErrorMessage = error.message
                        ?: "오늘의 운동을 다시 추천하지 못했어요."
                )
            }
        }
    }

    fun confirmLocationChanged() {
        _uiState.value = _uiState.value.copy(
            showLocationChangedDialog = false, isLocationUpdated = true
        )
    }

    // 기존 NavHost가 사용하는 함수 이름을 유지한다.
    // 현재 API는 originalExerciseCode 기반 교체이므로 운동 개수 변경은 막는다.
    fun updateRecommendedExercises(
        day: WorkoutDay,
        exercises: List<ExercisePrescription>
    ): Boolean {
        val state = _uiState.value

        if (!state.isRerecommendationReady || state.isLocationSaving) {
            return false
        }

        val current = state.recommendedDays
            .firstOrNull { it.day == day }
            ?: return false

        if (exercises.size != current.exercises.size) {
            _uiState.value = state.copy(
                locationErrorMessage =
                    "운동 추가·삭제는 현재 당일 교체 API에서 지원되지 않아요."
            )
            return false
        }

        _uiState.value = state.copy(
            recommendedDays = state.recommendedDays.map { routine ->
                if (routine.day == day) {
                    routine.copy(exercises = exercises)
                } else {
                    routine
                }
            },
            locationErrorMessage = null
        )

        return true
    }

    fun getRecommendedRoutine(day: WorkoutDay): DailyRoutine? =
        _uiState.value.recommendedDays.firstOrNull { it.day == day }

    fun saveRecommendedRoutine() {
        val state = _uiState.value
        if (state.isLocationSaving || !state.isRerecommendationReady) return
        val environmentId = state.recommendedEnvironmentId ?: return
        val recommendation = state.dailyRecommendation ?: return
        val edited = state.recommendedDays.firstOrNull()
        val original = state.originalWorkoutExercises
        if (edited == null || edited.exercises.size != original.size) {
            _uiState.value = state.copy(
                locationErrorMessage = "기존 운동과 수정된 운동의 개수가 일치하지 않아요."
            )
            return
        }

        // 원래 운동 코드와 수정한 운동을 순서대로 대응시킨다.
        // 교체되지 않은 운동은 기존 AI 결과에 없으면 replacements에 추가하지 않는다.
        val existingByOriginal = recommendation.replacements.associateBy {
            it.originalExerciseCode
        }
        val replacements = original.mapIndexedNotNull { index, before ->
            val after = edited.exercises[index]
            val previous = existingByOriginal[before.exerciseCode]
            if (previous == null && before.exerciseCode == after.exerciseCode &&
                before.weightKg == after.weightKg && before.reps == after.reps &&
                before.sets == after.sets && before.durationSeconds == after.durationSeconds &&
                before.restSeconds == after.restSeconds
            ) {
                null
            } else {
                DailyExerciseReplacement(
                    originalExerciseCode = before.exerciseCode,
                    replacementExercise = com.fitmate.domain.model.DailyReplacementExercise(
                        exerciseCode = after.exerciseCode,
                        nameKo = after.name,
                        weightKg = after.weightKg,
                        reps = after.reps,
                        sets = after.sets,
                        durationSeconds = after.durationSeconds,
                        restSeconds = after.restSeconds,
                        appliedAdjustmentId = previous?.replacementExercise?.appliedAdjustmentId
                    )
                )
            }
        }
        val editedRecommendation = recommendation.copy(replacements = replacements)

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLocationSaving = true,
                isLocationUpdated = false,
                locationErrorMessage = null
            )
            try {
                routineRepository.saveDailyRoutine(
                    environmentId = environmentId,
                    sessionMinutes = state.recommendedSessionMinutes,
                    recommendation = editedRecommendation
                ).getOrThrow()

                // POST /api/routine/daily가 새 환경까지 저장하는지는 서버 계약 확인 필요.
                // 여기서 PATCH를 무조건 호출하면 재추천 저장 성공 후 실패할 수 있으므로 생략.
                val updatedWorkout = workoutRepository.getTodayWorkout().getOrThrow()
                _uiState.value = _uiState.value.copy(
                    isLocationSaving = false,
                    isLocationUpdated = true,
                    isRerecommendationReady = false,
                    dailyRecommendation = null,
                    workout = updatedWorkout
                )
            } catch (error: Exception) {
                Log.e("FitMateWorkout", "당일 추천 루틴 저장 실패", error)
                _uiState.value = _uiState.value.copy(
                    isLocationSaving = false,
                    isLocationUpdated = false,
                    locationErrorMessage = error.message ?: "추천 루틴을 저장하지 못했어요."
                )
            }
        }
    }

    fun loadExerciseCatalog() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isCatalogLoading = true, catalogErrorMessage = null
            )
            val environmentId = _uiState.value.recommendedEnvironmentId
            val result = if (_uiState.value.isRerecommendationReady && environmentId != null) {
                workoutRepository.getExercisesAvailableByEnvironment(environmentId)
            } else {
                workoutRepository.getExerciseCatalog()
            }
            result.onSuccess { exercises ->
                _uiState.value = _uiState.value.copy(
                    exerciseCatalog = exercises, isCatalogLoading = false
                )
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    isCatalogLoading = false,
                    catalogErrorMessage = error.message ?: "운동 목록을 불러오지 못했어요."
                )
            }
        }
    }

    fun saveWorkoutExercises(exercises: List<WorkoutExercise>) {
        val dailyRoutineId = _uiState.value.workout?.dailyRoutineId ?: run {
            _uiState.value = _uiState.value.copy(
                exerciseSaveErrorMessage = "오늘의 운동 정보를 찾을 수 없어요."
            )
            return
        }
        if (_uiState.value.isExerciseSaving) return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isExerciseSaving = true,
                isExerciseSaved = false,
                exerciseSaveErrorMessage = null
            )
            try {
                val available = workoutRepository.getAvailableExercises(dailyRoutineId)
                    .getOrThrow()
                val codeById = available.associate { it.exerciseId to it.exerciseCode }
                val withCodes = exercises.map { exercise ->
                    val code = exercise.exerciseCode.takeIf { it.isNotBlank() }
                        ?: exercise.exerciseId?.let { codeById[it] }
                        ?: error("운동 코드를 찾을 수 없어요: ${exercise.exerciseName ?: "알 수 없는 운동"}")
                    exercise.copy(exerciseCode = code)
                }
                workoutRepository.updateDailyRoutineExercises(dailyRoutineId, withCodes)
                    .getOrThrow()
                val updatedWorkout = workoutRepository.getTodayWorkout().getOrThrow()
                _uiState.value = _uiState.value.copy(
                    isExerciseSaving = false, isExerciseSaved = true,
                    workout = updatedWorkout
                )
            } catch (error: Exception) {
                Log.e("FitMateWeight", "운동 설정 저장 실패", error)
                _uiState.value = _uiState.value.copy(
                    isExerciseSaving = false, isExerciseSaved = false,
                    exerciseSaveErrorMessage = error.message ?: "운동 설정 저장에 실패했어요."
                )
            }
        }
    }

    fun saveWorkoutWeights(weights: Map<Int, Int>) {
        val workout = _uiState.value.workout ?: return
        val updated = workout.exercises.mapIndexed { index, exercise ->
            weights[index + 1]?.let { exercise.copy(weightKg = it) } ?: exercise
        }
        saveWorkoutExercises(updated)
    }

    fun clearExerciseSaved() {
        _uiState.value = _uiState.value.copy(isExerciseSaved = false)
    }

    fun clearExerciseSaveError() {
        _uiState.value = _uiState.value.copy(exerciseSaveErrorMessage = null)
    }

    fun clearLocationError() {
        _uiState.value = _uiState.value.copy(locationErrorMessage = null)
    }

    fun clearLocationValidation() {
        _uiState.value = _uiState.value.copy(locationValidation = null)
    }

    fun clearLocationUpdated() {
        _uiState.value = _uiState.value.copy(isLocationUpdated = false)
    }
}
