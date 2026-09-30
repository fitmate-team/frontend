package com.fitmate.presentation.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fitmate.presentation.designsystem.theme.Blue400
import com.fitmate.presentation.designsystem.theme.BorderDefault
import com.fitmate.presentation.designsystem.theme.FitMateTheme
import com.fitmate.presentation.designsystem.theme.Gray100
import com.fitmate.presentation.designsystem.theme.Gray200
import com.fitmate.presentation.designsystem.theme.ShapeFull
import com.fitmate.presentation.designsystem.theme.Spacing1
import com.fitmate.presentation.designsystem.theme.Spacing2
import com.fitmate.presentation.designsystem.theme.Spacing3
import com.fitmate.presentation.designsystem.theme.Spacing4
import com.fitmate.presentation.designsystem.theme.TextPrimary
import com.fitmate.presentation.designsystem.theme.TextSecondary
import com.fitmate.presentation.designsystem.theme.White
import com.fitmate.presentation.designsystem.theme.fitMateTextStyle

private val ExerciseReportShape = RoundedCornerShape(20.dp)
private val SetBarShape = RoundedCornerShape(3.dp)

// 운동 리포트 카드: 순번, 운동명, 수행 내용, 난이도, 세트 진행 막대
@Composable
fun ExerciseReportCard(
    order: Int,
    name: String,
    detail: String,
    difficulty: String,
    completedSets: Int,
    totalSets: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(ExerciseReportShape)
            .background(White)
            .border(1.dp, BorderDefault, ExerciseReportShape)
            .padding(start = Spacing4, end = Spacing3, top = Spacing3, bottom = Spacing4)
    ) {
        Row {
            Box(
                modifier = Modifier
                    .padding(top = 2.dp)
                    .size(30.dp)
                    .clip(CircleShape)
                    .background(Gray100),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = order.toString(),
                    style = fitMateTextStyle(12.sp, 18.sp, FontWeight.Bold),
                    color = TextPrimary
                )
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = Spacing3, top = Spacing1)
            ) {
                Text(
                    text = name,
                    style = fitMateTextStyle(14.sp, 18.sp, FontWeight.Bold),
                    color = TextPrimary
                )
                Text(
                    text = detail,
                    style = fitMateTextStyle(12.sp, 18.sp),
                    color = TextSecondary
                )
            }
            DifficultyChip(text = difficulty)
        }
        Spacer(modifier = Modifier.height(Spacing2))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(Spacing1)
            ) {
                repeat(totalSets) { index ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(14.dp)
                            .clip(SetBarShape)
                            .background(if (index < completedSets) Blue400 else Gray200)
                    )
                }
            }
            Text(
                text = "$completedSets/$totalSets 세트",
                style = fitMateTextStyle(12.sp, 18.sp),
                color = TextPrimary,
                modifier = Modifier.padding(start = Spacing3)
            )
        }
    }
}

@Composable
private fun DifficultyChip(text: String) {
    Box(
        modifier = Modifier
            .height(26.dp)
            .clip(ShapeFull)
            .background(White)
            .border(1.dp, BorderDefault, ShapeFull)
            .padding(horizontal = Spacing3),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = fitMateTextStyle(12.sp, 18.sp),
            color = TextPrimary
        )
    }
}

@Preview
@Composable
private fun ExerciseReportCardPreview() {
    FitMateTheme {
        ExerciseReportCard(
            order = 1,
            name = "벤치프레스",
            detail = "20kg / 8회 × 4세트",
            difficulty = "어려움",
            completedSets = 3,
            totalSets = 4
        )
    }
}
