package com.fitmate.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Smartphone
import androidx.compose.material.icons.outlined.Watch
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fitmate.presentation.designsystem.component.FitMateBackTopBar
import com.fitmate.presentation.designsystem.component.FitMatePrimaryButton
import com.fitmate.presentation.designsystem.theme.Blue400
import com.fitmate.presentation.designsystem.theme.Blue500
import com.fitmate.presentation.designsystem.theme.Blue600
import com.fitmate.presentation.designsystem.theme.BorderDefault
import com.fitmate.presentation.designsystem.theme.FitMateTheme
import com.fitmate.presentation.designsystem.theme.Green100
import com.fitmate.presentation.designsystem.theme.ShapeLarge
import com.fitmate.presentation.designsystem.theme.Spacing1
import com.fitmate.presentation.designsystem.theme.Spacing2
import com.fitmate.presentation.designsystem.theme.Spacing4
import com.fitmate.presentation.designsystem.theme.Spacing5
import com.fitmate.presentation.designsystem.theme.Spacing6
import com.fitmate.presentation.designsystem.theme.TextPrimary
import com.fitmate.presentation.designsystem.theme.TextSecondary
import com.fitmate.presentation.designsystem.theme.White
import com.fitmate.presentation.designsystem.theme.fitMateTextStyle

private val SyncedItems = listOf("실제 중량", "반복수", "세트 완료", "휴식 시간", "운동 시간")
private val DeviceBoxShape = RoundedCornerShape(20.dp)
private val DividerWidth = 1.3.dp

// 2.2 워치에서 운동이 끝났을 때
@Composable
fun WatchSyncResultScreen(
    syncedSetCount: Int = 16,
    onBack: () -> Unit = {},
    onViewResult: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
    ) {
        FitMateBackTopBar(onBack = onBack, showDivider = false)
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = Spacing6),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(114.dp))
            SyncDevices()
            Spacer(modifier = Modifier.height(Spacing5))
            Text(
                text = "Watch 기록을 가져왔어요",
                style = fitMateTextStyle(24.sp, 36.sp, FontWeight.Bold, letterSpacing = (-0.3).sp),
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(Spacing1))
            Text(
                text = "오늘 운동 기록 ${syncedSetCount}세트가 동기화됐어요.",
                style = fitMateTextStyle(14.sp, 21.sp),
                color = TextSecondary
            )
            Spacer(modifier = Modifier.height(28.dp))
            SyncedItemList()
            Spacer(modifier = Modifier.height(56.dp))
            FitMatePrimaryButton(text = "운동 결과 보기", onClick = onViewResult, height = 50.dp)
            Spacer(modifier = Modifier.height(Spacing4))
            Text(
                text = "연결이 끊긴 경우 Watch에 기록을 저장한 뒤\n재연결 시 자동으로 동기화해요.",
                style = fitMateTextStyle(12.sp, 21.sp),
                color = TextSecondary,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(Spacing6))
        }
    }
}

// 워치 → 스마트폰 동기화 아이콘
@Composable
private fun SyncDevices() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        DeviceBox(icon = Icons.Outlined.Watch, containerColor = White, iconColor = Blue500)
        Spacer(modifier = Modifier.width(14.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(Spacing1)) {
            SyncDot(color = Blue600, alpha = 0.76f)
            SyncDot(color = Blue500, alpha = 0.43f)
            SyncDot(color = Blue400, alpha = 0.35f)
        }
        Spacer(modifier = Modifier.width(Spacing4))
        DeviceBox(icon = Icons.Outlined.Smartphone, containerColor = Blue500, iconColor = White)
    }
}

@Composable
private fun DeviceBox(icon: ImageVector, containerColor: Color, iconColor: Color) {
    Box(
        modifier = Modifier
            .size(62.dp)
            .clip(DeviceBoxShape)
            .background(containerColor)
            .border(1.dp, BorderDefault, DeviceBoxShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(32.dp))
    }
}

@Composable
private fun SyncDot(color: Color, alpha: Float) {
    Box(
        modifier = Modifier
            .size(6.dp)
            .alpha(alpha)
            .clip(CircleShape)
            .background(color)
    )
}

// 동기화된 항목 목록 (항목 사이 구분선)
@Composable
private fun SyncedItemList() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(ShapeLarge)
            .background(White)
            .border(DividerWidth, BorderDefault, ShapeLarge)
            .padding(horizontal = Spacing5, vertical = Spacing4)
    ) {
        SyncedItems.forEachIndexed { index, item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(38.dp)
                    .drawBehind {
                        if (index < SyncedItems.lastIndex) {
                            val y = size.height - DividerWidth.toPx() / 2
                            drawLine(BorderDefault, Offset(0f, y), Offset(size.width, y), DividerWidth.toPx())
                        }
                    }
                    .padding(vertical = Spacing2),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.CheckCircle,
                    contentDescription = null,
                    tint = Green100,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = item, style = fitMateTextStyle(14.sp, 21.sp), color = TextPrimary)
            }
        }
    }
}

@Preview(widthDp = 394, heightDp = 892)
@Composable
private fun WatchSyncResultScreenPreview() {
    FitMateTheme {
        WatchSyncResultScreen()
    }
}
