package com.fitmate.presentation.workout

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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

private val FitMateBlue = Color(0xFF3E78AD)
private val ScreenBackground = Color(0xFFF8F9FA)
private val CardBorder = Color(0xFFE2E6EA)
private val TextMuted = Color(0xFF6C757D)

@Composable
fun RestTimerScreen(
    initialSeconds: Int = 90,
    nextExerciseName: String = "벤치프레스",
    nextSetSummary: String = "40kg × 8회",
    onNextSet: () -> Unit = {},
    onSkipRest: () -> Unit = {}
) {
    var secondsLeft by remember { mutableIntStateOf(87) }
    var totalSeconds by remember { mutableIntStateOf(initialSeconds) }

    LaunchedEffect(secondsLeft) {
        if (secondsLeft > 0) {
            delay(1000)
            secondsLeft--
        } else {
            onNextSet()
        }
    }

    Column(modifier = Modifier.fillMaxSize().background(ScreenBackground)) {
        Column(modifier = Modifier.fillMaxWidth().background(FitMateBlue).padding(horizontal = 20.dp, vertical = 24.dp)) {
            Text(text = "휴식 중", color = Color(0xFFB0CBDC), fontWeight = FontWeight.Bold)
        }

        Column(
            modifier = Modifier.weight(1f).padding(horizontal = 20.dp).padding(top = 36.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(modifier = Modifier.size(260.dp), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    progress = { if (totalSeconds > 0) secondsLeft.toFloat() / totalSeconds else 0f },
                    modifier = Modifier.size(260.dp),
                    color = FitMateBlue,
                    trackColor = CardBorder,
                    strokeWidth = 6.dp
                )
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = formatTime(secondsLeft), fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.displayMedium.fontSize)
                    Text(text = "남은 시간", color = TextMuted, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(36.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Surface(
                    shape = RoundedCornerShape(100.dp),
                    color = Color.White,
                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
                    onClick = {
                        secondsLeft = (secondsLeft - 30).coerceAtLeast(0)
                        totalSeconds = (totalSeconds - 30).coerceAtLeast(1)
                    }
                ) {
                    Text(text = "−30초", fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp))
                }
                Surface(
                    shape = RoundedCornerShape(100.dp),
                    color = Color.White,
                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
                    onClick = {
                        secondsLeft += 30
                        totalSeconds += 30
                    }
                ) {
                    Text(text = "+30초", fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
            ) {
                Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)) {
                    Text(text = "다음 세트", color = TextMuted, fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.labelSmall.fontSize)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(text = nextExerciseName, fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.titleMedium.fontSize)
                    Text(text = nextSetSummary, color = TextMuted, fontWeight = FontWeight.Bold)
                }
            }
        }

        Column(modifier = Modifier.padding(20.dp)) {
            Button(
                onClick = onNextSet,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = FitMateBlue),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(text = "다음 세트")
            }
            TextButton(onClick = onSkipRest, modifier = Modifier.fillMaxWidth()) {
                Text(text = "휴식 건너뛰기", color = TextMuted, fontWeight = FontWeight.Bold)
            }
        }
    }
}

private fun formatTime(totalSeconds: Int): String {
    val m = totalSeconds / 60
    val s = totalSeconds % 60
    return "%02d:%02d".format(m, s)
}
