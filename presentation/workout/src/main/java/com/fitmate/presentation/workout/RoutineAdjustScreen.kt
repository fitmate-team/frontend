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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private val FitMateBlue = Color(0xFF3E78AD)
private val AdjustBlue = Color(0xFF42688A)
private val CardBorder = Color(0xFFE2E6EA)
private val TextMuted = Color(0xFF6C757D)
private val InfoBg = Color(0xFFE1ECF4)

private data class Suggestion(val name: String, val badge: String, val current: String, val suggested: String, val reason: String)

private val suggestions = listOf(
    Suggestion("벤치프레스", "유지", "40kg × 8회 × 4세트", "40kg × 8회 × 4세트", "마지막 세트에서 목표 반복수를 채우지 못했고 체감 난이도가 높았어요. 현재 중량에 적응한 뒤 올리는 것을 추천해요."),
    Suggestion("숄더프레스", "↑ 중량 증가", "10kg × 10회", "12kg × 10회", "최근 2회 연속 모든 목표 반복수를 여유 있게 완료했어요.")
)

@Composable
fun RoutineAdjustScreen(
    onBack: () -> Unit = {},
    onApplySelected: (Set<String>) -> Unit = {}
) {
    var applied by remember { mutableStateOf(setOf("숄더프레스")) }

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF8F9FA))) {
        Column(modifier = Modifier.fillMaxWidth().background(FitMateBlue).padding(20.dp)) {
            IconButton(onClick = onBack) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "뒤로", tint = Color.White)
            }
            Text(text = "다음 운동을 조금\n조정해볼까요?", color = Color.White, fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.headlineSmall.fontSize)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "오늘의 수행 기록과 세션 난이도를\n바탕으로 제안했어요.", color = Color(0xFFB0CBDC), fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.bodySmall.fontSize)
        }

        Column(modifier = Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            suggestions.forEach { s ->
                val isApplied = s.name in applied
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White,
                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text(text = s.name, fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.titleMedium.fontSize)
                            Surface(
                                shape = RoundedCornerShape(100.dp),
                                color = if (s.badge == "유지") Color(0xFFF8F9FA) else AdjustBlue,
                                border = if (s.badge == "유지") androidx.compose.foundation.BorderStroke(1.dp, CardBorder) else null
                            ) {
                                Text(text = s.badge, color = if (s.badge == "유지") TextMuted else Color.White, fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.labelSmall.fontSize, modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp))
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                            Surface(modifier = Modifier.weight(1f), shape = RoundedCornerShape(10.dp), color = Color(0xFFF8F9FA)) {
                                Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)) {
                                    Text(text = "현재", color = TextMuted, fontSize = MaterialTheme.typography.labelSmall.fontSize)
                                    Text(text = s.current, fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.labelMedium.fontSize)
                                }
                            }
                            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = CardBorder)
                            Surface(modifier = Modifier.weight(1f), shape = RoundedCornerShape(10.dp), color = InfoBg) {
                                Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)) {
                                    Text(text = "AI 제안", color = AdjustBlue, fontSize = MaterialTheme.typography.labelSmall.fontSize)
                                    Text(text = s.suggested, color = Color(0xFF27384A), fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.labelMedium.fontSize)
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(text = s.reason, color = TextMuted, fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.bodySmall.fontSize)
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Surface(
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp),
                                color = if (isApplied) AdjustBlue else Color(0xFFF8F9FA),
                                border = androidx.compose.foundation.BorderStroke(1.dp, if (isApplied) AdjustBlue else CardBorder),
                                onClick = { applied = applied + s.name }
                            ) {
                                Text(
                                    text = if (isApplied) "✓ 적용" else "적용",
                                    color = if (isApplied) Color.White else Color(0xFF16181A),
                                    fontWeight = FontWeight.Bold,
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                    modifier = Modifier.padding(vertical = 12.dp).fillMaxWidth()
                                )
                            }
                            Surface(
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp),
                                color = Color.White,
                                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
                                onClick = { applied = applied - s.name }
                            ) {
                                Text(text = "그대로 유지", color = TextMuted, fontWeight = FontWeight.Bold, textAlign = androidx.compose.ui.text.style.TextAlign.Center, modifier = Modifier.padding(vertical = 12.dp).fillMaxWidth())
                            }
                        }
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
                    Icon(imageVector = Icons.Filled.AutoAwesome, contentDescription = null, tint = AdjustBlue, modifier = Modifier.size(18.dp))
                    Text(text = "운동 기록이 쌓일수록 FitMate가 회원님에게 더 잘 맞는 운동을 제안할 수 있어요.", color = Color(0xFF27384A), fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.bodySmall.fontSize)
                }
            }
        }

        Column(modifier = Modifier.background(Color(0xFFF8F9FA)).padding(20.dp)) {
            Button(
                onClick = { onApplySelected(applied) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = FitMateBlue),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(text = "선택한 변경사항 적용")
            }
        }
    }
}
