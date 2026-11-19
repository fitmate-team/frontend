
package com.fitmate.presentation.workout.session

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitmate.domain.model.ExerciseGuide
import com.fitmate.domain.model.FinishWorkout
import com.fitmate.domain.model.WorkoutAdjustmentRecommendation
import com.fitmate.domain.model.ApplyWorkoutAdjustments
import com.fitmate.domain.repository.WorkoutRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class WorkoutSessionUiState(
    // 운동 가이드
    val isGuideLoading: Boolean = false,
    val exerciseGuides: Map<String, ExerciseGuide> = emptyMap(),
    val exerciseGuidesById: Map<Long, ExerciseGuide> = emptyMap(),
    val guideErrorMessage: String? = null,

    // 운동 종료
    val isFinishing: Boolean = false,
    val isFinished: Boolean = false,
    val errorMessage: String? = null,

    // AI 운동 강도 추천
    val isRecommendationLoading: Boolean = false,
    val recommendation: WorkoutAdjustmentRecommendation? = null,
    val recommendationErrorMessage: String? = null,

    // AI 추천 적용
    val isApplyingAdjustments: Boolean = false,
    val isAdjustmentsApplied: Boolean = false,
    val applyErrorMessage: String? = null
)

@HiltViewModel
class WorkoutSessionViewModel @Inject constructor(
    private val workoutRepository: WorkoutRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(WorkoutSessionUiState())

    val uiState: StateFlow<WorkoutSessionUiState> =
        _uiState.asStateFlow()

    // 마지막으로 조회한 루틴 ID
    private var loadedDailyRoutineId: Long? = null

    /**
     * 현재 루틴에 포함된 운동의 가이드를 조회한다.
     *
     * 1. dailyRoutineId로 운동 목록 조회
     * 2. exerciseCode와 exerciseId 매칭
     * 3. 운동 ID로 가이드 조회
     * 4. exerciseCode를 키로 가이드 저장
     */
    fun loadExerciseGuides(
        dailyRoutineId: Long,
        exerciseCodes: List<String>
    ) {
        if (_uiState.value.isGuideLoading) return

        // 동일한 루틴을 이미 조회했다면 중복 호출 방지
        if (
            loadedDailyRoutineId == dailyRoutineId &&
            exerciseCodes.all {
                it in _uiState.value.exerciseGuides
            }
        ) {
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isGuideLoading = true,
                guideErrorMessage = null
            )

            try {
                // 1. 루틴에서 사용 가능한 운동 목록 조회
                val availableExercises = workoutRepository
                    .getAvailableExercises(dailyRoutineId)
                    .getOrThrow()

                // 2. 현재 운동에 해당하는 운동 ID 추출
                val selectedExercises = availableExercises
                    .filter { it.exerciseCode in exerciseCodes }

                val exerciseIds = selectedExercises
                    .map { it.exerciseId }
                    .distinct()

                Log.d(
                    "FitMateGuide",
                    "요청 exerciseCodes = $exerciseCodes"
                )
                Log.d(
                    "FitMateGuide",
                    "매칭된 운동 = $selectedExercises"
                )
                Log.d(
                    "FitMateGuide",
                    "조회할 exerciseIds = $exerciseIds"
                )

                if (exerciseIds.isEmpty()) {
                    _uiState.value = _uiState.value.copy(
                        isGuideLoading = false,
                        exerciseGuides = emptyMap(),
                        guideErrorMessage = "조회할 운동 가이드가 없어요."
                    )
                    return@launch
                }

                // 3. 운동 가이드 조회
                val guides = workoutRepository
                    .getExerciseGuides(exerciseIds)
                    .getOrThrow()

                Log.d(
                    "FitMateGuide",
                    "가이드 응답 = $guides"
                )

                // 4. exerciseId를 exerciseCode에 매칭
                val guideById = guides.associateBy {
                    it.exerciseId
                }

                val guideByCode = selectedExercises
                    .mapNotNull { exercise ->
                        val guide = guideById[exercise.exerciseId]

                        if (guide != null) {
                            exercise.exerciseCode to guide
                        } else {
                            null
                        }
                    }
                    .toMap()

                loadedDailyRoutineId = dailyRoutineId

                _uiState.value = _uiState.value.copy(
                    isGuideLoading = false,
                    exerciseGuides = guideByCode,
                    exerciseGuidesById =
                        _uiState.value.exerciseGuidesById + guideById,
                    guideErrorMessage = null
                )

            } catch (error: Exception) {
                Log.e(
                    "FitMateGuide",
                    "운동 코드 기반 가이드 조회 실패",
                    error
                )

                _uiState.value = _uiState.value.copy(
                    isGuideLoading = false,
                    guideErrorMessage = error.message
                        ?: "운동 가이드를 불러오지 못했어요."
                )
            }
        }
    }

    /**
     * exerciseId를 이용해 운동 가이드를 직접 조회한다.
     * 중량 설정 화면에서 사용한다.
     */
    fun loadExerciseGuidesByIds(exerciseIds: List<Long>) {
        val ids = exerciseIds.distinct()

        if (ids.isEmpty()) return
        if (_uiState.value.isGuideLoading) return

        // 이미 필요한 가이드를 모두 조회했다면 중복 호출 방지
        if (
            ids.all {
                it in _uiState.value.exerciseGuidesById
            }
        ) {
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isGuideLoading = true,
                guideErrorMessage = null
            )

            try {
                Log.d(
                    "FitMateGuide",
                    "ID 기반 가이드 요청 = $ids"
                )

                val guides = workoutRepository
                    .getExerciseGuides(ids)
                    .getOrThrow()

                Log.d(
                    "FitMateGuide",
                    "ID 기반 가이드 응답 = $guides"
                )

                val guideById = guides.associateBy {
                    it.exerciseId
                }

                _uiState.value = _uiState.value.copy(
                    isGuideLoading = false,
                    exerciseGuidesById =
                        _uiState.value.exerciseGuidesById + guideById,
                    guideErrorMessage = null
                )

            } catch (error: Exception) {
                Log.e(
                    "FitMateGuide",
                    "ID 기반 가이드 조회 실패",
                    error
                )

                _uiState.value = _uiState.value.copy(
                    isGuideLoading = false,
                    guideErrorMessage = error.message
                        ?: "운동 가이드를 불러오지 못했어요."
                )
            }
        }
    }

    /**
     * 운동 기록 저장 및 종료
     */
    fun finishWorkout(request: FinishWorkout) {
        if (
            _uiState.value.isFinishing ||
            _uiState.value.isFinished
        ) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isFinishing = true,
                errorMessage = null
            )

            workoutRepository.finishWorkout(request)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(
                        isFinishing = false,
                        isFinished = true
                    )
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isFinishing = false,
                        errorMessage = error.message
                            ?: "운동 기록 저장에 실패했어요."
                    )
                }
        }
    }

    /**
     * 운동 종료 후 AI 운동 강도 추천 조회
     */
    fun loadWorkoutRecommendations(workoutRecordId: Long) {
        if (_uiState.value.isRecommendationLoading) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isRecommendationLoading = true,
                recommendationErrorMessage = null
            )

            workoutRepository
                .recommendAdjustments(workoutRecordId)
                .onSuccess { recommendation ->
                    _uiState.value = _uiState.value.copy(
                        isRecommendationLoading = false,
                        recommendation = recommendation
                    )
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isRecommendationLoading = false,
                        recommendationErrorMessage = error.message
                            ?: "운동 강도 추천을 불러오지 못했어요."
                    )
                }
        }
    }

    /**
     * 사용자가 선택한 AI 추천 변경사항 저장
     */
    fun applyWorkoutRecommendations(
        workoutRecordId: Long,
        request: ApplyWorkoutAdjustments
    ) {
        if (
            _uiState.value.isApplyingAdjustments ||
            _uiState.value.isAdjustmentsApplied
        ) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isApplyingAdjustments = true,
                applyErrorMessage = null
            )

            workoutRepository
                .applyAdjustments(
                    workoutRecordId = workoutRecordId,
                    request = request
                )
                .onSuccess {
                    _uiState.value = _uiState.value.copy(
                        isApplyingAdjustments = false,
                        isAdjustmentsApplied = true
                    )
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isApplyingAdjustments = false,
                        applyErrorMessage = error.message
                            ?: "운동 강도 변경사항 저장에 실패했어요."
                    )
                }
        }
    }

    /**
     * AI 추천 관련 오류 초기화
     */
    fun clearRecommendationError() {
        _uiState.value = _uiState.value.copy(
            recommendationErrorMessage = null
        )
    }

    fun clearApplyError() {
        _uiState.value = _uiState.value.copy(
            applyErrorMessage = null
        )
    }

    /**
     * 운동 종료 오류 초기화
     */
    fun clearError() {
        _uiState.value = _uiState.value.copy(
            errorMessage = null
        )
    }

    /**
     * 운동 가이드 조회 오류 초기화
     */
    fun clearGuideError() {
        _uiState.value = _uiState.value.copy(
            guideErrorMessage = null
        )
    }
}
