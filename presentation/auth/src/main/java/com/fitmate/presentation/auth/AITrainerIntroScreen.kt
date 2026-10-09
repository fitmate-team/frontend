package com.fitmate.presentation.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Blue700 = Color(0xFF3E78AD)
private val Blue200 = Color(0xFFDBE9F5)
private val Blue400 = Color(0xFF9BC0DE)
private val Blue900 = Color(0xFF294767)
private val Gray200 = Color(0xFFE2E6EA)
private val Gray300 = Color(0xFFA7AEB3)
private val Gray900 = Color(0xFF16181A)
private val ScreenBg = Color(0xFFF8F9FA)

@Composable
fun AITrainerIntroScreen(
    onNext: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().background(ScreenBg)) {
        Box(
            modifier = Modifier.fillMaxWidth().height(220.dp).background(Blue700),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(140.dp)
                    .background(Gray200, RoundedCornerShape(17.dp))
                    .border(BorderStroke(1.dp, Gray300), RoundedCornerShape(17.dp))
            )
        }

        Column(modifier = Modifier.weight(1f).padding(20.dp)) {
            Text(
                buildString { append("나에게 맞는 운동을\n시작해볼까요?") },
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Gray900,
                modifier = Modifier.padding(top = 32.dp)
            )
            Text(
                "몇 가지 정보를 알려주시면\nFitMate가 회원님에게 맞는\n운동 계획을 만들어드려요.",
                fontSize = 16.sp,
                color = Color(0xFF6C757D),
                modifier = Modifier.padding(top = 16.dp, bottom = 24.dp)
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Blue200, RoundedCornerShape(16.dp))
                    .border(BorderStroke(1.dp, Blue400), RoundedCornerShape(16.dp))
                    .padding(20.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier.size(36.dp).background(Blue700, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Psychology, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                    }
                    Text("FitMate AI Trainer", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Blue900, modifier = Modifier.padding(start = 10.dp))
                }
                Text(
                    "신체 정보, 운동 목표, 운동 환경과\n실제 운동 기록을 반영해\n나에게 맞는 루틴으로 계속 조정해요.",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Gray900,
                    modifier = Modifier.padding(top = 12.dp)
                )
                Row(modifier = Modifier.padding(top = 16.dp)) {
                    listOf("맞춤 루틴", "실시간 조정", "목표 최적화").forEach { label ->
                        Box(
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .background(Blue700, RoundedCornerShape(100.dp))
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(label, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }
        }

        Button(
            onClick = onNext,
            colors = ButtonDefaults.buttonColors(containerColor = Blue700),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp).padding(bottom = 24.dp).height(52.dp)
        ) {
            Text("시작하기", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}
