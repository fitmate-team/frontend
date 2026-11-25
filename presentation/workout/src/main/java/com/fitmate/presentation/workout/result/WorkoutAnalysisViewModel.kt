package com.fitmate.presentation.workout.result

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitmate.domain.model.ApplyWorkoutAdjustments
import com.fitmate.domain.model.WorkoutAdjustmentRecommendation
import com.fitmate.domain.model.WorkoutRecordDetail
import com.fitmate.domain.repository.WorkoutRecordRepository
import com.fitmate.domain.repository.WorkoutRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class WorkoutAnalysisUiState(

    // 운동 분석 결과
    val isAnalysisLoading: Boolean = false,
    val analysis: WorkoutRecordDetail? = null,
    val analysisErrorMessage: String? = null,

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
class WorkoutAnalysisViewModel @Inject constructor(
    private val workoutRecordRepository: WorkoutRecordRepository,
    private val workoutRepository: WorkoutRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(WorkoutAnalysisUiState())

    val uiState: StateFlow<WorkoutAnalysisUiState> =
        _uiState.asStateFlow()

    /**
     * 운동 기록 상세 및 분석 결과 조회
     */
    fun loadWorkoutAnalysis(workoutRecordId: Long) {

        if (_uiState.value.isAnalysisLoading) return

        // 동일한 운동 기록 중복 조회 방지
        if (
            _uiState.value.analysis?.workoutRecordId ==
            workoutRecordId
        ) return

        _uiState.value = WorkoutAnalysisUiState(
            isAnalysisLoading = true
        )

        viewModelScope.launch {

            workoutRecordRepository
                .getWorkoutRecordDetail(workoutRecordId)
                .onSuccess { analysis ->

                    Log.d(
                        "FitMateAnalysis",
                        "운동 분석 조회 성공: recordId=$workoutRecordId"
                    )

                    _uiState.value = _uiState.value.copy(
                        isAnalysisLoading = false,
                        analysis = analysis,
                        analysisErrorMessage = null
                    )
                }
                .onFailure { error ->

                    Log.e(
                        "FitMateAnalysis",
                        "운동 분석 조회 실패",
                        error
                    )

                    _uiState.value = _uiState.value.copy(
                        isAnalysisLoading = false,
                        analysisErrorMessage = error.message
                            ?: "운동 분석 결과를 불러오지 못했어요."
                    )
                }
        }
    }

    /**
     * 운동 종료 후 AI 운동 강도 추천 조회
     */
    fun loadWorkoutRecommendations(workoutRecordId: Long) {

        val state = _uiState.value

        if (state.isRecommendationLoading) return

        // 분석 결과가 조회된 운동만 추천 요청
        if (state.analysis?.workoutRecordId != workoutRecordId) {
            return
        }

        // 다음 운동 루틴이 없는 경우 추천 요청 방지
        if (!state.analysis.hasNextRoutine) return

        // 동일한 운동 기록 중복 추천 방지
        if (state.recommendation?.workoutRecordId == workoutRecordId) {
            return
        }

        _uiState.value = _uiState.value.copy(
            isRecommendationLoading = true,
            recommendationErrorMessage = null
        )

        viewModelScope.launch {

            workoutRepository
                .recommendAdjustments(workoutRecordId)
                .onSuccess { recommendation ->

                    _uiState.value = _uiState.value.copy(
                        isRecommendationLoading = false,
                        recommendation = recommendation,
                        recommendationErrorMessage = null
                    )
                }
                .onFailure { error ->

                    Log.e(
                        "FitMateAnalysis",
                        "AI 운동 강도 추천 실패",
                        error
                    )

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

        val state = _uiState.value

        if (
            state.isApplyingAdjustments ||
            state.isAdjustmentsApplied
        ) return

        if (
            state.recommendation?.workoutRecordId !=
            workoutRecordId
        ) return

        _uiState.value = _uiState.value.copy(
            isApplyingAdjustments = true,
            applyErrorMessage = null
        )

        viewModelScope.launch {

            workoutRepository
                .applyAdjustments(
                    workoutRecordId = workoutRecordId,
                    request = request
                )
                .onSuccess {

                    _uiState.value = _uiState.value.copy(
                        isApplyingAdjustments = false,
                        isAdjustmentsApplied = true,
                        applyErrorMessage = null
                    )
                }
                .onFailure { error ->

                    Log.e(
                        "FitMateAnalysis",
                        "AI 추천 적용 실패",
                        error
                    )

                    _uiState.value = _uiState.value.copy(
                        isApplyingAdjustments = false,
                        applyErrorMessage = error.message
                            ?: "운동 강도 변경사항 저장에 실패했어요."
                    )
                }
        }
    }

    /**
     * 운동 분석 조회 오류 초기화
     */
    fun clearAnalysisError() {
        _uiState.value = _uiState.value.copy(
            analysisErrorMessage = null
        )
    }

    /**
     * AI 추천 조회 오류 초기화
     */
    fun clearRecommendationError() {
        _uiState.value = _uiState.value.copy(
            recommendationErrorMessage = null
        )
    }

    /**
     * AI 추천 적용 오류 초기화
     */
    fun clearApplyError() {
        _uiState.value = _uiState.value.copy(
            applyErrorMessage = null
        )
    }
}