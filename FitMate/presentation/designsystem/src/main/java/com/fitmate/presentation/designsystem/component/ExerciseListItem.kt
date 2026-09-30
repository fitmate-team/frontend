package com.fitmate.presentation.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FitnessCenter
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fitmate.presentation.designsystem.theme.Blue500
import com.fitmate.presentation.designsystem.theme.BorderDefault
import com.fitmate.presentation.designsystem.theme.FitMateTheme
import com.fitmate.presentation.designsystem.theme.Gray200
import com.fitmate.presentation.designsystem.theme.Gray50
import com.fitmate.presentation.designsystem.theme.ShapeFull
import com.fitmate.presentation.designsystem.theme.ShapeLarge
import com.fitmate.presentation.designsystem.theme.Spacing2
import com.fitmate.presentation.designsystem.theme.Spacing3
import com.fitmate.presentation.designsystem.theme.Spacing4
import com.fitmate.presentation.designsystem.theme.TextPrimary
import com.fitmate.presentation.designsystem.theme.TextSecondary
import com.fitmate.presentation.designsystem.theme.White
import com.fitmate.presentation.designsystem.theme.fitMateTextStyle

private val ExerciseIconShape = RoundedCornerShape(10.dp)

// 루틴 운동 목록 항목
// needWeight = true 이면 "중량 설정 필요" 배지 표시
@Composable
fun ExerciseListItem(
    order: String,
    name: String,
    detail: String,
    restTime: String,
    modifier: Modifier = Modifier,
    needWeight: Boolean = false
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(94.dp)
            .clip(ShapeLarge)
            .background(White)
            .border(1.dp, BorderDefault, ShapeLarge)
            .padding(Spacing4),
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(ExerciseIconShape)
                    .background(Gray50)
                    .border(1.dp, Gray200, ExerciseIconShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.FitnessCenter,
                    contentDescription = null,
                    tint = TextSecondary,
                    modifier = Modifier.size(18.dp)
                )
            }
            Text(
                text = order,
                style = fitMateTextStyle(11.sp, 16.5.sp),
                color = TextSecondary
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Spacing2)
            ) {
                Text(
                    text = name,
                    style = fitMateTextStyle(15.sp, 22.5.sp, FontWeight.Bold),
                    color = TextPrimary
                )
                if (needWeight) {
                    WeightRequiredBadge()
                }
            }
            Text(
                text = detail,
                style = fitMateTextStyle(13.sp, 18.sp),
                color = TextSecondary
            )
            Text(
                text = restTime,
                style = fitMateTextStyle(12.sp, 18.sp),
                color = Blue500
            )
        }
    }
}

@Composable
private fun WeightRequiredBadge() {
    Box(
        modifier = Modifier
            .height(20.dp)
            .clip(ShapeFull)
            .background(Blue500)
            .padding(horizontal = Spacing2),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "중량 설정 필요",
            style = fitMateTextStyle(10.sp, 15.sp, FontWeight.Medium),
            color = White
        )
    }
}

@Preview
@Composable
private fun ExerciseListItemPreview() {
    FitMateTheme {
        Column(verticalArrangement = Arrangement.spacedBy(Spacing3)) {
            ExerciseListItem(order = "01", name = "벤치프레스", detail = "8회 × 4세트", restTime = "휴식 90초", needWeight = true)
            ExerciseListItem(order = "01", name = "벤치프레스", detail = "40kg / 8회 × 4세트", restTime = "휴식 90초")
            ExerciseListItem(order = "01", name = "벤치프레스", detail = "15분", restTime = "휴식 90초")
        }
    }
}
