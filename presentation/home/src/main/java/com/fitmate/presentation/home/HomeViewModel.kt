package com.fitmate.presentation.home

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitmate.domain.model.TodayRoutine
import com.fitmate.domain.model.WeeklyRoutine
import com.fitmate.domain.repository.RoutineRepository
import com.fitmate.domain.repository.WorkoutRecordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

private val WeekDayLabels = listOf("월", "화", "수", "목", "금", "토", "일")

// 2.1 홈. 이번 주 루틴 / 오늘의 운동 / 이번 달까지의 운동 날짜로 화면 상태를 만든다
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val routineRepository: RoutineRepository,
    private val workoutRecordRepository: WorkoutRecordRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState(weekly = null, today = TodayWorkout.None, isLoading = true))
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _messages = Channel<String>(Channel.BUFFERED)
    val messages = _messages.receiveAsFlow()

    // 홈에 들어올 때마다 다시 불러온다 (운동을 마치고 돌아온 경우)
    fun refresh() {
        viewModelScope.launch {
            val today = LocalDate.now()
            val weeklyResult = async { routineRepository.getWeeklyRoutine() }
            val todayResult = async { routineRepository.getTodayRoutine() }
            // 이번 주가 지난달에 걸치거나 연속 운동일이 지난달부터 이어질 수 있어서 두 달을 본다
            val workoutDates = async {
                listOf(today.minusMonths(1), today).map { month ->
                    async { workoutRecordRepository.getWorkoutDates(month.year, month.monthValue).getOrNull().orEmpty() }
                }.awaitAll().flatten().toSet()
            }

            val weekly = weeklyResult.await().getOrElse { return@launch fail(it) }
            val todayRoutine = todayResult.await().getOrElse { return@launch fail(it) }

            _uiState.value = if (weekly == null) {
                HomeUiState(weekly = null, today = TodayWorkout.None)
            } else {
                HomeUiState(
                    weekly = weekly.progress(workoutDates.await(), today),
                    today = todayRoutine?.toPlanned() ?: TodayWorkout.RestDay
                )
            }
        }
    }

    private suspend fun fail(error: Throwable) {
        _uiState.update { it.copy(isLoading = false) }
        _messages.send(error.message ?: "홈 정보를 불러오지 못했어요.")
    }
}

@Composable
fun HomeMessageEffect(viewModel: HomeViewModel) {
    val context = LocalContext.current
    LaunchedEffect(viewModel) {
        viewModel.messages.collect { Toast.makeText(context, it, Toast.LENGTH_SHORT).show() }
    }
}

private fun WeeklyRoutine.progress(workoutDates: Set<LocalDate>, today: LocalDate): WeeklyProgress {
    val weekEnd = weekStartDate.plusDays(6)
    return WeeklyProgress(
        doneCount = workoutDates.count { it in weekStartDate..weekEnd },
        goalCount = days.size,
        streakDays = streakDays(workoutDates, today)
    )
}

// 오늘(오늘 아직 안 했으면 어제)부터 거꾸로 하루도 빠짐없이 운동한 날 수
private fun streakDays(workoutDates: Set<LocalDate>, today: LocalDate): Int {
    var day = if (today in workoutDates) today else today.minusDays(1)
    var count = 0
    while (day in workoutDates) {
        count++
        day = day.minusDays(1)
    }
    return count
}

private fun TodayRoutine.toPlanned() = TodayWorkout.Planned(
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
    completed = hasWorkedOut
)
