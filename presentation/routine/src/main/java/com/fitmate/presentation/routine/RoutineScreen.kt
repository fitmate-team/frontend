package com.fitmate.presentation.routine

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Bed
import androidx.compose.material.icons.rounded.Upload
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fitmate.presentation.designsystem.R
import com.fitmate.presentation.designsystem.component.FitMateCompactButton
import com.fitmate.presentation.designsystem.component.FitMateTabTopBar
import com.fitmate.presentation.designsystem.component.RoutineDayItem
import com.fitmate.presentation.designsystem.component.WorkoutStat
import com.fitmate.presentation.designsystem.component.WorkoutStatsRow
import com.fitmate.presentation.designsystem.component.WorkoutStatsStyle
import com.fitmate.presentation.designsystem.theme.FitMateTheme
import com.fitmate.presentation.designsystem.theme.Spacing3
import com.fitmate.presentation.designsystem.theme.Spacing6
import com.fitmate.presentation.designsystem.theme.TextPrimary
import com.fitmate.presentation.designsystem.theme.TextSecondary
import com.fitmate.presentation.designsystem.theme.White
import com.fitmate.presentation.designsystem.theme.fitMateTextStyle

// 요일별 루틴. title이 null이면 휴식일
data class RoutineDayPlan(
    val dayLabel: String,
    val title: String?,
    val summary: String? = null
)

data class WeeklyRoutine(
    val place: String,
    val averageTime: String,
    val difficulty: String,
    val days: List<RoutineDayPlan>
)

// 서버 연동 전까지 사용하는 예시 데이터
val SampleWeeklyRoutine = WeeklyRoutine(
    place = "헬스장",
    averageTime = "약 55분",
    difficulty = "초급",
    days = listOf(
        RoutineDayPlan("월", "상체", "5종목 · 약 55분"),
        RoutineDayPlan("화", "하체", "5종목 · 약 55분"),
        RoutineDayPlan("수", null),
        RoutineDayPlan("목", "코어", "5종목 · 약 55분"),
        RoutineDayPlan("금", null),
        RoutineDayPlan("토", "하체", "5종목 · 약 55분"),
        RoutineDayPlan("일", null)
    )
)

// 3.1 나의 루틴. routine이 null이면 생성된 루틴이 없는 상태
@Composable
fun RoutineScreen(
    routine: WeeklyRoutine? = SampleWeeklyRoutine,
    onCreateRoutine: () -> Unit = {},
    onOpenDay: (String) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
    ) {
        FitMateTabTopBar(
            title = "나의 루틴",
            action = if (routine != null) {
                {
                    FitMateCompactButton(
                        text = "루틴 재생성하기",
                        onClick = onCreateRoutine,
                        outlined = true,
                        leadingIcon = painterResource(R.drawable.ic_refresh)
                    )
                }
            } else {
                null
            }
        )
        if (routine == null) {
            EmptyRoutine(onCreateRoutine = onCreateRoutine, modifier = Modifier.weight(1f))
        } else {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = Spacing6)
            ) {
                Spacer(modifier = Modifier.height(Spacing6))
                WorkoutStatsRow(
                    stats = listOf(
                        WorkoutStat(routine.place, "운동 장소"),
                        WorkoutStat(routine.averageTime, "일별 평균 시간"),
                        WorkoutStat(routine.difficulty, "난이도")
                    ),
                    style = WorkoutStatsStyle.Boxed
                )
                Spacer(modifier = Modifier.height(19.dp))
                Text(
                    text = "이번 주 일정",
                    style = fitMateTextStyle(16.sp, 20.sp, FontWeight.Bold),
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(Spacing3))
                Column(verticalArrangement = Arrangement.spacedBy(Spacing3)) {
                    routine.days.forEach { day ->
                        RoutineDayItem(
                            day = day.dayLabel,
                            title = day.title ?: "휴식",
                            icon = if (day.title == null) Icons.Rounded.Bed else Icons.Rounded.Upload,
                            summary = day.summary,
                            onClick = { onOpenDay(day.dayLabel) }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(Spacing6))
            }
        }
    }
}

@Composable
private fun EmptyRoutine(onCreateRoutine: () -> Unit, modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "생성된 루틴이 없어요.\n루틴을 생성하러 가볼까요?",
                style = fitMateTextStyle(12.sp, 18.sp),
                color = TextSecondary,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(Spacing3))
            FitMateCompactButton(text = "+ 루틴 생성하기", onClick = onCreateRoutine)
        }
    }
}

@Preview(name = "루틴 있음", widthDp = 394, heightDp = 812)
@Composable
private fun RoutineScreenPreview() {
    FitMateTheme { RoutineScreen() }
}

@Preview(name = "루틴 없음", widthDp = 394, heightDp = 812)
@Composable
private fun RoutineScreenEmptyPreview() {
    FitMateTheme { RoutineScreen(routine = null) }
}
