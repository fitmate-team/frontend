package com.fitmate.presentation.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.SentimentSatisfied
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
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

private data class ExperienceOption(val title: String, val desc: String, val icon: ImageVector)

@Composable
fun ExerciseExperienceScreen(
    onBack: () -> Unit,
    onNext: () -> Unit
) {
    val options = listOf(
        ExperienceOption("처음이에요", "거의 운동해본 적이 없어요", Icons.Default.SentimentSatisfied),
        ExperienceOption("초급", "운동 경력 1년 미만", Icons.AutoMirrored.Filled.DirectionsWalk),
        ExperienceOption("중급", "운동 경력 1~3년", Icons.AutoMirrored.Filled.DirectionsRun),
        ExperienceOption("상급", "운동 경력 3년 이상", Icons.Default.FitnessCenter)
    )
    var selected by remember { mutableStateOf(options[0].title) }
    var status by remember { mutableStateOf<String?>(null) }

    Column(modifier = Modifier.fillMaxSize().background(ScreenBg)) {
        SignupHeader(step = 3, onBack = onBack)

        Column(modifier = Modifier.weight(1f).padding(20.dp)) {
            Text("운동 경험이 어느 정도인가요?", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Gray900, modifier = Modifier.padding(bottom = 20.dp))

            options.forEach { option ->
                val isSelected = selected == option.title
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                        .clickable { selected = option.title }
                        .background(Color.White, RoundedCornerShape(16.dp))
                        .border(BorderStroke(1.dp, if (isSelected) Blue700 else Gray200), RoundedCornerShape(16.dp))
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier.size(48.dp).background(ScreenBg, RoundedCornerShape(12.dp)).border(BorderStroke(1.dp, Gray200), RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) { Icon(option.icon, contentDescription = null, tint = Gray500) }
                    Column(modifier = Modifier.weight(1f).padding(start = 16.dp)) {
                        Text(option.title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Gray900)
                        Text(option.desc, fontSize = 13.sp, color = Gray500)
                    }
                    Box(
                        modifier = Modifier.size(22.dp).background(if (isSelected) Blue700 else Color.Transparent, CircleShape)
                            .border(BorderStroke(1.dp, if (isSelected) Blue700 else Gray200), CircleShape)
                    )
                }
            }

            Text("현재 운동 상태", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Gray900, modifier = Modifier.padding(top = 8.dp, bottom = 12.dp))
            Row {
                listOf("처음 시작", "꾸준히 운동 중", "다시 운동 시작").forEach { label ->
                    OutlinedButton(
                        onClick = { status = label },
                        colors = if (status == label) ButtonDefaults.outlinedButtonColors(containerColor = Color(0xFFDBE9F5)) else ButtonDefaults.outlinedButtonColors(),
                        shape = RoundedCornerShape(100.dp),
                        modifier = Modifier.padding(end = 8.dp)
                    ) { Text(label, fontSize = 14.sp) }
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
