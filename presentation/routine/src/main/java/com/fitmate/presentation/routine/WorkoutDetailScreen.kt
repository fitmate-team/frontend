package com.fitmate.presentation.routine

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.SportsGymnastics
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

private data class ExerciseDetail(
    val order: String,
    val name: String,
    val detail: String,
    val rest: String? = null,
    val aiRecommended: Boolean = false,
    val icon: ImageVector = Icons.Filled.FitnessCenter
)

private val exerciseDetails = listOf(
    ExerciseDetail("01", "벤치프레스", "40kg · 8회 × 4세트", "휴식 90초", aiRecommended = true),
    ExerciseDetail("02", "숄더프레스", "10kg · 10회 × 3세트", "휴식 60초"),
    ExerciseDetail("03", "체스트프레스", "30kg · 12회 × 3세트"),
    ExerciseDetail("04", "레터럴레이즈", "5kg · 15회 × 3세트"),
    ExerciseDetail("05", "러닝머신", "15분", icon = Icons.Filled.DirectionsRun)
)

@Composable
fun WorkoutDetailScreen(
    dayLabel: String = "월",
    onBack: () -> Unit = {},
    onStartWorkout: () -> Unit = {}
) {
    Column(modifier = Modifier.fillMaxSize().background(ScreenBackground)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(end = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack, modifier = Modifier.padding(4.dp)) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "뒤로")
                }
                Text(text = "오늘의 운동", fontWeight = FontWeight.Medium)
            }
            Surface(shape = RoundedCornerShape(100.dp), color = Color.White, border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)) {
                Row(modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Icon(imageVector = Icons.Filled.LocationOn, contentDescription = null, tint = TextMuted, modifier = Modifier.size(15.dp))
                    Text(text = "Fit Gym", color = TextMuted, fontSize = MaterialTheme.typography.labelSmall.fontSize)
                }
            }
        }

        Column(modifier = Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(horizontal = 20.dp)) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "상체 PUSH", fontSize = MaterialTheme.typography.headlineLarge.fontSize)
            Spacer(modifier = Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                InfoChip(icon = Icons.Filled.Schedule, text = "약 55분")
                InfoChip(icon = Icons.Filled.SportsGymnastics, text = "5종목")
                InfoChip(icon = Icons.Filled.Repeat, text = "16세트")
            }

            Spacer(modifier = Modifier.height(20.dp))
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                color = InfoBg,
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF9BC0DE))
            ) {
                Row(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Icon(imageVector = Icons.Filled.Psychology, contentDescription = null, tint = FitMateBlue)
                    Text(text = "오늘은 가슴과 어깨 중심의 상체 운동이에요.", color = Color(0xFF294767))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                exerciseDetails.forEach { exercise ->
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        color = Color.White,
                        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                    ) {
                        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Box(
                                    modifier = Modifier.size(36.dp).background(ScreenBackground, RoundedCornerShape(10.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(imageVector = exercise.icon, contentDescription = null, tint = TextMuted, modifier = Modifier.size(18.dp))
                                }
                                Text(text = exercise.order, color = TextMuted, fontSize = MaterialTheme.typography.labelSmall.fontSize)
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Text(text = exercise.name, fontWeight = FontWeight.Medium)
                                    if (exercise.aiRecommended) {
                                        Surface(shape = RoundedCornerShape(100.dp), color = FitMateBlue) {
                                            Text(text = "AI 추천", color = Color.White, fontSize = MaterialTheme.typography.labelSmall.fontSize, modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp))
                                        }
                                    }
                                }
                                Text(text = exercise.detail, color = TextMuted, fontSize = MaterialTheme.typography.bodySmall.fontSize)
                                exercise.rest?.let {
                                    Text(text = it, color = FitMateBlue, fontSize = MaterialTheme.typography.labelMedium.fontSize)
                                }
                            }
                            Icon(imageVector = Icons.Filled.ChevronRight, contentDescription = null, tint = CardBorder)
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        Column(modifier = Modifier.background(ScreenBackground).padding(20.dp)) {
            Button(
                onClick = onStartWorkout,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = FitMateBlue),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(text = "운동 시작")
            }
        }
    }
}

@Composable
private fun InfoChip(icon: ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(5.dp)) {
        Icon(imageVector = icon, contentDescription = null, tint = TextMuted, modifier = Modifier.size(16.dp))
        Text(text = text, color = TextMuted)
    }
}
