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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

private val FitMateBlue = Color(0xFF3E78AD)
private val AccentBlue = Color(0xFF9BC0DE)

private val loadingSteps = listOf("운동 목표 분석", "운동 경험 확인", "운동 가능 시간 확인", "사용 가능한 기구 확인")

@Composable
fun RoutineAiLoadingScreen(onComplete: () -> Unit = {}) {
    LaunchedEffect(Unit) {
        delay(1600)
        onComplete()
    }

    Box(
        modifier = Modifier.fillMaxSize().background(FitMateBlue).padding(horizontal = 28.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(modifier = Modifier.size(140.dp).clip(RoundedCornerShape(17.dp)).background(Color(0xFFE2E6EA)))
            Spacer(modifier = Modifier.height(36.dp))
            Text(
                text = "회원님에게 맞는 운동을\n분석하고 있어요",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(36.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White.copy(alpha = 0.08f))
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                loadingSteps.forEach { step ->
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Box(
                            modifier = Modifier.size(24.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.9f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(imageVector = Icons.Filled.Check, contentDescription = null, tint = FitMateBlue, modifier = Modifier.size(16.dp))
                        }
                        Text(text = step, color = Color.White, fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.bodyMedium.fontSize)
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))
            LinearProgressIndicator(
                progress = { 0.88f },
                modifier = Modifier.fillMaxWidth().height(4.dp).clip(RoundedCornerShape(2.dp)),
                color = AccentBlue,
                trackColor = Color.White.copy(alpha = 0.15f)
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(text = "잠시만 기다려주세요.", color = Color.White.copy(alpha = 0.45f), fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.labelLarge.fontSize)
        }
    }
}
