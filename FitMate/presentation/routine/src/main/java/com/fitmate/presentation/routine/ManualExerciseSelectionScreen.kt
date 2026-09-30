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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private val FitMateBlue = Color(0xFF3E78AD)
private val ScreenBackground = Color(0xFFF8F9FA)
private val CardBorder = Color(0xFFE2E6EA)
private val TextMuted = Color(0xFF6C757D)
private val IconBg = Color(0xFFDBE9F5)

private data class ExerciseOption(val name: String, val category: String)

private val bodyParts = listOf("전체", "가슴", "등", "하체", "어깨", "팔", "코어", "유산소")

private val exercisePool = listOf(
    ExerciseOption("벤치프레스", "가슴 · 프리웨이트"),
    ExerciseOption("체스트프레스", "가슴 · 머신"),
    ExerciseOption("인클라인 덤벨프레스", "가슴 · 프리웨이트"),
    ExerciseOption("숄더프레스", "어깨 · 프리웨이트"),
    ExerciseOption("랫풀다운", "등 · 머신"),
    ExerciseOption("스쿼트", "하체 · 프리웨이트"),
    ExerciseOption("레그프레스", "하체 · 머신")
)

@Composable
fun ManualExerciseSelectionScreen(
    dayLabel: String = "월",
    onBack: () -> Unit = {},
    onNext: (List<String>) -> Unit = {}
) {
    var selectedExercises by remember {
        mutableStateOf(setOf("벤치프레스", "체스트프레스", "인클라인 덤벨프레스", "숄더프레스"))
    }

    Column(modifier = Modifier.fillMaxSize().background(ScreenBackground)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack, modifier = Modifier.padding(4.dp)) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "뒤로")
            }
            Text(text = "${dayLabel}요일 루틴", fontWeight = FontWeight.Medium, fontSize = MaterialTheme.typography.titleMedium.fontSize)
        }

        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            Spacer(modifier = Modifier.height(24.dp))
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = Color.White,
                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
            ) {
                Row(modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(imageVector = Icons.Filled.Search, contentDescription = null, tint = TextMuted)
                    Text(text = "운동 검색", color = TextMuted)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                bodyParts.forEachIndexed { index, part ->
                    Surface(
                        shape = RoundedCornerShape(100.dp),
                        color = if (index == 0) IconBg else Color.White,
                        border = androidx.compose.foundation.BorderStroke(1.dp, if (index == 0) FitMateBlue else CardBorder)
                    ) {
                        Text(
                            text = part,
                            color = if (index == 0) FitMateBlue else TextMuted,
                            fontWeight = if (index == 0) FontWeight.Medium else FontWeight.Normal,
                            fontSize = MaterialTheme.typography.labelMedium.fontSize,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                        )
                    }
                }
            }
        }

        Column(modifier = Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(horizontal = 20.dp, vertical = 12.dp)) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                exercisePool.forEach { exercise ->
                    val selected = exercise.name in selectedExercises
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        color = if (selected) IconBg else Color.White,
                        border = androidx.compose.foundation.BorderStroke(1.dp, if (selected) FitMateBlue else CardBorder),
                        onClick = {
                            selectedExercises = if (selected) selectedExercises - exercise.name else selectedExercises + exercise.name
                        }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = exercise.name, color = if (selected) FitMateBlue else Color(0xFF16181A), fontWeight = FontWeight.Medium)
                                Text(text = exercise.category, color = TextMuted, fontSize = MaterialTheme.typography.labelMedium.fontSize)
                            }
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (selected) FitMateBlue else ScreenBackground),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (selected) Icons.Filled.Check else Icons.Filled.Add,
                                    contentDescription = null,
                                    tint = if (selected) Color.White else TextMuted
                                )
                            }
                        }
                    }
                }
            }
        }

        Column(modifier = Modifier.background(ScreenBackground).padding(20.dp)) {
            Button(
                onClick = { onNext(selectedExercises.toList()) },
                modifier = Modifier.fillMaxWidth(),
                enabled = selectedExercises.isNotEmpty(),
                colors = ButtonDefaults.buttonColors(containerColor = FitMateBlue),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(text = "선택한 운동 설정하기 (${selectedExercises.size})")
            }
        }
    }
}
