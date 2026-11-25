package com.fitmate.presentation.record

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitmate.domain.model.ExerciseGrowth
import com.fitmate.domain.model.GrowthAnalysis
import com.fitmate.domain.model.MonthlyWorkoutRecord
import com.fitmate.domain.repository.WorkoutRecordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.YearMonth
import java.time.ZoneId
import javax.inject.Inject

// 기록 화면 UI 상태
data class RecordUiState(
    val selectedMonth: YearMonth =
        YearMonth.now(ZoneId.of("Asia/Seoul")),

    // 월별 운동 기록
    val monthlyRecord: MonthlyWorkoutRecord? = null,
    val isMonthlyLoading: Boolean = false,
    val monthlyErrorMessage: String? = null,

    // 성장 분석
    val growthAnalysis: GrowthAnalysis? = null,
    val isGrowthLoading: Boolean = false,
    val growthErrorMessage: String? = null,

    // 운동별 중량 변화
    val exerciseGrowth: ExerciseGrowth? = null,
    val isExerciseGrowthLoading: Boolean = false,
    val exerciseGrowthErrorMessage: String? = null
)

@HiltViewModel
class RecordViewModel @Inject constructor(
    private val workoutRecordRepository: WorkoutRecordRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RecordUiState())
    val uiState: StateFlow<RecordUiState> = _uiState.asStateFlow()

    private var monthlyJob: Job? = null
    private var growthJob: Job? = null
    private var exerciseGrowthJob: Job? = null

    // ==================== 월별 운동 기록 ====================

    fun loadMonthlyRecords(
        month: YearMonth = _uiState.value.selectedMonth
    ) {
        monthlyJob?.cancel()

        _uiState.update {
            it.copy(
                selectedMonth = month,
                monthlyRecord = null,
                isMonthlyLoading = true,
                monthlyErrorMessage = null
            )
        }

        monthlyJob = viewModelScope.launch {
            val result = workoutRecordRepository
                .getMonthlyWorkoutRecords(
                    year = month.year,
                    month = month.monthValue
                )

            if (_uiState.value.selectedMonth != month) {
                return@launch
            }

            result.onSuccess { record ->
                _uiState.update {
                    it.copy(
                        monthlyRecord = record,
                        isMonthlyLoading = false
                    )
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isMonthlyLoading = false,
                        monthlyErrorMessage = error.message
                            ?: "운동 기록을 불러오지 못했어요."
                    )
                }
            }
        }
    }

    // 이전 달 조회
    fun selectPreviousMonth() {
        loadMonthlyRecords(
            _uiState.value.selectedMonth.minusMonths(1)
        )
    }

    // 다음 달 조회
    fun selectNextMonth() {
        val nextMonth =
            _uiState.value.selectedMonth.plusMonths(1)

        val currentMonth =
            YearMonth.now(ZoneId.of("Asia/Seoul"))

        if (nextMonth.isAfter(currentMonth)) return

        loadMonthlyRecords(nextMonth)
    }

    // ==================== 성장 분석 ====================

    fun loadGrowthAnalysis(forceRefresh: Boolean = false) {
        val state = _uiState.value

        if (state.isGrowthLoading) return
        if (!forceRefresh && state.growthAnalysis != null) return

        growthJob?.cancel()
        exerciseGrowthJob?.cancel()

        _uiState.update {
            it.copy(
                isGrowthLoading = true,
                growthErrorMessage = null,
                isExerciseGrowthLoading = false,
                exerciseGrowthErrorMessage = null
            )
        }

        growthJob = viewModelScope.launch {
            workoutRecordRepository
                .getGrowthAnalysis()
                .onSuccess { analysis ->
                    _uiState.update {
                        it.copy(
                            growthAnalysis = analysis,
                            exerciseGrowth =
                                analysis.workoutGrowth.selectedExercise,
                            isGrowthLoading = false
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isGrowthLoading = false,
                            growthErrorMessage = error.message
                                ?: "성장 분석을 불러오지 못했어요."
                        )
                    }
                }
        }
    }

    // ==================== 운동별 중량 변화 ====================

    fun loadExerciseGrowth(exerciseId: Long) {
        val state = _uiState.value

        if (state.isGrowthLoading) return

        if (
            !state.isExerciseGrowthLoading &&
            state.exerciseGrowth?.exerciseId == exerciseId &&
            state.exerciseGrowthErrorMessage == null
        ) return

        exerciseGrowthJob?.cancel()

        _uiState.update {
            it.copy(
                isExerciseGrowthLoading = true,
                exerciseGrowthErrorMessage = null
            )
        }

        exerciseGrowthJob = viewModelScope.launch {
            workoutRecordRepository
                .getExerciseGrowth(exerciseId)
                .onSuccess { growth ->
                    _uiState.update {
                        it.copy(
                            exerciseGrowth = growth,
                            isExerciseGrowthLoading = false
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isExerciseGrowthLoading = false,
                            exerciseGrowthErrorMessage = error.message
                                ?: "운동 성장 기록을 불러오지 못했어요."
                        )
                    }
                }
        }
    }
}
