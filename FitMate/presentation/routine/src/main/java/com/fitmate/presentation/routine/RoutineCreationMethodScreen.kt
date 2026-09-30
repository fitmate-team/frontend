package com.fitmate.presentation.routine

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ChevronRight
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private val FitMateBlue = Color(0xFF3E78AD)
private val ScreenBackground = Color(0xFFF8F9FA)
private val CardBorder = Color(0xFFE2E6EA)
private val TextMuted = Color(0xFF6C757D)
private val IconBg = Color(0xFFDBE9F5)

@Composable
fun RoutineCreationMethodScreen(
    onBack: () -> Unit = {},
    onAiHandsOff: () -> Unit = {},
    onAiTogether: () -> Unit = {}
) {
    Column(modifier = Modifier.fillMaxSize().background(ScreenBackground)) {
        IconButton(onClick = onBack, modifier = Modifier.padding(4.dp)) {
            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "뒤로")
        }

        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "어떻게 루틴을 만들까요?",
                fontWeight = FontWeight.Bold,
                fontSize = MaterialTheme.typography.headlineMedium.fontSize
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "두 방식 중 나에게 맞는 걸 선택해보세요.", color = TextMuted)

            Spacer(modifier = Modifier.height(48.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = IconBg,
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF9BC0DE)),
                onClick = onAiHandsOff
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(modifier = Modifier.size(64.dp).clip(RoundedCornerShape(8.dp)).background(CardBorder))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "AI에게 맡기기", color = FitMateBlue, fontWeight = FontWeight.Medium, fontSize = MaterialTheme.typography.bodyLarge.fontSize)
                        Text(text = "내 목표와 운동 환경에 맞게\n주간 루틴 전체를 만들어드려요.", color = TextMuted, fontSize = MaterialTheme.typography.bodySmall.fontSize)
                    }
                    Icon(imageVector = Icons.Filled.ChevronRight, contentDescription = null, tint = FitMateBlue)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                border = androidx.compose.foundation.BorderStroke(1.dp, FitMateBlue),
                onClick = onAiTogether
            ) {
                Box {
                    Row(
                        modifier = Modifier.padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Box(
                            modifier = Modifier.size(56.dp).clip(RoundedCornerShape(14.dp)).background(FitMateBlue),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(imageVector = Icons.Filled.AutoAwesome, contentDescription = null, tint = Color.White)
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "AI와 함께 만들기", fontWeight = FontWeight.Medium, fontSize = MaterialTheme.typography.bodyLarge.fontSize)
                            Text(text = "내가 정한 운동은 유지하고\n빈 부분은 AI가 채워드려요.", color = TextMuted, fontSize = MaterialTheme.typography.bodySmall.fontSize)
                        }
                        Icon(imageVector = Icons.Filled.ChevronRight, contentDescription = null, tint = FitMateBlue)
                    }
                    Surface(
                        modifier = Modifier.align(Alignment.TopEnd),
                        color = FitMateBlue,
                        shape = RoundedCornerShape(bottomStart = 12.dp)
                    ) {
                        Text(
                            text = "FitMate 추천",
                            color = Color.White,
                            fontSize = MaterialTheme.typography.labelSmall.fontSize,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}
