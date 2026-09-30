package com.fitmate.presentation.record

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private val FitMateBlue = Color(0xFF3E78AD)
private val AccentBlue = Color(0xFF42688A)
private val ScreenBackground = Color(0xFFF8F9FA)
private val CardBorder = Color(0xFFE2E6EA)
private val TextMuted = Color(0xFF6C757D)
private val InfoBg = Color(0xFFE1ECF4)

private data class SetRow(val order: String, val kg: String, val reps: String)
private data class ExerciseLog(val name: String, val baseWeight: String, val sets: List<SetRow>)

private val exerciseLogs = listOf(
    ExerciseLog("벤치프레스", "40kg", listOf(SetRow("01", "30kg", "8회"), SetRow("02", "40kg", "8회"), SetRow("03", "45kg", "8회"), SetRow("04", "45kg", "7회"))),
    ExerciseLog("숄더프레스", "12kg", listOf(SetRow("01", "12kg", "10회"), SetRow("02", "12kg", "10회"), SetRow("03", "12kg", "10회"))),
    ExerciseLog("체스트프레스", "30kg", listOf(SetRow("01", "30kg", "12회"), SetRow("02", "30kg", "12회"), SetRow("03", "30kg", "12회")))
)

@Composable
fun WorkoutRecordDetailScreen(
    dateLabel: String = "9월 3일 운동",
    onBack: () -> Unit = {},
    onViewGrowth: () -> Unit = {}
) {
    Column(modifier = Modifier.fillMaxSize().background(ScreenBackground)) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().background(Color.White)) {
            IconButton(onClick = onBack) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "뒤로")
            }
            Text(text = dateLabel, color = TextMuted, fontSize = MaterialTheme.typography.bodyMedium.fontSize)
        }
        Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(CardBorder))

        Column(modifier = Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(20.dp)) {
            Text(text = "상체 PUSH", fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.headlineSmall.fontSize)
            Text(text = "9월 3일 수요일", color = TextMuted, fontSize = MaterialTheme.typography.bodySmall.fontSize)

            Spacer(modifier = Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                StatColumn(value = "55분", label = "운동 시간")
                StatColumn(value = "5종목", label = "종목")
                StatColumn(value = "16세트", label = "세트")
                StatColumn(value = "92%", label = "수행률")
            }

            Spacer(modifier = Modifier.height(16.dp))
            Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(CardBorder))
            Spacer(modifier = Modifier.height(12.dp))
            Row {
                Text(text = "총 볼륨 ", color = TextMuted, fontSize = MaterialTheme.typography.bodyMedium.fontSize)
                Text(text = "4,820kg", fontWeight = FontWeight.Bold)
            }

            exerciseLogs.forEach { exercise ->
                Spacer(modifier = Modifier.height(20.dp))
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Box(modifier = Modifier.size(38.dp).background(InfoBg, RoundedCornerShape(10.dp)), contentAlignment = Alignment.Center) {
                        Icon(imageVector = Icons.Filled.FitnessCenter, contentDescription = null, tint = AccentBlue, modifier = Modifier.size(18.dp))
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = exercise.name, fontWeight = FontWeight.Bold)
                        Text(text = "기준 중량 ${exercise.baseWeight}", color = TextMuted, fontSize = MaterialTheme.typography.labelMedium.fontSize)
                    }
                    Text(text = "완료", color = AccentBlue, fontWeight = FontWeight.SemiBold, fontSize = MaterialTheme.typography.labelMedium.fontSize)
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp)) {
                    Text(text = "SET", color = TextMuted, fontSize = MaterialTheme.typography.labelSmall.fontSize, modifier = Modifier.width(32.dp))
                    Text(text = "KG", color = TextMuted, fontSize = MaterialTheme.typography.labelSmall.fontSize, modifier = Modifier.weight(1f))
                    Text(text = "REPS", color = TextMuted, fontSize = MaterialTheme.typography.labelSmall.fontSize, modifier = Modifier.weight(1f))
                }
                Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(CardBorder))
                exercise.sets.forEach { set ->
                    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 7.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text(text = set.order, color = TextMuted, fontSize = MaterialTheme.typography.bodySmall.fontSize, modifier = Modifier.width(32.dp))
                        Text(text = set.kg, fontSize = MaterialTheme.typography.bodyMedium.fontSize, modifier = Modifier.weight(1f))
                        Text(text = set.reps, fontSize = MaterialTheme.typography.bodyMedium.fontSize, modifier = Modifier.weight(1f))
                        Icon(imageVector = Icons.Filled.CheckCircle, contentDescription = "완료", tint = AccentBlue, modifier = Modifier.size(16.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), color = Color(0xFFF8F9FA), border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)) {
                Row(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(text = "🏋️", fontSize = MaterialTheme.typography.titleMedium.fontSize)
                    Column {
                        Text(text = "운동 모드", fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.labelMedium.fontSize)
                        Text(text = "거의 한계였어요", color = TextMuted, fontSize = MaterialTheme.typography.labelMedium.fontSize)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), color = InfoBg, border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFB0CBDC))) {
                Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Icon(imageVector = Icons.Filled.SmartToy, contentDescription = null, tint = AccentBlue)
                    Column {
                        Text(text = "AI 인사이트", color = AccentBlue, fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.labelMedium.fontSize)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "벤치프레스 마지막 세트에서 반복수가 감소했어요.", fontSize = MaterialTheme.typography.bodySmall.fontSize)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            OutlinedButton(
                onClick = onViewGrowth,
                modifier = Modifier.fillMaxWidth(),
                colors = androidx.compose.material3.ButtonDefaults.outlinedButtonColors(contentColor = AccentBlue),
                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(imageVector = Icons.AutoMirrored.Filled.TrendingUp, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "이날 한 운동의 성장 보기", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun StatColumn(value: String, label: String) {
    Column {
        Text(text = value, fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.titleMedium.fontSize)
        Text(text = label, color = TextMuted, fontSize = MaterialTheme.typography.labelSmall.fontSize)
    }
}
