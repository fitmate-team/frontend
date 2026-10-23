package com.fitmate.wear

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.EmojiEvents
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
import com.fitmate.presentation.designsystem.theme.White
import com.fitmate.wear.component.WatchOutlinedChip
import com.fitmate.wear.component.WatchPillButton
import com.fitmate.wear.component.WatchScreen
import com.fitmate.wear.component.WatchStat
import com.fitmate.wear.component.watchTextStyle

// 워치 - 운동 끝
@Composable
fun WatchFinishScreen(
    elapsedMinutes: Int,
    totalSets: Int,
    onConfirm: () -> Unit = {}
) {
    WatchScreen {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(Blue500),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.EmojiEvents,
                    contentDescription = null,
                    tint = White,
                    modifier = Modifier.size(28.dp)
                )
            }
            Text(
                text = "운동 완료!",
                style = watchTextStyle(18.sp, 27.sp, FontWeight.Medium),
                color = White,
                modifier = Modifier.padding(top = 10.dp, bottom = 6.dp)
            )
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                WatchStat(value = "${elapsedMinutes}분", label = "시간")
                WatchStat(value = "${totalSets}세트", label = "세트")
            }
            WatchOutlinedChip(
                text = "휴대폰과 동기화 완료",
                horizontalPadding = 10.dp,
                borderWidth = 1.301.dp,
                textStyle = watchTextStyle(12.sp, 13.5.sp),
                modifier = Modifier.padding(top = 8.dp, bottom = 12.dp)
            )
            WatchPillButton(
                text = "확인",
                onClick = onConfirm,
                textStyle = watchTextStyle(14.sp, 21.sp, FontWeight.Medium),
                modifier = Modifier.size(width = 80.dp, height = 36.dp)
            )
        }
    }
}

@Preview(widthDp = 304, heightDp = 304)
@Composable
private fun WatchFinishScreenPreview() {
    WatchFinishScreen(elapsedMinutes = 52, totalSets = 16)
}
