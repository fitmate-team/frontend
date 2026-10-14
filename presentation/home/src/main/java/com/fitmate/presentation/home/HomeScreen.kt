package com.fitmate.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Watch
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.FitnessCenter
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fitmate.presentation.designsystem.R
import com.fitmate.presentation.designsystem.component.EmptyStateCard
import com.fitmate.presentation.designsystem.component.FitMateLogoTopBar
import com.fitmate.presentation.designsystem.component.FitMateOutlinedButton
import com.fitmate.presentation.designsystem.component.FitMatePrimaryButton
import com.fitmate.presentation.designsystem.component.SoftIconBox
import com.fitmate.presentation.designsystem.component.WorkoutStat
import com.fitmate.presentation.designsystem.component.WorkoutStatsRow
import com.fitmate.presentation.designsystem.theme.Blue100
import com.fitmate.presentation.designsystem.theme.Blue400
import com.fitmate.presentation.designsystem.theme.Blue500
import com.fitmate.presentation.designsystem.theme.BorderDefault
import com.fitmate.presentation.designsystem.theme.FitMateShadow
import com.fitmate.presentation.designsystem.theme.FitMateTheme
import com.fitmate.presentation.designsystem.theme.Gray300
import com.fitmate.presentation.designsystem.theme.Gray50
import com.fitmate.presentation.designsystem.theme.Spacing2
import com.fitmate.presentation.designsystem.theme.Spacing3
import com.fitmate.presentation.designsystem.theme.Spacing4
import com.fitmate.presentation.designsystem.theme.Spacing5
import com.fitmate.presentation.designsystem.theme.Spacing6
import com.fitmate.presentation.designsystem.theme.TextPrimary
import com.fitmate.presentation.designsystem.theme.TextSecondary
import com.fitmate.presentation.designsystem.theme.White
import com.fitmate.presentation.designsystem.theme.fitMateDisplayTextStyle
import com.fitmate.presentation.designsystem.theme.fitMateShadow
import com.fitmate.presentation.designsystem.theme.fitMateTextStyle

// 이번 주 운동 진행도. 루틴이 없으면 HomeUiState.weekly가 null
data class WeeklyProgress(
    val doneCount: Int,
    val goalCount: Int,
    val streakDays: Int
)

sealed interface TodayWorkout {
    data class Planned(
        val title: String,
        val dateLabel: String,
        val exerciseSummary: String,
        val durationMinutes: Int,
        val exerciseCount: Int,
        val restSeconds: Int,
        val completed: Boolean
    ) : TodayWorkout

    // 이번 주 루틴은 있지만 오늘은 쉬는 날
    data object RestDay : TodayWorkout

    // 이번 주 루틴이 없음
    data object None : TodayWorkout
}

data class HomeUiState(
    val weekly: WeeklyProgress?,
    val today: TodayWorkout
)

// 서버 연동 전까지 사용하는 예시 데이터
val SampleHomeUiState = HomeUiState(
    weekly = WeeklyProgress(doneCount = 4, goalCount = 5, streakDays = 12),
    today = TodayWorkout.Planned(
        title = "상체",
        dateLabel = "9월 3일 (수)",
        exerciseSummary = "벤치프레스, 숄더프레스, 체스트프레스 외 2종",
        durationMinutes = 55,
        exerciseCount = 5,
        restSeconds = 60,
        completed = false
    )
)

private val WorkoutCardShape = RoundedCornerShape(20.dp)
private val WatchCardShape = RoundedCornerShape(18.dp)
private val WatchCardShadow = FitMateShadow(offsetY = 3.dp, blur = 12.dp, alpha = 0.07f, color = Color(0xFF294766))
private val SectionLabelStyle = fitMateTextStyle(16.sp, 20.sp, FontWeight.Bold)

// 2.1 홈
@Composable
fun HomeScreen(
    state: HomeUiState = SampleHomeUiState,
    onStartWorkout: () -> Unit = {},
    onCompletedWorkoutClick: () -> Unit = {},
    onAddWorkout: () -> Unit = {},
    onNavigateToWatchSync: () -> Unit = {}
) {
    val hasPlannedWorkout = state.today is TodayWorkout.Planned

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
    ) {
        FitMateLogoTopBar()
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = Spacing6)
        ) {
            Spacer(modifier = Modifier.height(Spacing6))
            Text(
                text = "오늘의 운동을 시작해볼까요?",
                style = fitMateDisplayTextStyle(28.sp, 36.sp, letterSpacing = (-0.3).sp),
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(Spacing2))
            Text(
                text = "오늘도 한 걸음 이어가세요.",
                style = fitMateTextStyle(14.sp, 20.sp),
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(34.dp))
            Text(text = "이번 주 운동", style = SectionLabelStyle, color = TextPrimary)
            if (state.weekly != null) {
                Spacer(modifier = Modifier.height(Spacing4))
                WeeklyProgressCard(progress = state.weekly)
            } else {
                Spacer(modifier = Modifier.height(Spacing5))
                EmptyStateCard(
                    icon = ImageVector.vectorResource(R.drawable.ic_goal_exercise_habit),
                    iconSize = 48.dp,
                    containerColor = Gray50,
                    title = "이번 주 운동이 없어요.",
                    description = "지금 운동을 추가하고, 건강한 습관을 만들어보세요."
                )
            }

            // 운동 카드가 있을 때와 빈 상태일 때 섹션 간격이 다르다
            Spacer(modifier = Modifier.height(if (hasPlannedWorkout) Spacing5 else 34.dp))
            Text(text = "오늘의 운동", style = SectionLabelStyle, color = TextPrimary)
            Spacer(modifier = Modifier.height(if (hasPlannedWorkout) Spacing4 else 14.dp))
            when (val today = state.today) {
                is TodayWorkout.Planned -> {
                    TodayWorkoutCard(
                        workout = today,
                        onStartWorkout = onStartWorkout,
                        onCompletedWorkoutClick = onCompletedWorkoutClick
                    )
                    if (!today.completed) {
                        Spacer(modifier = Modifier.height(Spacing6))
                        WatchConnectCard(onClick = onNavigateToWatchSync)
                    }
                }

                TodayWorkout.RestDay -> EmptyStateCard(
                    icon = Icons.Rounded.FitnessCenter,
                    title = "오늘은 운동하는 날이 아니에요.",
                    description = "다음에 다시 만나요!"
                )

                TodayWorkout.None -> EmptyStateCard(
                    icon = Icons.Rounded.FitnessCenter,
                    title = "오늘의 운동이 없어요.",
                    description = "이번 주 운동을 추가하면,\n오늘의 운동이 여기에 표시돼요.",
                    action = { FitMatePrimaryButton(text = "운동 추가하기", onClick = onAddWorkout, height = 50.dp) }
                )
            }
            Spacer(modifier = Modifier.height(Spacing6))
        }
    }
}

@Composable
private fun WeeklyProgressCard(progress: WeeklyProgress) {
    val remaining = progress.goalCount - progress.doneCount
    val goalText = if (remaining <= 0) "목표 달성" else "목표까지 ${remaining}회"
    val ratio = (progress.doneCount.toFloat() / progress.goalCount).coerceIn(0f, 1f)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(WorkoutCardShape)
            .background(Gray50)
            .padding(Spacing4)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(Spacing2)
                .clip(RoundedCornerShape(2.dp))
                .background(Blue100)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(ratio)
                    .height(Spacing2)
                    .clip(RoundedCornerShape(2.dp))
                    .background(Blue400)
            )
        }
        Spacer(modifier = Modifier.height(Spacing3))
        Row {
            Text(
                text = "$goalText  / ${progress.streakDays}일 연속 운동 중",
                style = fitMateTextStyle(12.sp, 20.sp),
                color = TextSecondary,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "${progress.doneCount} / ${progress.goalCount} 회",
                style = fitMateTextStyle(12.sp, 20.sp),
                color = TextPrimary
            )
        }
    }
}

@Composable
private fun TodayWorkoutCard(
    workout: TodayWorkout.Planned,
    onStartWorkout: () -> Unit,
    onCompletedWorkoutClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(WorkoutCardShape)
            .background(White)
            .border(1.dp, BorderDefault, WorkoutCardShape)
    ) {
        Row(
            modifier = Modifier.padding(start = Spacing4, top = Spacing4, end = Spacing4),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SoftIconBox(icon = Icons.Rounded.FitnessCenter)
            Spacer(modifier = Modifier.width(Spacing3))
            Text(
                text = workout.title,
                style = fitMateTextStyle(20.sp, 20.sp, FontWeight.Bold),
                color = TextPrimary,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = workout.dateLabel,
                style = fitMateTextStyle(12.sp, 20.sp),
                color = TextSecondary,
                modifier = Modifier.align(Alignment.Top)
            )
        }
        Spacer(modifier = Modifier.height(Spacing3))
        Text(
            text = workout.exerciseSummary,
            style = fitMateTextStyle(14.sp, 20.sp),
            color = TextSecondary,
            modifier = Modifier.padding(horizontal = Spacing5)
        )
        Spacer(modifier = Modifier.height(Spacing5))
        WorkoutStatsRow(
            stats = listOf(
                WorkoutStat("${workout.durationMinutes}분", "예상 시간"),
                WorkoutStat("${workout.exerciseCount}종목", "운동 종목"),
                WorkoutStat("${workout.restSeconds}초", "평균 휴식")
            ),
            modifier = Modifier.padding(horizontal = Spacing3)
        )
        Spacer(modifier = Modifier.height(22.dp))
        Box(modifier = Modifier.padding(start = Spacing5, end = Spacing5, bottom = Spacing5)) {
            if (workout.completed) {
                FitMateOutlinedButton(
                    text = "운동 완료",
                    onClick = onCompletedWorkoutClick,
                    height = 50.dp,
                    borderColor = Blue500,
                    textStyle = fitMateDisplayTextStyle(16.sp, 24.sp, letterSpacing = 0.16.sp)
                )
            } else {
                FitMatePrimaryButton(text = "운동 시작", onClick = onStartWorkout, height = 50.dp)
            }
        }
    }
}

@Composable
private fun WatchConnectCard(onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .fitMateShadow(WatchCardShadow, 18.dp)
            .clip(WatchCardShape)
            .background(White)
            .border(1.dp, BorderDefault, WatchCardShape)
            .clickable(onClick = onClick)
            .padding(horizontal = 18.dp, vertical = Spacing4),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(White)
                .border(1.dp, BorderDefault, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.Watch,
                contentDescription = null,
                tint = Blue500,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Galaxy Watch 연동",
                style = fitMateTextStyle(14.5.sp, 17.sp, FontWeight.Medium, FontFamily.Default),
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "워치로 오늘 운동 시작하기",
                style = fitMateTextStyle(12.sp, 14.sp, fontFamily = FontFamily.Default),
                color = TextSecondary
            )
        }
        Spacer(modifier = Modifier.width(14.dp))
        Icon(
            imageVector = Icons.Rounded.ChevronRight,
            contentDescription = null,
            tint = Gray300,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Preview(name = "오늘의 운동 존재", widthDp = 394, heightDp = 812)
@Composable
private fun HomeScreenPlannedPreview() {
    FitMateTheme { HomeScreen() }
}

@Preview(name = "오늘의 운동 완료", widthDp = 394, heightDp = 812)
@Composable
private fun HomeScreenCompletedPreview() {
    FitMateTheme {
        HomeScreen(
            state = HomeUiState(
                weekly = WeeklyProgress(doneCount = 5, goalCount = 5, streakDays = 13),
                today = (SampleHomeUiState.today as TodayWorkout.Planned).copy(completed = true)
            )
        )
    }
}

@Preview(name = "오늘의 운동 없음", widthDp = 394, heightDp = 812)
@Composable
private fun HomeScreenRestDayPreview() {
    FitMateTheme {
        HomeScreen(
            state = HomeUiState(
                weekly = WeeklyProgress(doneCount = 5, goalCount = 5, streakDays = 13),
                today = TodayWorkout.RestDay
            )
        )
    }
}

@Preview(name = "이번 주 루틴 없음", widthDp = 394, heightDp = 812)
@Composable
private fun HomeScreenNoRoutinePreview() {
    FitMateTheme {
        HomeScreen(state = HomeUiState(weekly = null, today = TodayWorkout.None))
    }
}
