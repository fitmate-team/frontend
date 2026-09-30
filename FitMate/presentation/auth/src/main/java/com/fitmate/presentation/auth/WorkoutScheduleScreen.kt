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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.unit.sp

private val Blue700 = Color(0xFF3E78AD)
private val Blue400 = Color(0xFF9BC0DE)
private val Gray200 = Color(0xFFE2E6EA)
private val Gray500 = Color(0xFF6C757D)
private val Gray900 = Color(0xFF16181A)
private val ScreenBg = Color(0xFFF8F9FA)

@Composable
private fun SignupHeader(step: Int, onBack: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth().height(80.dp).padding(horizontal = 20.dp), verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "뒤로", tint = Gray900) }
        Box(modifier = Modifier.weight(1f).padding(horizontal = 8.dp).height(4.dp).background(Gray200, RoundedCornerShape(2.dp))) {
            Box(modifier = Modifier.fillMaxWidth(step / 8f).height(4.dp).background(Blue700, RoundedCornerShape(2.dp)))
        }
        Text("$step / 8", fontSize = 12.sp, color = Gray500, fontWeight = FontWeight.Bold)
    }
}

private val DayLabels = listOf("월", "화", "수", "목", "금", "토", "일")

@Composable
fun WorkoutScheduleScreen(
    onBack: () -> Unit,
    onNext: () -> Unit
) {
    var selectedDays by remember { mutableStateOf(setOf("월", "화", "목", "토")) }

    Column(modifier = Modifier.fillMaxSize().background(ScreenBg)) {
        SignupHeader(step = 5, onBack = onBack)

        Column(modifier = Modifier.weight(1f).padding(20.dp)) {
            Text("언제 운동할 수 있나요?", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Gray900, modifier = Modifier.padding(bottom = 24.dp))

            Text("일주일에 몇 번 운동할까요?", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Gray500)
            Row(modifier = Modifier.fillMaxWidth().padding(top = 16.dp), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                (1..7).forEach { count ->
                    val isSelected = selectedDays.size == count
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp)
                            .background(if (isSelected) Blue700 else Color.White, RoundedCornerShape(12.dp))
                            .border(BorderStroke(1.dp, if (isSelected) Blue700 else Gray200), RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) { Text("$count", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = if (isSelected) Color.White else Gray500) }
                }
            }
            Text("주 ${selectedDays.size}회", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Gray500, modifier = Modifier.fillMaxWidth().padding(top = 8.dp), textAlign = androidx.compose.ui.text.style.TextAlign.End)

            Text("운동 가능한 요일", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Gray900, modifier = Modifier.padding(top = 32.dp, bottom = 16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                DayLabels.forEach { day ->
                    val isSelected = day in selectedDays
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp)
                            .clickable {
                                selectedDays = if (isSelected) selectedDays - day else selectedDays + day
                            }
                            .background(if (isSelected) Blue200Local else Color.White, RoundedCornerShape(12.dp))
                            .border(BorderStroke(1.dp, if (isSelected) Blue700 else Gray200), RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) { Text(day, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = if (isSelected) Blue700 else Gray500) }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp)
                    .background(Blue700, RoundedCornerShape(16.dp))
                    .padding(horizontal = 24.dp, vertical = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("선택한 일정", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Blue400)
                    Text("주 ${selectedDays.size}회", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.White, modifier = Modifier.padding(top = 6.dp))
                    Text(DayLabels.filter { it in selectedDays }.joinToString(" · "), fontSize = 14.sp, color = Blue400, modifier = Modifier.padding(top = 4.dp))
                }
                Box(
                    modifier = Modifier.size(52.dp).background(Color.White.copy(alpha = 0.12f), RoundedCornerShape(14.dp)),
                    contentAlignment = Alignment.Center
                ) { Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = Color.White) }
            }
        }

        Button(
            onClick = onNext,
            colors = ButtonDefaults.buttonColors(containerColor = Blue700),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp).padding(bottom = 24.dp).height(52.dp)
        ) { Text("다음", fontSize = 16.sp, fontWeight = FontWeight.Bold) }
    }
}

private val Blue200Local = Color(0xFFDBE9F5)
