package com.fitmate.presentation.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Blue700 = Color(0xFF3E78AD)
private val Blue900 = Color(0xFF294767)
private val Blue200 = Color(0xFFDBE9F5)
private val Blue400 = Color(0xFF9BC0DE)
private val Gray200 = Color(0xFFE2E6EA)
private val Gray500 = Color(0xFF6C757D)
private val Gray900 = Color(0xFF16181A)
private val ScreenBg = Color(0xFFF8F9FA)

private data class DaySession(val day: String, val icon: ImageVector, val title: String, val exercises: String, val duration: String)

@Composable
private fun DayCard(session: DaySession) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(16.dp))
            .border(BorderStroke(1.dp, Gray200), RoundedCornerShape(16.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier.size(44.dp).background(Blue200, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) { Icon(session.icon, contentDescription = null, tint = Blue700) }
            Text(session.day, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Blue700, modifier = Modifier.padding(top = 4.dp))
        }
        Column(modifier = Modifier.weight(1f).padding(start = 14.dp)) {
            Text(session.title, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Gray900)
            Text(session.exercises, fontSize = 12.sp, color = Gray500, modifier = Modifier.padding(top = 4.dp))
            Text("약 ${session.duration}", fontSize = 12.sp, color = Gray500, modifier = Modifier.padding(top = 4.dp))
        }
    }
}

@Composable
fun AIRoutineResultScreen(
    onWhy: () -> Unit,
    onUseRoutine: () -> Unit,
    onRegenerate: () -> Unit
) {
    val sessions = listOf(
        DaySession("월", Icons.Default.Upload, "상체 PUSH", "벤치프레스 · 숄더프레스 · 체스트프레스", "55분"),
        DaySession("화", Icons.AutoMirrored.Filled.DirectionsRun, "하체", "스쿼트 · 레그프레스 · 레그컬", "60분"),
        DaySession("목", Icons.Default.Download, "상체 PULL", "랫풀다운 · 시티드로우 · 바벨컬", "55분"),
        DaySession("토", Icons.AutoMirrored.Filled.DirectionsRun, "전신 + 유산소", "전신 운동 · 러닝 20분", "60분")
    )

    Column(modifier = Modifier.fillMaxSize().background(ScreenBg)) {
        Column(modifier = Modifier.fillMaxWidth().background(Blue700).padding(horizontal = 20.dp, vertical = 16.dp)) {
            Text("AI 생성 루틴", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Blue400)
            Text("이번 주 추천 루틴", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White, modifier = Modifier.padding(top = 4.dp))
        }

        LazyColumn(modifier = Modifier.weight(1f).padding(horizontal = 20.dp)) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp)
                        .background(Blue200, RoundedCornerShape(16.dp))
                        .border(BorderStroke(1.dp, Blue400), RoundedCornerShape(16.dp))
                        .padding(horizontal = 20.dp, vertical = 16.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row {
                            Box(modifier = Modifier.background(Blue700, RoundedCornerShape(100.dp)).padding(horizontal = 10.dp, vertical = 4.dp)) {
                                Text("체지방 감량", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                            Box(
                                modifier = Modifier
                                    .padding(start = 6.dp)
                                    .border(BorderStroke(1.dp, Blue700), RoundedCornerShape(100.dp))
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) { Text("균형형", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Blue700) }
                        }
                        Text("주 4회 · 회당 약 60분", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Blue900, modifier = Modifier.padding(top = 8.dp))
                        Text("근력 70% · 유산소 30%", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Blue700, modifier = Modifier.padding(top = 4.dp))
                    }
                }

                Column(modifier = Modifier.padding(top = 20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    sessions.forEach { DayCard(it) }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp, bottom = 20.dp)
                        .clickable(onClick = onWhy)
                        .background(Color.White, RoundedCornerShape(14.dp))
                        .border(BorderStroke(1.dp, Gray200), RoundedCornerShape(14.dp))
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Psychology, contentDescription = null, tint = Blue700)
                    Text("왜 이렇게 추천했나요?", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Gray900, modifier = Modifier.weight(1f).padding(start = 10.dp))
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = Gray500)
                }
            }
        }

        Column(modifier = Modifier.padding(horizontal = 20.dp).padding(bottom = 22.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Button(
                onClick = onUseRoutine,
                colors = ButtonDefaults.buttonColors(containerColor = Blue700),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth().height(52.dp)
            ) { Text("이 루틴 사용하기", fontSize = 16.sp, fontWeight = FontWeight.Bold) }

            OutlinedButton(
                onClick = onRegenerate,
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Blue700),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth().height(48.dp)
            ) { Text("다시 만들기", fontSize = 15.sp, fontWeight = FontWeight.Bold) }
        }
    }
}
