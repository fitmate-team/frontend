package com.fitmate.presentation.routine

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.DirectionsRun
import androidx.compose.material.icons.rounded.FitnessCenter
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fitmate.presentation.designsystem.component.ExerciseListItem
import com.fitmate.presentation.designsystem.component.FitMatePrimaryButton
import com.fitmate.presentation.designsystem.component.FitMateTitleTopBar
import com.fitmate.presentation.designsystem.component.SoftIconBox
import com.fitmate.presentation.designsystem.component.WorkoutStat
import com.fitmate.presentation.designsystem.component.WorkoutStatsRow
import com.fitmate.presentation.designsystem.theme.BorderDefault
import com.fitmate.presentation.designsystem.theme.FitMateTheme
import com.fitmate.presentation.designsystem.theme.ShapeFull
import com.fitmate.presentation.designsystem.theme.Spacing1
import com.fitmate.presentation.designsystem.theme.Spacing2
import com.fitmate.presentation.designsystem.theme.Spacing3
import com.fitmate.presentation.designsystem.theme.Spacing4
import com.fitmate.presentation.designsystem.theme.Spacing6
import com.fitmate.presentation.designsystem.theme.TextPrimary
import com.fitmate.presentation.designsystem.theme.TextSecondary
import com.fitmate.presentation.designsystem.theme.White
import com.fitmate.presentation.designsystem.theme.fitMateTextStyle

// 운동 목록 한 줄. detail은 "40kg / 8회 × 4세트" / "15분" 같은 처방 문구
data class PlannedExercise(
    val name: String,
    val detail: String,
    val restTime: String?,
    val needWeight: Boolean = false,
    val isCardio: Boolean = false
) {
    val icon: ImageVector get() = if (isCardio) Icons.AutoMirrored.Rounded.DirectionsRun else Icons.Rounded.FitnessCenter
}

// 요일 루틴 상세에 보여줄 내용. estimatedMinutes가 null이면 예상 시간 칸을 빼고 보여준다
data class RoutineDayDetailUi(
    val title: String,
    val dateLabel: String,
    val estimatedMinutes: Int?,
    val averageRestSeconds: Int,
    val exercises: List<PlannedExercise>
)

// 미리보기용 예시 데이터
private val SampleDayDetail = RoutineDayDetailUi(
    title = "상체",
    dateLabel = "9월 21일 (월)",
    estimatedMinutes = 55,
    averageRestSeconds = 60,
    exercises = listOf(
        PlannedExercise("벤치프레스", "8회 × 4세트", "휴식 90초", needWeight = true),
        PlannedExercise("숄더프레스", "40kg / 8회 × 4세트", "휴식 90초"),
        PlannedExercise("체스트프레스", "8회 × 4세트", "휴식 90초", needWeight = true),
        PlannedExercise("레터럴레이즈", "20kg / 8회 × 4세트", "휴식 90초"),
        PlannedExercise("러닝머신", "15분", null, isCardio = true)
    )
)

// 3.2 오늘의 운동. 오늘이면 운동 장소 / 중량 설정 필요 배지 / 시작하기 버튼을 보여준다
@Composable
fun WorkoutDetailScreen(
    detail: RoutineDayDetailUi = SampleDayDetail,
    isToday: Boolean = true,
    placeLabel: String = "Fit Gym",
    onBack: () -> Unit = {},
    onChangePlace: () -> Unit = {},
    onStartWorkout: () -> Unit = {}
) {
    RoutineDayDetail(
        topBarTitle = "오늘의 운동",
        detail = detail,
        showWeightBadges = isToday,
        onBack = onBack,
        topBarAction = if (isToday) {
            { PlaceChip(place = placeLabel, onClick = onChangePlace) }
        } else {
            null
        },
        startButton = if (isToday) onStartWorkout else null
    )
}

// 3.6 이번 주 추천 루틴 - 일자별 미리보기
@Composable
fun RoutineDayPreviewScreen(
    detail: RoutineDayDetailUi = SampleDayDetail,
    onBack: () -> Unit = {}
) {
    RoutineDayDetail(
        topBarTitle = "미리보기",
        detail = detail,
        showWeightBadges = true,
        onBack = onBack
    )
}

// 3.5 운동 상세 조회 - 직접 만든 요일 루틴. 예상 시간 없이 종목 수 / 평균 휴식만 보여준다
@Composable
fun ManualDayDetailScreen(
    dayLabel: String = "화",
    exercises: List<ExerciseSetting> = emptyList(),
    onBack: () -> Unit = {}
) {
    val detail = if (exercises.isEmpty()) {
        SampleDayDetail.copy(estimatedMinutes = null)
    } else {
        manualDayDetailOf(dayLabel, exercises)
    }
    RoutineDayDetail(
        topBarTitle = "오늘의 운동",
        detail = detail,
        showWeightBadges = false,
        onBack = onBack
    )
}

// 요일 루틴 상세 공통 틀: 상단 바 + 루틴 이름 / 날짜 + 요약 + 운동 목록 (+ 시작하기)
@Composable
private fun RoutineDayDetail(
    topBarTitle: String,
    detail: RoutineDayDetailUi,
    showWeightBadges: Boolean,
    onBack: () -> Unit,
    topBarAction: (@Composable () -> Unit)? = null,
    startButton: (() -> Unit)? = null
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
    ) {
        FitMateTitleTopBar(title = topBarTitle, onBack = onBack, action = topBarAction)
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = Spacing6)
        ) {
            Spacer(modifier = Modifier.height(21.dp))
            Row(
                modifier = Modifier.padding(start = 9.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SoftIconBox(icon = Icons.Rounded.FitnessCenter)
                Spacer(modifier = Modifier.width(Spacing3))
                Text(
                    text = detail.title,
                    style = fitMateTextStyle(20.sp, 20.sp, FontWeight.Bold),
                    color = TextPrimary,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = detail.dateLabel,
                    style = fitMateTextStyle(12.sp, 20.sp),
                    color = TextSecondary,
                    modifier = Modifier.align(Alignment.Top)
                )
            }
            Spacer(modifier = Modifier.height(Spacing4))
            WorkoutStatsRow(stats = detail.stats())
            Spacer(modifier = Modifier.height(21.dp))
            Column(verticalArrangement = Arrangement.spacedBy(Spacing2)) {
                detail.exercises.forEachIndexed { index, exercise ->
                    ExerciseListItem(
                        order = "%02d".format(index + 1),
                        name = exercise.name,
                        detail = exercise.detail,
                        restTime = exercise.restTime,
                        needWeight = showWeightBadges && exercise.needWeight,
                        icon = exercise.icon
                    )
                }
            }
            Spacer(modifier = Modifier.height(Spacing4))
        }
        if (startButton != null) {
            FitMatePrimaryButton(
                text = "시작하기",
                onClick = startButton,
                height = 50.dp,
                modifier = Modifier.padding(start = Spacing6, top = Spacing2, end = Spacing6, bottom = 11.dp)
            )
        }
    }
}

private fun RoutineDayDetailUi.stats(): List<WorkoutStat> = listOfNotNull(
    estimatedMinutes?.let { WorkoutStat("${it}분", "예상 시간") },
    WorkoutStat("${exercises.size}종목", "운동 종목"),
    WorkoutStat("${averageRestSeconds}초", "평균 휴식")
)

// 상단 바 오른쪽 운동 장소 표시 (누르면 장소 변경)
@Composable
private fun PlaceChip(place: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .height(32.dp)
            .clip(ShapeFull)
            .background(White)
            .border(1.dp, BorderDefault, ShapeFull)
            .clickable(onClick = onClick)
            .padding(horizontal = Spacing3),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Rounded.LocationOn,
            contentDescription = null,
            tint = TextSecondary,
            modifier = Modifier.size(15.dp)
        )
        Spacer(modifier = Modifier.width(Spacing1))
        Text(text = place, style = fitMateTextStyle(12.sp, 18.sp), color = TextSecondary)
    }
}

@Preview(name = "운동일", widthDp = 394, heightDp = 812)
@Composable
private fun WorkoutDetailScreenPreview() {
    FitMateTheme { WorkoutDetailScreen() }
}

@Preview(name = "비운동일", widthDp = 394, heightDp = 812)
@Composable
private fun WorkoutDetailScreenOtherDayPreview() {
    FitMateTheme { WorkoutDetailScreen(isToday = false) }
}

@Preview(name = "미리보기", widthDp = 394, heightDp = 892)
@Composable
private fun RoutineDayPreviewScreenPreview() {
    FitMateTheme { RoutineDayPreviewScreen() }
}

@Preview(name = "운동 상세 조회", widthDp = 394, heightDp = 812)
@Composable
private fun ManualDayDetailScreenPreview() {
    FitMateTheme { ManualDayDetailScreen() }
}
