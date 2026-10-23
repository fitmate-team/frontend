package com.fitmate.presentation.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.Upload
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fitmate.presentation.designsystem.theme.Blue100
import com.fitmate.presentation.designsystem.theme.Blue500
import com.fitmate.presentation.designsystem.theme.Blue600
import com.fitmate.presentation.designsystem.theme.BorderDefault
import com.fitmate.presentation.designsystem.theme.FitMateTheme
import com.fitmate.presentation.designsystem.theme.Gray200
import com.fitmate.presentation.designsystem.theme.ShapeLarge
import com.fitmate.presentation.designsystem.theme.ShapeMedium
import com.fitmate.presentation.designsystem.theme.Spacing1
import com.fitmate.presentation.designsystem.theme.Spacing2
import com.fitmate.presentation.designsystem.theme.Spacing4
import com.fitmate.presentation.designsystem.theme.TextPrimary
import com.fitmate.presentation.designsystem.theme.TextSecondary
import com.fitmate.presentation.designsystem.theme.White
import com.fitmate.presentation.designsystem.theme.fitMateTextStyle

private val DayIconSize = 44.dp

// 주간 루틴의 하루 카드 (추천 루틴 / 직접 만든 루틴). 아래 줄은 예상 시간 또는 휴식 시간
@Composable
fun WeeklyPlanDayCard(
    dayLabel: String,
    icon: ImageVector,
    title: String,
    exercises: String,
    footer: WeeklyPlanFooter,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    iconSize: Dp = 22.dp
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp)
            .clip(ShapeLarge)
            .background(White)
            .border(1.dp, BorderDefault, ShapeLarge)
            .clickable(onClick = onClick)
            .padding(Spacing4),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(DayIconSize)
                    .clip(ShapeMedium)
                    .background(Blue100),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = Blue600, modifier = Modifier.size(iconSize))
            }
            Spacer(modifier = Modifier.height(Spacing1))
            Text(text = dayLabel, style = fitMateTextStyle(11.sp, 16.5.sp), color = TextPrimary)
        }
        Spacer(modifier = Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, style = fitMateTextStyle(15.sp, 22.5.sp, FontWeight.Bold), color = TextPrimary)
            Text(text = exercises, style = fitMateTextStyle(12.sp, 18.sp), color = TextSecondary)
            when (footer) {
                is WeeklyPlanFooter.Duration -> Row(
                    modifier = Modifier.padding(top = Spacing1),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Schedule,
                        contentDescription = null,
                        tint = TextSecondary,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(Spacing1))
                    Text(text = footer.text, style = fitMateTextStyle(12.sp, 18.sp, FontWeight.Bold), color = TextSecondary)
                }
                is WeeklyPlanFooter.Rest -> Text(
                    text = footer.text,
                    style = fitMateTextStyle(12.sp, 18.sp),
                    color = Blue500
                )
            }
        }
        Spacer(modifier = Modifier.width(14.dp))
        Icon(
            imageVector = Icons.Rounded.ChevronRight,
            contentDescription = null,
            tint = Gray200,
            modifier = Modifier.size(20.dp)
        )
    }
}

sealed interface WeeklyPlanFooter {
    val text: String

    // 시계 아이콘 + 예상 시간 (약 55분)
    data class Duration(override val text: String) : WeeklyPlanFooter

    // 파란 휴식 시간 (휴식 90초)
    data class Rest(override val text: String) : WeeklyPlanFooter
}

@Preview(widthDp = 346)
@Composable
private fun WeeklyPlanDayCardPreview() {
    FitMateTheme {
        Column(verticalArrangement = Arrangement.spacedBy(Spacing2)) {
            WeeklyPlanDayCard(
                dayLabel = "월",
                icon = Icons.Rounded.Upload,
                title = "상체",
                exercises = "벤치프레스 · 숄더프레스 · 체스트프레스",
                footer = WeeklyPlanFooter.Duration("약 55분"),
                onClick = {}
            )
            WeeklyPlanDayCard(
                dayLabel = "월",
                icon = Icons.Rounded.Upload,
                title = "상체",
                exercises = "벤치프레스 · 숄더프레스 · 체스트프레스",
                footer = WeeklyPlanFooter.Rest("휴식 90초"),
                onClick = {}
            )
        }
    }
}
