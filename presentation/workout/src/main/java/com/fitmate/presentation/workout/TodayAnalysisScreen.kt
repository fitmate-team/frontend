package com.fitmate.presentation.workout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Timer
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private val FitMateBlue = Color(0xFF3E78AD)
private val HeaderText = Color(0xFF27384A)
private val ScreenBackground = Color(0xFFF8F9FA)
private val CardBorder = Color(0xFFE2E6EA)
private val TextMuted = Color(0xFF6C757D)
private val InfoBg = Color(0xFFE1ECF4)

@Composable
fun TodayAnalysisScreen(
    onBack: () -> Unit = {},
    onViewSuggestion: () -> Unit = {}
) {
    Column(modifier = Modifier.fillMaxSize().background(ScreenBackground)) {
        Column(modifier = Modifier.fillMaxWidth().background(Color.White).padding(20.dp)) {
            IconButton(onClick = onBack, modifier = Modifier.padding(0.dp)) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "뒤로")
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = "오늘 운동을 분석했어요", fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.headlineSmall.fontSize)
            Spacer(modifier = Modifier.height(16.dp))
            Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Column {
                    Text(text = "전체 수행률", color = HeaderText, fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.labelMedium.fontSize)
                    Text(text = "92%", fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.displayMedium.fontSize)
                }
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.padding(bottom = 12.dp)) {
                    Icon(imageVector = Icons.Filled.Timer, contentDescription = null, tint = HeaderText, modifier = Modifier.size(14.dp))
                    Text(text = "실제 운동 시간 52분", color = HeaderText, fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.labelSmall.fontSize)
                }
            }
        }

        Column(modifier = Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "벤치프레스", fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.titleMedium.fontSize)
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Surface(modifier = Modifier.weight(1f), shape = RoundedCornerShape(10.dp), color = ScreenBackground) {
                            Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)) {
                                Text(text = "AI 권장", color = TextMuted, fontSize = MaterialTheme.typography.labelSmall.fontSize)
                                Text(text = "40kg × 8회 × 4세트", fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.labelMedium.fontSize)
                            }
                        }
                        Surface(modifier = Modifier.weight(1f), shape = RoundedCornerShape(10.dp), color = ScreenBackground) {
                            Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)) {
                                Text(text = "실제 수행", color = TextMuted, fontSize = MaterialTheme.typography.labelSmall.fontSize)
                                Text(text = "40kg", fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.labelMedium.fontSize)
                                Text(text = "8 / 8 / 8 / 7", color = TextMuted, fontSize = MaterialTheme.typography.labelSmall.fontSize)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Surface(shape = RoundedCornerShape(100.dp), color = Color(0xFFFFF3E0)) {
                        Text(text = "🥵 거의 한계", color = Color(0xFFE65100), fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.labelSmall.fontSize, modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp))
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(10.dp), color = ScreenBackground, border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)) {
                        Text(text = "마지막 세트에서 목표 반복수를 채우지 못했고 체감 난이도도 높았어요.", color = TextMuted, fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.bodySmall.fontSize, modifier = Modifier.padding(12.dp))
                    }
                }
            }

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                color = InfoBg,
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFB0CBDC))
            ) {
                Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Icon(imageVector = Icons.Filled.AutoAwesome, contentDescription = null, tint = Color(0xFF42688A), modifier = Modifier.size(18.dp))
                    Column {
                        Text(text = "숄더프레스", color = HeaderText, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "모든 세트를 완료했고 최근 기록보다 수행이 좋아졌어요.", color = HeaderText, fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.bodySmall.fontSize)
                    }
                }
            }
        }

        Column(modifier = Modifier.background(ScreenBackground).padding(20.dp)) {
            Button(
                onClick = onViewSuggestion,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = FitMateBlue),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(text = "다음 운동 제안 보기")
            }
        }
    }
}
