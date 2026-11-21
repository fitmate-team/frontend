package com.fitmate.presentation.home

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitmate.domain.model.HomeSummary
import com.fitmate.domain.model.HomeTodayWorkout
import com.fitmate.domain.repository.RoutineRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private val WeekDayLabels = listOf("월", "화", "수", "목", "금", "토", "일")

// 2.1 홈. 홈 조회 API 하나로 이번 주 진행도 / 연속 운동일 / 오늘의 운동을 만든다
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val routineRepository: RoutineRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState(weekly = null, today = TodayWorkout.None, isLoading = true))
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _messages = Channel<String>(Channel.BUFFERED)
    val messages = _messages.receiveAsFlow()

    // 홈에 들어올 때마다 다시 불러온다 (운동을 마치고 돌아온 경우)
    fun refresh() {
        viewModelScope.launch {
            routineRepository.getHomeSummary()
                .onSuccess { _uiState.value = it.toUiState() }
                .onFailure { error ->
                    _uiState.update { it.copy(isLoading = false) }
                    _messages.send(error.message ?: "홈 정보를 불러오지 못했어요.")
                }
        }
    }
}

@Composable
fun HomeMessageEffect(viewModel: HomeViewModel) {
    val context = LocalContext.current
    LaunchedEffect(viewModel) {
        viewModel.messages.collect { Toast.makeText(context, it, Toast.LENGTH_SHORT).show() }
    }
}

private fun HomeSummary.toUiState(): HomeUiState {
    if (!hasWeeklyRoutine) return HomeUiState(weekly = null, today = TodayWorkout.None)
    return HomeUiState(
        weekly = WeeklyProgress(
            doneCount = completedWorkoutCount,
            goalCount = targetWorkoutCount,
            streakDays = consecutiveWorkoutDays
        ),
        today = today?.toPlanned() ?: TodayWorkout.RestDay
    )
}

private fun HomeTodayWorkout.toPlanned() = TodayWorkout.Planned(
    title = title,
    dateLabel = "${date.monthValue}월 ${date.dayOfMonth}일 (${WeekDayLabels[date.dayOfWeek.ordinal]})",
    exerciseSummary = if (exerciseNames.size <= 3) {
        exerciseNames.joinToString(", ")
    } else {
        "${exerciseNames.take(3).joinToString(", ")} 외 ${exerciseNames.size - 3}종"
    },
    durationMinutes = estimatedMinutes,
    exerciseCount = exerciseNames.size,
    restSeconds = averageRestSeconds,
    completed = completed
)
