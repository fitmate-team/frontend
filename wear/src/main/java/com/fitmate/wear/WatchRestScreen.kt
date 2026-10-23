package com.fitmate.wear

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fitmate.presentation.designsystem.theme.Blue500
import com.fitmate.presentation.designsystem.theme.Gray900
import com.fitmate.presentation.designsystem.theme.White
import com.fitmate.wear.component.WatchOutlinedChip
import com.fitmate.wear.component.WatchPillButton
import com.fitmate.wear.component.WatchScreen
import com.fitmate.wear.component.watchTextStyle

// 휴식 링: 293dp 영역 안 반지름 131.86dp, 두께 6dp. 흰 트랙 위에 남은 시간만큼 파란 호를 12시부터 그린다
private val RestRingSize = 293.dp
private val RestRingRadius = 131.864.dp
private val RestRingStroke = 6.dp

// 워치 - 휴식 (다음 세트까지 남은 시간)
@Composable
fun WatchRestScreen(
    remainingSeconds: Int,
    totalSeconds: Int,
    nextSet: WatchSet,
    nextSetNumber: Int,
    setCount: Int,
    onAddTime: () -> Unit = {},
    onSkip: () -> Unit = {}
) {
    val fraction = if (totalSeconds > 0) remainingSeconds.toFloat() / totalSeconds else 0f

    WatchScreen {
        RestRing(fraction = fraction.coerceIn(0f, 1f))
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "REST",
                style = watchTextStyle(12.sp, 15.sp, letterSpacing = 1.sp),
                color = White,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = "%02d:%02d".format(remainingSeconds / 60, remainingSeconds % 60),
                style = watchTextStyle(40.sp, 60.sp, FontWeight.Bold),
                color = White
            )
            Column(
                modifier = Modifier.padding(top = 12.dp, bottom = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "${nextSet.weightKg}kg × ${nextSet.reps}",
                    style = watchTextStyle(14.sp, 19.5.sp, FontWeight.Medium),
                    color = White
                )
                Text(
                    text = "SET $nextSetNumber / $setCount",
                    style = watchTextStyle(12.sp, 15.sp),
                    color = White,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                WatchPillButton(
                    text = "+30",
                    onClick = onAddTime,
                    outlined = true,
                    textStyle = watchTextStyle(13.sp, 18.sp),
                    modifier = Modifier.size(width = 56.dp, height = 32.dp)
                )
                // 디자인은 흰 배경에 흰 글자라 라벨이 보이지 않아, 글자만 Gray900으로 둔다
                WatchPillButton(
                    text = "건너뛰기",
                    onClick = onSkip,
                    containerColor = White,
                    contentColor = Gray900,
                    textStyle = watchTextStyle(13.sp, 16.5.sp),
                    modifier = Modifier.size(width = 72.dp, height = 32.dp)
                )
            }
        }
    }
}

@Composable
private fun RestRing(fraction: Float) {
    Canvas(modifier = Modifier.size(RestRingSize)) {
        val radius = RestRingRadius.toPx()
        val stroke = RestRingStroke.toPx()
        val topLeft = Offset(center.x - radius, center.y - radius)
        val arcSize = Size(radius * 2, radius * 2)
        drawCircle(color = White, radius = radius, style = Stroke(width = stroke))
        drawArc(
            color = Blue500,
            startAngle = -90f,
            sweepAngle = 360f * fraction,
            useCenter = false,
            topLeft = topLeft,
            size = arcSize,
            style = Stroke(width = stroke, cap = StrokeCap.Round)
        )
    }
}

@Preview(widthDp = 304, heightDp = 304)
@Composable
private fun WatchRestScreenPreview() {
    WatchRestScreen(
        remainingSeconds = 78,
        totalSeconds = 78,
        nextSet = WatchSet(40, 8),
        nextSetNumber = 3,
        setCount = 4
    )
}
