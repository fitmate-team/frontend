package com.fitmate.presentation.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.Bed
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fitmate.presentation.designsystem.theme.Blue100
import com.fitmate.presentation.designsystem.theme.Blue500
import com.fitmate.presentation.designsystem.theme.Blue600
import com.fitmate.presentation.designsystem.theme.BorderDefault
import com.fitmate.presentation.designsystem.theme.FitMateTheme
import com.fitmate.presentation.designsystem.theme.Gray200
import com.fitmate.presentation.designsystem.theme.Gray50
import com.fitmate.presentation.designsystem.theme.IconSizeMedium
import com.fitmate.presentation.designsystem.theme.Spacing2
import com.fitmate.presentation.designsystem.theme.Spacing3
import com.fitmate.presentation.designsystem.theme.Spacing4
import com.fitmate.presentation.designsystem.theme.TextPrimary
import com.fitmate.presentation.designsystem.theme.TextSecondary
import com.fitmate.presentation.designsystem.theme.White
import com.fitmate.presentation.designsystem.theme.fitMateTextStyle

private val RoutineDayShape = RoundedCornerShape(14.dp)
private val RoutineIconShape = RoundedCornerShape(10.dp)

// 요일별 루틴 항목. summary 가 null 이면 휴식일 스타일로 표시
@Composable
fun RoutineDayItem(
    day: String,
    title: String,
    icon: ImageVector,
    summary: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isRest = summary == null
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(72.dp)
            .clip(RoutineDayShape)
            .background(if (isRest) Gray50 else White)
            .border(1.dp, BorderDefault, RoutineDayShape)
            .clickable(enabled = !isRest, onClick = onClick)
            .padding(start = 14.dp, end = 18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoutineIconShape)
                .background(if (isRest) Gray50 else Blue100)
                .border(1.dp, if (isRest) BorderDefault else Blue500, RoutineIconShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isRest) TextSecondary else Blue600,
                modifier = Modifier.size(IconSizeMedium)
            )
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = Spacing4)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Spacing2)
            ) {
                Text(
                    text = day,
                    style = fitMateTextStyle(11.sp, 16.5.sp),
                    color = TextSecondary
                )
                Box(
                    modifier = Modifier
                        .size(width = 1.dp, height = 10.dp)
                        .background(Gray200)
                )
                Text(
                    text = title,
                    style = fitMateTextStyle(13.sp, 21.sp, FontWeight.Bold),
                    color = if (isRest) TextSecondary else TextPrimary
                )
            }
            if (summary != null) {
                Text(
                    text = summary,
                    style = fitMateTextStyle(10.sp, 18.sp),
                    color = TextSecondary
                )
            }
        }
        if (!isRest) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                contentDescription = null,
                tint = Gray200,
                modifier = Modifier
                    .padding(start = Spacing3)
                    .size(IconSizeMedium)
            )
        }
    }
}

@Preview
@Composable
private fun RoutineDayItemPreview() {
    FitMateTheme {
        Column(verticalArrangement = Arrangement.spacedBy(Spacing3), modifier = Modifier.width(346.dp)) {
            RoutineDayItem(day = "월", title = "상체 PUSH", icon = Icons.Rounded.Upload, summary = "5종목 · 약 55분", onClick = {})
            RoutineDayItem(day = "금", title = "휴식", icon = Icons.Rounded.Bed, summary = null, onClick = {})
        }
    }
}
