package com.fitmate.presentation.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Timer
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Blue700 = Color(0xFF3E78AD)
private val Blue200 = Color(0xFFDBE9F5)
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

private data class DurationOption(val label: String, val desc: String, val icon: ImageVector)

@Composable
fun WorkoutDurationScreen(
    onBack: () -> Unit,
    onNext: () -> Unit
) {
    val options = listOf(
        DurationOption("20분", "짧고 집중적인 운동", Icons.Default.Bolt),
        DurationOption("30분", "간결하게 핵심만", Icons.Default.Timer),
        DurationOption("45분", "표준적인 운동 시간", Icons.Default.Schedule),
        DurationOption("60분", "충분한 볼륨의 운동", Icons.Default.FitnessCenter),
        DurationOption("90분 이상", "고강도 집중 훈련", Icons.Default.EmojiEvents)
    )
    var selected by remember { mutableStateOf("60분") }

    Column(modifier = Modifier.fillMaxSize().background(ScreenBg)) {
        SignupHeader(step = 6, onBack = onBack)

        Column(modifier = Modifier.weight(1f).padding(20.dp)) {
            Text("한 번 운동할 때\n얼마나 시간을 쓸 수 있나요?", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Gray900)
            Text("가능한 시간에 맞춰 운동 수와 세트 수를 조절해요.", fontSize = 14.sp, color = Gray500, modifier = Modifier.padding(top = 8.dp, bottom = 24.dp))

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                options.forEach { option ->
                    val isSelected = selected == option.label
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selected = option.label }
                            .background(if (isSelected) Blue200 else Color.White, RoundedCornerShape(16.dp))
                            .border(BorderStroke(1.dp, if (isSelected) Blue700 else Gray200), RoundedCornerShape(16.dp))
                            .padding(horizontal = 20.dp, vertical = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier.size(52.dp).background(if (isSelected) Blue700 else ScreenBg, RoundedCornerShape(13.dp)),
                            contentAlignment = Alignment.Center
                        ) { Icon(option.icon, contentDescription = null, tint = if (isSelected) Color.White else Gray500) }
                        Column(modifier = Modifier.weight(1f).padding(start = 16.dp)) {
                            Text(option.label, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = if (isSelected) Blue700 else Gray900)
                            Text(option.desc, fontSize = 13.sp, color = Gray500)
                        }
                        if (isSelected) {
                            Box(modifier = Modifier.size(22.dp).background(Blue700, CircleShape), contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                            }
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
        ) { Text("다음", fontSize = 16.sp, fontWeight = FontWeight.Bold) }
    }
}
