package com.fitmate.presentation.routine

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private val FitMateBlue = Color(0xFF3E78AD)
private val ScreenBackground = Color(0xFFF8F9FA)
private val CardBorder = Color(0xFFE2E6EA)
private val TextMuted = Color(0xFF6C757D)
private val InfoBg = Color(0xFFDBE9F5)

private val restOptions = listOf("30초", "60초", "90초", "2분", "3분")

@Composable
fun ExerciseConfigurationScreen(
    exercises: List<String> = listOf("벤치프레스", "인클라인 덤벨프레스", "체스트프레스", "숄더프레스"),
    onBack: () -> Unit = {},
    onSave: () -> Unit = {}
) {
    var selectedExercise by remember { mutableStateOf(exercises.firstOrNull() ?: "벤치프레스") }
    var sets by remember { mutableIntStateOf(4) }
    var reps by remember { mutableIntStateOf(10) }
    var weight by remember { mutableIntStateOf(40) }
    var restIndex by remember { mutableStateOf(2) }

    Column(modifier = Modifier.fillMaxSize().background(ScreenBackground)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack, modifier = Modifier.padding(4.dp)) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "뒤로")
            }
            Text(text = "운동 설정", fontWeight = FontWeight.Medium, fontSize = MaterialTheme.typography.titleMedium.fontSize)
        }

        Column(modifier = Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(horizontal = 20.dp)) {
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                exercises.forEach { exercise ->
                    val selected = exercise == selectedExercise
                    Surface(
                        shape = RoundedCornerShape(100.dp),
                        color = if (selected) InfoBg else Color.White,
                        border = androidx.compose.foundation.BorderStroke(1.dp, if (selected) FitMateBlue else CardBorder),
                        onClick = { selectedExercise = exercise }
                    ) {
                        Text(
                            text = exercise,
                            color = if (selected) FitMateBlue else TextMuted,
                            fontWeight = if (selected) FontWeight.Medium else FontWeight.Normal,
                            fontSize = MaterialTheme.typography.labelMedium.fontSize,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
            ) {
                Row(modifier = Modifier.padding(16.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    NumberStepper(label = "세트", suffix = "세트", value = sets, onChange = { sets = it.coerceAtLeast(1) })
                    NumberStepper(label = "횟수", suffix = "회", value = reps, onChange = { reps = it.coerceAtLeast(1) })
                    NumberStepper(label = "중량", suffix = "kg", value = weight, onChange = { weight = it.coerceAtLeast(0) })
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "휴식 시간", color = TextMuted, fontWeight = FontWeight.Medium, fontSize = MaterialTheme.typography.labelMedium.fontSize)
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        restOptions.forEachIndexed { index, option ->
                            val selected = index == restIndex
                            Surface(
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp),
                                color = if (selected) InfoBg else ScreenBackground,
                                border = androidx.compose.foundation.BorderStroke(1.dp, if (selected) FitMateBlue else CardBorder),
                                onClick = { restIndex = index }
                            ) {
                                Box(contentAlignment = Alignment.Center, modifier = Modifier.height(36.dp).fillMaxWidth()) {
                                    Text(text = option, color = if (selected) FitMateBlue else TextMuted, fontSize = MaterialTheme.typography.labelMedium.fontSize)
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                color = InfoBg,
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF9BC0DE))
            ) {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp)) {
                    Text(text = "AI 추천 — $selectedExercise", color = Color(0xFF294767), fontWeight = FontWeight.Medium, fontSize = MaterialTheme.typography.labelMedium.fontSize)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "4세트 · 8회 · 42.5kg · 휴식 90초", color = Color(0xFF27384A), fontSize = MaterialTheme.typography.bodySmall.fontSize)
                    Text(text = "최근 4회 평균 성공률 94%를 기준으로 추천해요.", color = TextMuted, fontSize = MaterialTheme.typography.labelMedium.fontSize)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        Column(modifier = Modifier.background(ScreenBackground).padding(20.dp)) {
            Button(
                onClick = onSave,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = FitMateBlue),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(text = "저장하기")
            }
        }
    }
}

@Composable
private fun NumberStepper(label: String, suffix: String, value: Int, onChange: (Int) -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(text = label, color = TextMuted, fontSize = MaterialTheme.typography.labelSmall.fontSize)
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            StepButton(icon = Icons.Filled.Remove, onClick = { onChange(value - 1) })
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "$value", fontWeight = FontWeight.Medium, fontSize = MaterialTheme.typography.titleMedium.fontSize)
                Text(text = suffix, color = TextMuted, fontSize = MaterialTheme.typography.labelSmall.fontSize)
            }
            StepButton(icon = Icons.Filled.Add, onClick = { onChange(value + 1) })
        }
    }
}

@Composable
private fun StepButton(icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {
    Surface(
        modifier = Modifier.size(28.dp),
        shape = RoundedCornerShape(7.dp),
        color = Color.White,
        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
        onClick = onClick
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Icon(imageVector = icon, contentDescription = null, modifier = Modifier.size(16.dp))
        }
    }
}
