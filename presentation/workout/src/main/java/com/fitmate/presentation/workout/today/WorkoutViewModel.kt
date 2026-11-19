
package com.fitmate.presentation.workout.today

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitmate.domain.model.TodayWorkout
import com.fitmate.domain.model.ExerciseCatalogItem
import com.fitmate.domain.model.WorkoutEnvironments
import com.fitmate.domain.model.WorkoutLocationValidation
import com.fitmate.domain.model.WorkoutExercise
import com.fitmate.domain.repository.WorkoutRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

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
    val isCatalogLoading: Boolean = false,
    val catalogErrorMessage: String? = null,
    val isExerciseSaving: Boolean = false,
    val isExerciseSaved: Boolean = false,
    val exerciseSaveErrorMessage: String? = null,
)

@HiltViewModel
class WorkoutViewModel @Inject constructor(
    private val workoutRepository: WorkoutRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(WorkoutUiState())

    val uiState: StateFlow<WorkoutUiState> =
        _uiState.asStateFlow()

    init {
        loadTodayWorkout()
    }

    fun loadTodayWorkout() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null
            )

            workoutRepository.getTodayWorkout()
                .onSuccess { workout ->
                    val catalogResult =
                        workoutRepository.getExerciseCatalog()

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        workout = workout,
                        exerciseCatalog = catalogResult.getOrDefault(emptyList()),
                        errorMessage = null
                    )
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = exception.message
                            ?: "오늘의 운동을 불러오지 못했어요."
                    )
                }
        }
    }

    fun startWorkout() {
        val dailyRoutineId = _uiState.value.workout?.dailyRoutineId
            ?: return

        if (
            _uiState.value.isStarting ||
            _uiState.value.workoutRecordId != null
        ) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isStarting = true,
                startErrorMessage = null
            )

            workoutRepository.startWorkout(dailyRoutineId)
                .onSuccess { recordId ->
                    _uiState.value = _uiState.value.copy(
                        isStarting = false,
                        workoutRecordId = recordId
                    )
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isStarting = false,
                        startErrorMessage = error.message
                            ?: "운동을 시작하지 못했어요."
                    )
                }
        }
    }

    fun clearStartError() {
        _uiState.value = _uiState.value.copy(
            startErrorMessage = null
        )
    }

    fun loadWorkoutEnvironments() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLocationLoading = true,
                locationErrorMessage = null
            )

            workoutRepository.getWorkoutEnvironments()
                .onSuccess { environments ->
                    _uiState.value = _uiState.value.copy(
                        isLocationLoading = false,
                        environments = environments
                    )
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLocationLoading = false,
                        locationErrorMessage = error.message
                            ?: "운동 장소를 불러오지 못했어요."
                    )
                }
        }
    }

    fun validateWorkoutLocation(workoutEnvironmentId: Long) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLocationLoading = true,
                locationValidation = null,
                locationErrorMessage = null
            )

            workoutRepository.validateWorkoutLocation(workoutEnvironmentId)
                .onSuccess { validation ->
                    _uiState.value = _uiState.value.copy(
                        isLocationLoading = false,
                        locationValidation = validation
                    )
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLocationLoading = false,
                        locationErrorMessage = error.message
                            ?: "운동 장소를 확인하지 못했어요."
                    )
                }
        }
    }

    fun updateWorkoutLocation(workoutEnvironmentId: Long) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLocationSaving = true,
                locationErrorMessage = null
            )

            workoutRepository.updateWorkoutLocation(workoutEnvironmentId)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(
                        isLocationSaving = false,
                        isLocationUpdated = true,
                        locationValidation = null
                    )
                    loadTodayWorkout()
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLocationSaving = false,
                        locationErrorMessage = error.message
                            ?: "운동 장소 변경에 실패했어요."
                    )
                }
        }
    }

    fun loadExerciseCatalog() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isCatalogLoading = true,
                catalogErrorMessage = null
            )

            workoutRepository.getExerciseCatalog()
                .onSuccess { exercises ->
                    _uiState.value = _uiState.value.copy(
                        exerciseCatalog = exercises,
                        isCatalogLoading = false
                    )
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isCatalogLoading = false,
                        catalogErrorMessage = error.message
                            ?: "운동 목록을 불러오지 못했어요."
                    )
                }
        }
    }

    /**
     * 운동 목록 전체 저장
     *
     * Today API는 exerciseCode를 제공하지 않으므로
     * available-exercises API에서 exerciseId와 exerciseCode를 매칭한다.
     */
    fun saveWorkoutExercises(exercises: List<WorkoutExercise>) {
        val dailyRoutineId = _uiState.value.workout?.dailyRoutineId
            ?: run {
                _uiState.value = _uiState.value.copy(
                    exerciseSaveErrorMessage =
                        "오늘의 운동 정보를 찾을 수 없어요."
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
                // 서버에서 운동 ID와 운동 코드 매핑 조회
                val availableExercises = workoutRepository
                    .getAvailableExercises(dailyRoutineId)
                    .getOrThrow()

                val codeById = availableExercises.associate {
                    it.exerciseId to it.exerciseCode
                }

                // 운동 코드를 보완한 전체 운동 목록
                val exercisesWithCodes = exercises.map { exercise ->
                    val resolvedCode = exercise.exerciseCode
                        .takeIf { it.isNotBlank() }
                        ?: exercise.exerciseId?.let { codeById[it] }

                    if (resolvedCode.isNullOrBlank()) {
                        throw IllegalStateException(
                            "운동 코드를 찾을 수 없어요: " +
                                    (exercise.exerciseName ?: "알 수 없는 운동")
                        )
                    }

                    exercise.copy(exerciseCode = resolvedCode)
                }

                Log.d(
                    "FitMateWeight",
                    "중량 저장 운동 목록 = $exercisesWithCodes"
                )

                workoutRepository.updateDailyRoutineExercises(
                    dailyRoutineId = dailyRoutineId,
                    exercises = exercisesWithCodes
                ).getOrThrow()

                // 저장 후 오늘의 운동 다시 조회
                val updatedWorkout = workoutRepository
                    .getTodayWorkout()
                    .getOrThrow()

                _uiState.value = _uiState.value.copy(
                    isExerciseSaving = false,
                    isExerciseSaved = true,
                    workout = updatedWorkout
                )

            } catch (error: Exception) {
                Log.e(
                    "FitMateWeight",
                    "운동 설정 저장 실패",
                    error
                )

                _uiState.value = _uiState.value.copy(
                    isExerciseSaving = false,
                    isExerciseSaved = false,
                    exerciseSaveErrorMessage = error.message
                        ?: "운동 설정 저장에 실패했어요."
                )
            }
        }
    }

    /**
     * 중량 설정 화면에서 입력한 중량 저장
     *
     * 화면 ID는 전체 운동 목록의 index + 1과 일치한다.
     */
    fun saveWorkoutWeights(weights: Map<Int, Int>) {
        val workout = _uiState.value.workout ?: return

        val updatedExercises = workout.exercises.mapIndexed { index, exercise ->
            val newWeight = weights[index + 1]

            if (newWeight != null) {
                exercise.copy(weightKg = newWeight)
            } else {
                exercise
            }
        }

        saveWorkoutExercises(updatedExercises)
    }

    fun clearExerciseSaved() {
        _uiState.value = _uiState.value.copy(
            isExerciseSaved = false
        )
    }

    fun clearExerciseSaveError() {
        _uiState.value = _uiState.value.copy(
            exerciseSaveErrorMessage = null
        )
    }

    fun clearLocationError() {
        _uiState.value = _uiState.value.copy(
            locationErrorMessage = null
        )
    }

    fun clearLocationValidation() {
        _uiState.value = _uiState.value.copy(
            locationValidation = null
        )
    }

    fun clearLocationUpdated() {
        _uiState.value = _uiState.value.copy(
            isLocationUpdated = false
        )
    }
}
