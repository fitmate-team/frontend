package com.fitmate.presentation.routine

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitmate.domain.model.BaselineRecordType
import com.fitmate.domain.model.ExerciseLocation
import com.fitmate.domain.model.WorkoutDay
import com.fitmate.domain.model.WorkoutEnvironment
import com.fitmate.domain.repository.CatalogRepository
import com.fitmate.domain.repository.MemberRepository
import com.fitmate.domain.repository.RoutineRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject
import kotlin.math.roundToInt
import com.fitmate.domain.model.WeeklyRoutine as SavedWeeklyRoutine

data class RoutineUiState(
    val isLoading: Boolean = true,
    val routine: WeeklyRoutine? = null,
    // 요일 문구("월")별 상세. 쉬는 날은 없다
    val dayDetails: Map<String, RoutineDayDetailUi> = emptyMap(),
    val todayLabel: String = WorkoutDay.entries[LocalDate.now().dayOfWeek.ordinal].label(),
    // 오늘의 운동 상단 장소 칩 (헬스장 이름 / 집 / 야외)
    val placeLabel: String = ""
)

sealed interface RoutineEvent {
    data class Message(val text: String) : RoutineEvent
}

// 3.1 나의 루틴 / 3.2 요일별 운동 상세
@HiltViewModel
class RoutineViewModel @Inject constructor(
    private val routineRepository: RoutineRepository,
    private val memberRepository: MemberRepository,
    private val catalogRepository: CatalogRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RoutineUiState())
    val uiState: StateFlow<RoutineUiState> = _uiState.asStateFlow()

    private val _events = Channel<RoutineEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    // 탭에 들어올 때마다 다시 불러온다 (루틴을 새로 만들고 돌아온 경우)
    fun refresh() {
        viewModelScope.launch {
            // 내 정보 / 운동 환경 / 운동 목록은 보조 정보라 실패해도 루틴은 보여준다
            val member = async { memberRepository.getMyInfo().getOrNull() }
            val environments = async { memberRepository.getWorkoutEnvironments().getOrNull() }
            val exercises = async { catalogRepository.getExercises().getOrNull().orEmpty() }

            routineRepository.getWeeklyRoutine()
                .onSuccess { routine ->
                    val environment = environments.await()?.environments?.firstOrNull { it.id == routine?.environmentId }
                    val recordTypes = exercises.await().associate { it.code to it.recordType }
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            routine = routine?.toUi(environment, member.await()?.exerciseLevel?.label()),
                            dayDetails = routine?.dayDetails(recordTypes).orEmpty(),
                            placeLabel = environment?.placeLabel().orEmpty()
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update { it.copy(isLoading = false) }
                    _events.send(RoutineEvent.Message(error.message ?: "루틴을 불러오지 못했어요."))
                }
        }
    }
}

// 헬스장이면 헬스장 이름, 아니면 장소 이름
internal fun WorkoutEnvironment.placeLabel(): String =
    if (location == ExerciseLocation.GYM) gymName ?: location.label() else location.label()

private fun SavedWeeklyRoutine.toUi(environment: WorkoutEnvironment?, difficulty: String?): WeeklyRoutine {
    val plans = days.associateBy { it.day }
    return WeeklyRoutine(
        place = environment?.location?.label() ?: "-",
        averageTime = days.map { it.estimatedMinutes }.takeIf { it.isNotEmpty() }
            ?.let { "약 ${it.average().roundToInt()}분" } ?: "-",
        difficulty = difficulty ?: "-",
        days = WorkoutDay.entries.map { day ->
            val plan = plans[day]
            RoutineDayPlan(
                dayLabel = day.label(),
                title = plan?.mainTarget?.label(),
                summary = plan?.let { "${it.exercises.size}종목 · 약 ${it.estimatedMinutes}분" }
            )
        }
    )
}

private fun SavedWeeklyRoutine.dayDetails(recordTypes: Map<String, BaselineRecordType>): Map<String, RoutineDayDetailUi> =
    days.associate { it.day.label() to it.toDetailUi(recordTypes) }
