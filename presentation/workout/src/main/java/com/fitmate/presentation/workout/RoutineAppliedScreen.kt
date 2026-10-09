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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp

private val FitMateBlue = Color(0xFF3E78AD)
private val AdjustBlue = Color(0xFF42688A)
private val CardBorder = Color(0xFFE2E6EA)
private val TextMuted = Color(0xFF6C757D)
private val InfoBg = Color(0xFFE1ECF4)

@Composable
fun RoutineAppliedScreen(
    onGoHome: () -> Unit = {}
) {
    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF8F9FA))) {
        Column(modifier = Modifier.fillMaxWidth().background(Color.White).padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(modifier = Modifier.size(22.dp).background(FitMateBlue, CircleShape), contentAlignment = Alignment.Center) {
                    Icon(imageVector = Icons.Filled.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                }
                Text(text = "ROUTINE UPDATED", color = FitMateBlue, fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.labelSmall.fontSize)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "다음 운동에 반영했어요", fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.headlineSmall.fontSize)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "오늘의 운동 기록을 바탕으로\n다음 루틴을 조금 더 나에게 맞게 조정했어요.", color = TextMuted, fontWeight = FontWeight.Bold)
        }

        Column(modifier = Modifier.weight(1f).padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
            ) {
                Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 18.dp)) {
                    Text(text = "변경된 내용", color = TextMuted, fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.labelSmall.fontSize)

                    Spacer(modifier = Modifier.height(16.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Column {
                            Text(text = "벤치프레스", fontWeight = FontWeight.Bold)
                            Text(text = "40kg × 8회 × 4세트", color = TextMuted, fontSize = MaterialTheme.typography.labelMedium.fontSize)
                        }
                        Surface(shape = RoundedCornerShape(100.dp), color = Color(0xFFF8F9FA), border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)) {
                            Text(text = "유지", color = TextMuted, fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.labelSmall.fontSize, modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp))
                        }
                    }
                    Box(modifier = Modifier.fillMaxWidth().padding(vertical = 14.dp).height(1.dp).background(CardBorder))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Column {
                            Text(text = "숄더프레스", fontWeight = FontWeight.Bold)
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Text(text = "10kg", color = TextMuted, fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.labelMedium.fontSize, textDecoration = TextDecoration.LineThrough)
                                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = AdjustBlue, modifier = Modifier.size(14.dp))
                                Text(text = "12kg", color = AdjustBlue, fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.labelMedium.fontSize)
                            }
                        }
                        Surface(shape = RoundedCornerShape(100.dp), color = AdjustBlue) {
                            Text(text = "↑ 중량 증가", color = Color.White, fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.labelSmall.fontSize, modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp))
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
                    Text(text = "운동 기록이 쌓일수록 FitMate가 회원님에게 더 잘 맞는 운동을 제안할 수 있어요. 변경된 루틴은 다음주부터 적용돼요.", color = Color(0xFF27384A), fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.bodySmall.fontSize)
                }
            }
        }

        Column(modifier = Modifier.background(Color(0xFFF8F9FA)).padding(20.dp)) {
            Button(
                onClick = onGoHome,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = FitMateBlue),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(text = "홈으로")
            }
        }
    }
}
