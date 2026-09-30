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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Schedule
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private val FitMateBlue = Color(0xFF3E78AD)
private val ScreenBackground = Color(0xFFF8F9FA)
private val CardBorder = Color(0xFFE2E6EA)
private val TextMuted = Color(0xFF6C757D)
private val InfoBg = Color(0xFFDBE9F5)
private val InfoText = Color(0xFF294767)

private data class SetupRow(val icon: ImageVector, val label: String, val value: String)

private val setupRows = listOf(
    SetupRow(Icons.Filled.Flag, "목표", "체지방 감량 · 균형형"),
    SetupRow(Icons.Filled.CalendarMonth, "빈도", "주 4회"),
    SetupRow(Icons.Filled.Event, "요일", "월 · 화 · 목 · 토"),
    SetupRow(Icons.Filled.Schedule, "운동 시간", "60분"),
    SetupRow(Icons.Filled.LocationOn, "운동 장소", "Fit Gym"),
    SetupRow(Icons.Filled.FitnessCenter, "기구", "바벨, 덤벨, 스미스머신 외 8개")
)

@Composable
fun AiHandsOffSetupScreen(
    onBack: () -> Unit = {},
    onGenerate: () -> Unit = {}
) {
    Column(modifier = Modifier.fillMaxSize().background(ScreenBackground)) {
        IconButton(onClick = onBack, modifier = Modifier.padding(4.dp)) {
            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "뒤로")
        }

        Column(modifier = Modifier.weight(1f).padding(horizontal = 20.dp)) {
            Text(
                text = "이번 루틴은 어떻게 만들까요?",
                fontWeight = FontWeight.Bold,
                fontSize = MaterialTheme.typography.headlineMedium.fontSize
            )

            Spacer(modifier = Modifier.height(28.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
            ) {
                Column {
                    setupRows.forEachIndexed { index, row ->
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(imageVector = row.icon, contentDescription = null, tint = FitMateBlue, modifier = Modifier.size(18.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = row.label, color = TextMuted, fontSize = MaterialTheme.typography.labelSmall.fontSize)
                                Text(text = row.value, fontWeight = FontWeight.Medium)
                            }
                            Icon(imageVector = Icons.Filled.Edit, contentDescription = "수정", tint = TextMuted, modifier = Modifier.size(18.dp))
                        }
                        if (index < setupRows.lastIndex) {
                            androidx.compose.foundation.layout.Box(
                                modifier = Modifier.fillMaxWidth().height(1.dp).background(CardBorder)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                color = InfoBg,
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF9BC0DE))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(imageVector = Icons.Filled.Psychology, contentDescription = null, tint = FitMateBlue, modifier = Modifier.size(18.dp))
                    Text(text = "현재 설정을 기준으로\nFitMate가 주간 루틴을 다시 구성해요.", color = InfoText, fontSize = MaterialTheme.typography.bodySmall.fontSize)
                }
            }
        }

        Column(modifier = Modifier.background(ScreenBackground).padding(20.dp)) {
            Button(
                onClick = onGenerate,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = FitMateBlue),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(text = "AI 루틴 만들기")
            }
        }
    }
}
