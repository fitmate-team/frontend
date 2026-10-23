package com.fitmate.presentation.designsystem.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fitmate.presentation.designsystem.theme.BorderDefault
import com.fitmate.presentation.designsystem.theme.FitMateTheme
import com.fitmate.presentation.designsystem.theme.Spacing2
import com.fitmate.presentation.designsystem.theme.Spacing4
import com.fitmate.presentation.designsystem.theme.TextPrimary
import com.fitmate.presentation.designsystem.theme.TextSecondary
import com.fitmate.presentation.designsystem.theme.fitMateTextStyle

data class WorkoutStat(val value: String, val label: String)

enum class WorkoutStatsStyle {
    // 위쪽 구분선만 (운동 카드 안)
    TopDivider,

    // 전체 테두리 박스 (나의 루틴 요약)
    Boxed
}

private val BoxedShape = RoundedCornerShape(10.dp)

// 값 + 라벨 여러 칸 (예상 시간 / 운동 종목 / 평균 휴식 ...). 칸 사이 세로 구분선
@Composable
fun WorkoutStatsRow(
    stats: List<WorkoutStat>,
    modifier: Modifier = Modifier,
    style: WorkoutStatsStyle = WorkoutStatsStyle.TopDivider
) {
    val containerModifier = when (style) {
        WorkoutStatsStyle.TopDivider -> Modifier.drawBehind {
            drawLine(BorderDefault, Offset(0f, 0f), Offset(size.width, 0f), 1.dp.toPx())
        }
        WorkoutStatsStyle.Boxed -> Modifier.border(1.dp, BorderDefault, BoxedShape)
    }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .then(containerModifier)
            .padding(vertical = if (style == WorkoutStatsStyle.Boxed) 9.dp else Spacing2),
        verticalAlignment = Alignment.CenterVertically
    ) {
        stats.forEachIndexed { index, stat ->
            Column(
                modifier = Modifier
                    .weight(1f)
                    .height(46.dp)
                    .drawBehind {
                        if (index < stats.lastIndex) {
                            val x = size.width - 0.5.dp.toPx()
                            drawLine(BorderDefault, Offset(x, 0f), Offset(x, size.height), 1.dp.toPx())
                        }
                    },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stat.value,
                    style = fitMateTextStyle(16.sp, 20.sp, FontWeight.Bold),
                    color = TextPrimary,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = stat.label,
                    style = fitMateTextStyle(14.sp, 20.sp),
                    color = TextSecondary,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview(widthDp = 346)
@Composable
private fun WorkoutStatsRowPreview() {
    FitMateTheme {
        Column(verticalArrangement = Arrangement.spacedBy(Spacing4)) {
            WorkoutStatsRow(
                stats = listOf(WorkoutStat("55분", "예상 시간"), WorkoutStat("5종목", "운동 종목"), WorkoutStat("60초", "평균 휴식"))
            )
            WorkoutStatsRow(
                stats = listOf(WorkoutStat("헬스장", "운동 장소"), WorkoutStat("약 55분", "일별 평균 시간"), WorkoutStat("초급", "난이도")),
                style = WorkoutStatsStyle.Boxed
            )
        }
    }
}
