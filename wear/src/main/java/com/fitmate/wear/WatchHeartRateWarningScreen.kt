package com.fitmate.wear

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fitmate.presentation.designsystem.theme.White
import com.fitmate.presentation.designsystem.theme.Yellow200
import com.fitmate.wear.component.WatchCardShape
import com.fitmate.wear.component.WatchPillButton
import com.fitmate.wear.component.WatchScreen
import com.fitmate.wear.component.watchTextStyle

// 워치 - 운동 중 심박수 경고
@Composable
fun WatchHeartRateWarningScreen(onRest: () -> Unit = {}) {
    WatchScreen {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Column(
                modifier = Modifier
                    .size(width = 182.dp, height = 115.dp)
                    .clip(WatchCardShape)
                    .background(Yellow200),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically)
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_heart_rate_warning),
                    contentDescription = null,
                    modifier = Modifier.size(width = 20.dp, height = 18.dp)
                )
                Text(
                    text = "심박수가 너무\n높습니다",
                    style = watchTextStyle(16.sp, 24.sp),
                    color = White,
                    textAlign = TextAlign.Center
                )
            }
            WatchPillButton(
                text = "휴식하러 가기",
                onClick = onRest,
                modifier = Modifier.size(width = 136.dp, height = 37.dp)
            )
        }
    }
}

@Preview(widthDp = 304, heightDp = 304)
@Composable
private fun WatchHeartRateWarningScreenPreview() {
    WatchHeartRateWarningScreen()
}
