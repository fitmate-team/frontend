package com.fitmate.presentation.routine

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private val FitMateBlue = Color(0xFF3E78AD)
private val ScreenBackground = Color(0xFFF8F9FA)
private val CardBorder = Color(0xFFE2E6EA)
private val TextMuted = Color(0xFF6C757D)
private val IconBg = Color(0xFFDBE9F5)

private data class ResultDay(val label: String, val title: String, val exercises: String, val duration: String, val icon: ImageVector)

private val resultDays = listOf(
    ResultDay("월", "상체 PUSH", "벤치프레스 · 숄더프레스 · 체스트프레스", "약 55분", Icons.Filled.Upload),
    ResultDay("화", "하체", "스쿼트 · 레그프레스 · 레그컬", "약 60분", Icons.Filled.DirectionsWalk),
    ResultDay("목", "상체 PULL", "랫풀다운 · 시티드로우 · 바벨컬", "약 55분", Icons.Filled.Download),
    ResultDay("토", "전신 + 유산소", "전신 운동 · 러닝 20분", "약 60분", Icons.Filled.DirectionsRun)
)

@Composable
fun RoutineAiResultScreen(
    onUseRoutine: () -> Unit = {},
    onWhy: () -> Unit = {},
    onRegenerate: () -> Unit = {}
) {
    Column(modifier = Modifier.fillMaxSize().background(ScreenBackground)) {
        Column(
            modifier = Modifier.fillMaxWidth().background(FitMateBlue).padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Text(text = "AI 생성 루틴", color = Color(0xFF9BC0DE), fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.labelSmall.fontSize)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "이번 주 추천 루틴", color = Color.White, fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.headlineSmall.fontSize)
        }

        Column(modifier = Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(20.dp)) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = IconBg,
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF9BC0DE))
            ) {
                Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Surface(shape = RoundedCornerShape(100.dp), color = FitMateBlue) {
                            Text(text = "체지방 감량", color = Color.White, fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.labelSmall.fontSize, modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp))
                        }
                        Surface(shape = RoundedCornerShape(100.dp), color = Color.Transparent, border = androidx.compose.foundation.BorderStroke(1.dp, FitMateBlue)) {
                            Text(text = "균형형", color = FitMateBlue, fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.labelSmall.fontSize, modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp))
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "주 4회 · 회당 약 60분", color = Color(0xFF294767), fontWeight = FontWeight.Bold)
                    Text(text = "근력 70% · 유산소 30%", color = FitMateBlue, fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.bodySmall.fontSize)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                resultDays.forEach { day ->
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        color = Color.White,
                        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                androidx.compose.foundation.layout.Box(
                                    modifier = Modifier.size(44.dp).clip(RoundedCornerShape(12.dp)).background(IconBg),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(imageVector = day.icon, contentDescription = null, tint = FitMateBlue)
                                }
                                Text(text = day.label, color = FitMateBlue, fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.labelSmall.fontSize)
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = day.title, fontWeight = FontWeight.Bold)
                                Text(text = day.exercises, color = TextMuted, fontSize = MaterialTheme.typography.labelMedium.fontSize)
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Icon(imageVector = Icons.Filled.Schedule, contentDescription = null, tint = TextMuted, modifier = Modifier.size(14.dp))
                                    Text(text = day.duration, color = TextMuted, fontSize = MaterialTheme.typography.labelMedium.fontSize)
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                color = Color.White,
                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
                onClick = onWhy
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(imageVector = Icons.Filled.Psychology, contentDescription = null, tint = FitMateBlue)
                    Text(text = "왜 이렇게 추천했나요?", modifier = Modifier.weight(1f))
                    Icon(imageVector = Icons.Filled.ChevronRight, contentDescription = null, tint = TextMuted)
                }
            }
        }

        Column(modifier = Modifier.background(ScreenBackground).padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Button(
                onClick = onUseRoutine,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = FitMateBlue),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(text = "이 루틴 사용하기")
            }
            OutlinedButton(
                onClick = onRegenerate,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = FitMateBlue),
                border = androidx.compose.foundation.BorderStroke(1.dp, FitMateBlue),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(text = "다시 만들기")
            }
        }
    }
}
