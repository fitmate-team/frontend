package com.fitmate.presentation.home

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material.icons.filled.Watch
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

private val FitMateBlue = Color(0xFF3E78AD)
private val ScreenBackground = Color(0xFFF8F9FA)
private val CardBorder = Color(0xFFE2E6EA)
private val TextMuted = Color(0xFF6C757D)
private val CheckGreen = Color(0xFF428A54)

private val syncedItems = listOf("실제 중량", "반복수", "세트 완료", "휴식시간", "운동시간")

@Composable
fun WatchSyncResultScreen(
    onBack: () -> Unit = {},
    onViewResult: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ScreenBackground)
    ) {
        IconButton(onClick = onBack, modifier = Modifier.padding(4.dp)) {
            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "뒤로")
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF1A1A1A)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = Icons.Filled.Watch, contentDescription = null, tint = Color.White, modifier = Modifier.size(32.dp))
                }
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    repeat(3) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(FitMateBlue.copy(alpha = 0.7f - it * 0.15f))
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(FitMateBlue),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = Icons.Filled.Smartphone, contentDescription = null, tint = Color.White, modifier = Modifier.size(32.dp))
                }
            }

            Spacer(modifier = Modifier.height(28.dp))
            Text(
                text = "Watch 기록을 가져왔어요",
                fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "오늘 운동 기록 16세트가 동기화됐어요.",
                color = TextMuted,
                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(28.dp))
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
            ) {
                Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)) {
                    syncedItems.forEachIndexed { index, label ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.CheckCircle,
                                contentDescription = null,
                                tint = CheckGreen,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(text = label, fontSize = MaterialTheme.typography.bodyMedium.fontSize)
                        }
                        if (index < syncedItems.lastIndex) {
                            Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(CardBorder))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = onViewResult,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = FitMateBlue),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(text = "운동 결과 보기")
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "연결이 끊긴 경우 Watch에 기록을 저장한 뒤 재연결 시 자동으로 동기화해요.",
                color = TextMuted,
                fontSize = MaterialTheme.typography.labelSmall.fontSize,
                textAlign = TextAlign.Center,
                modifier = Modifier.width(220.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
