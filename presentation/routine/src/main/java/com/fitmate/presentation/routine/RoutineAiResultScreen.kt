package com.fitmate.presentation.routine

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.DirectionsRun
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.Psychology
import androidx.compose.material.icons.rounded.Upload
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fitmate.presentation.designsystem.R
import com.fitmate.presentation.designsystem.component.FitMateOutlinedButton
import com.fitmate.presentation.designsystem.component.FitMatePrimaryButton
import com.fitmate.presentation.designsystem.component.FitMateTabTopBar
import com.fitmate.presentation.designsystem.component.WeeklyPlanDayCard
import com.fitmate.presentation.designsystem.component.WeeklyPlanFooter
import com.fitmate.presentation.designsystem.theme.Blue100
import com.fitmate.presentation.designsystem.theme.Blue500
import com.fitmate.presentation.designsystem.theme.Blue700
import com.fitmate.presentation.designsystem.theme.BorderDefault
import com.fitmate.presentation.designsystem.theme.FitMateTheme
import com.fitmate.presentation.designsystem.theme.Gray200
import com.fitmate.presentation.designsystem.theme.ShapeFull
import com.fitmate.presentation.designsystem.theme.ShapeLarge
import com.fitmate.presentation.designsystem.theme.Spacing2
import com.fitmate.presentation.designsystem.theme.Spacing4
import com.fitmate.presentation.designsystem.theme.Spacing5
import com.fitmate.presentation.designsystem.theme.Spacing6
import com.fitmate.presentation.designsystem.theme.TextPrimary
import com.fitmate.presentation.designsystem.theme.White
import com.fitmate.presentation.designsystem.theme.fitMateTextStyle

internal enum class PlanIcon { Upper, Lower, Cardio }

internal data class RecommendedDay(
    val dayLabel: String,
    val title: String,
    val exercises: String,
    val duration: String,
    val icon: PlanIcon
)

// 서버 연동 전까지 사용하는 추천 결과 (예시)
internal val SampleRecommendedDays = listOf(
    RecommendedDay("월", "상체", "벤치프레스 · 숄더프레스 · 체스트프레스", "약 55분", PlanIcon.Upper),
    RecommendedDay("목", "하체", "스쿼트 · 레그프레스 · 레그컬", "약 60분", PlanIcon.Lower),
    RecommendedDay("금", "상체", "랫풀다운 · 시티드로우 · 바벨컬", "약 55분", PlanIcon.Upper),
    RecommendedDay("일", "유산소", "러닝", "약 65분", PlanIcon.Cardio)
)

// 루틴 아이콘과 크기. 하체 아이콘은 44dp 박스 전체를 기준으로 그려져 있다
@Composable
internal fun PlanIcon.toIcon(): Pair<ImageVector, Dp> = when (this) {
    PlanIcon.Upper -> Icons.Rounded.Upload to 22.dp
    PlanIcon.Lower -> ImageVector.vectorResource(R.drawable.ic_leg_day) to 44.dp
    PlanIcon.Cardio -> Icons.AutoMirrored.Rounded.DirectionsRun to 22.dp
}

private val ReasonButtonShape = RoundedCornerShape(14.dp)

// 3.6 이번 주 추천 루틴
@Composable
fun RoutineAiResultScreen(
    onOpenDay: (String) -> Unit = {},
    onUseRoutine: () -> Unit = {},
    onWhy: () -> Unit = {},
    onRegenerate: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
    ) {
        FitMateTabTopBar(title = "이번 주 추천 루틴")
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = Spacing6)
        ) {
            Spacer(modifier = Modifier.height(Spacing6))
            RecommendSummaryCard(tags = listOf("체지방 감량", "균형형"), summary = "주 4회 · 회당 약 60분")
            Spacer(modifier = Modifier.height(Spacing5))
            Column(verticalArrangement = Arrangement.spacedBy(Spacing2)) {
                SampleRecommendedDays.forEach { day ->
                    val (icon, iconSize) = day.icon.toIcon()
                    WeeklyPlanDayCard(
                        dayLabel = day.dayLabel,
                        icon = icon,
                        iconSize = iconSize,
                        title = day.title,
                        exercises = day.exercises,
                        footer = WeeklyPlanFooter.Duration(day.duration),
                        onClick = { onOpenDay(day.dayLabel) }
                    )
                }
            }
            Spacer(modifier = Modifier.height(13.dp))
            WhyRecommendButton(onClick = onWhy)
            Spacer(modifier = Modifier.height(Spacing6))
        }
        Column(
            modifier = Modifier.padding(start = Spacing6, end = Spacing6, bottom = Spacing6),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            FitMatePrimaryButton(text = "이 루틴 사용하기", onClick = onUseRoutine)
            FitMateOutlinedButton(text = "다시 만들기", onClick = onRegenerate)
        }
    }
}

// 목표 태그 + 주간 요약
@Composable
private fun RecommendSummaryCard(tags: List<String>, summary: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
            .clip(ShapeLarge)
            .background(Blue100)
            .border(1.dp, Blue500, ShapeLarge)
            .padding(horizontal = Spacing5, vertical = Spacing4)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            tags.forEachIndexed { index, tag ->
                // 첫 태그는 채운 스타일, 나머지는 테두리 스타일
                val filled = index == 0
                Box(
                    modifier = Modifier
                        .height(24.dp)
                        .clip(ShapeFull)
                        .background(if (filled) Blue500 else Blue100)
                        .border(1.dp, Blue500, ShapeFull)
                        .padding(horizontal = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = tag,
                        style = fitMateTextStyle(11.sp, 16.5.sp, FontWeight.Bold),
                        color = if (filled) White else Blue500
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(Spacing2))
        Text(text = summary, style = fitMateTextStyle(13.sp, 20.sp, FontWeight.Bold), color = Blue700)
    }
}

@Composable
private fun WhyRecommendButton(onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(ReasonButtonShape)
            .background(White)
            .border(1.dp, BorderDefault, ReasonButtonShape)
            .clickable(onClick = onClick)
            .padding(horizontal = Spacing4, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = Icons.Rounded.Psychology, contentDescription = null, tint = Blue500, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(10.dp))
        Text(text = "왜 이렇게 추천했나요?", style = fitMateTextStyle(14.sp, 21.sp, FontWeight.Bold), color = TextPrimary)
        Spacer(modifier = Modifier.width(10.dp))
        Icon(imageVector = Icons.Rounded.ChevronRight, contentDescription = null, tint = Gray200, modifier = Modifier.size(20.dp))
    }
}

@Preview(widthDp = 394, heightDp = 892)
@Composable
private fun RoutineAiResultScreenPreview() {
    FitMateTheme { RoutineAiResultScreen() }
}
