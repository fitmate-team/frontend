package com.fitmate.presentation.record

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

private val FitMateBlue = Color(0xFF3E78AD)
private val AccentBlue = Color(0xFF42688A)
private val ScreenBackground = Color(0xFFF8F9FA)
private val CardBorder = Color(0xFFE2E6EA)
private val TextMuted = Color(0xFF6C757D)

private data class WorkoutSummary(val dateLabel: String, val title: String, val detail: String, val exercises: String? = null)

private val markedDays = setOf(1, 3, 8, 10, 15, 17, 22, 24, 27, 29, 30)
private const val LeadingBlankDays = 2 // 2026년 9월 1일은 화요일

private val recentWorkouts = listOf(
    WorkoutSummary("9월 3일 · 수", "상체 PUSH", "52분 · 5종목 · 16세트", "벤치프레스 40kg · 숄더프레스 12kg"),
    WorkoutSummary("9월 1일 · 월", "하체", "61분 · 5종목 · 18세트"),
    WorkoutSummary("8월 30일 · 토", "상체 PULL", "48분 · 5종목 · 15세트")
)

@Composable
fun RecordScreen(
    onOpenWorkoutDetail: (String) -> Unit = {}
) {
    var isCalendarTab by remember { mutableStateOf(true) }

    Column(modifier = Modifier.fillMaxSize().background(ScreenBackground)) {
        Column(modifier = Modifier.fillMaxWidth().background(Color.White).padding(horizontal = 20.dp, vertical = 16.dp)) {
            Text(text = "운동 기록", fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.titleLarge.fontSize)
            Spacer(modifier = Modifier.height(12.dp))
            Surface(shape = RoundedCornerShape(8.dp), color = ScreenBackground) {
                Row(modifier = Modifier.padding(3.dp)) {
                    TabPill(modifier = Modifier.weight(1f), text = "캘린더", selected = isCalendarTab, onClick = { isCalendarTab = true })
                    TabPill(modifier = Modifier.weight(1f), text = "목록", selected = !isCalendarTab, onClick = { isCalendarTab = false })
                }
            }
        }

        Column(modifier = Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(horizontal = 16.dp, vertical = 12.dp)) {
            if (isCalendarTab) {
                MiniCalendar()
                Spacer(modifier = Modifier.height(20.dp))
            } else {
                Spacer(modifier = Modifier.height(4.dp))
            }

            Column(modifier = Modifier.padding(horizontal = 4.dp)) {
                Text(text = "이번 달", color = TextMuted, fontSize = MaterialTheme.typography.bodySmall.fontSize)
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(text = "12회", fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.headlineMedium.fontSize)
                    Text(text = "운동 · 총 9시간 40분", color = TextMuted, fontSize = MaterialTheme.typography.bodyMedium.fontSize, modifier = Modifier.padding(bottom = 6.dp))
                }
            }

            if (!isCalendarTab) {
                Spacer(modifier = Modifier.height(16.dp))
                Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(CardBorder))
            }

            Spacer(modifier = Modifier.height(20.dp))
            Text(text = "최근 운동", fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.titleMedium.fontSize)
            Spacer(modifier = Modifier.height(8.dp))

            Column {
                recentWorkouts.forEachIndexed { index, workout ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onOpenWorkoutDetail(workout.dateLabel) }
                            .padding(vertical = 14.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(modifier = Modifier.padding(top = 3.dp).size(8.dp).background(AccentBlue, CircleShape))
                            if (index < recentWorkouts.lastIndex) {
                                Box(modifier = Modifier.padding(top = 4.dp).width(1.dp).height(32.dp).background(CardBorder))
                            }
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = workout.dateLabel, color = TextMuted, fontSize = MaterialTheme.typography.labelMedium.fontSize)
                            Text(text = workout.title, fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.bodyLarge.fontSize)
                            Text(text = workout.detail, color = TextMuted, fontSize = MaterialTheme.typography.labelMedium.fontSize)
                            workout.exercises?.let {
                                Text(text = it, color = TextMuted, fontSize = MaterialTheme.typography.labelSmall.fontSize)
                            }
                        }
                        Icon(imageVector = Icons.Filled.ChevronRight, contentDescription = null, tint = TextMuted, modifier = Modifier.padding(top = 2.dp))
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun TabPill(modifier: Modifier = Modifier, text: String, selected: Boolean, onClick: () -> Unit) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(6.dp),
        color = if (selected) FitMateBlue else Color.Transparent,
        onClick = onClick
    ) {
        Text(
            text = text,
            color = if (selected) Color.White else TextMuted,
            fontWeight = FontWeight.SemiBold,
            fontSize = MaterialTheme.typography.labelLarge.fontSize,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 22.dp, vertical = 8.dp).fillMaxWidth()
        )
    }
}

@Composable
private fun MiniCalendar() {
    val cells: List<Int?> = List(LeadingBlankDays) { null } + (1..30).map { it }
    val paddedCells = cells + List((7 - cells.size % 7) % 7) { null }
    val weeks = paddedCells.chunked(7)

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Filled.ChevronLeft, contentDescription = "이전 달", tint = TextMuted)
                Text(text = "2026년 9월", fontWeight = FontWeight.Bold)
                Icon(imageVector = Icons.Filled.ChevronRight, contentDescription = "다음 달", tint = TextMuted)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                listOf("일" to Color(0xFFE05C5C), "월" to TextMuted, "화" to TextMuted, "수" to TextMuted, "목" to TextMuted, "금" to TextMuted, "토" to Color(0xFF5C7BE0)).forEach { (d, c) ->
                    Text(text = d, color = c, fontWeight = FontWeight.SemiBold, fontSize = MaterialTheme.typography.labelSmall.fontSize, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            weeks.forEach { week ->
                Row(modifier = Modifier.fillMaxWidth()) {
                    week.forEach { day ->
                        Column(
                            modifier = Modifier.weight(1f).padding(vertical = 4.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            if (day != null) {
                                val isSelected = day == 3
                                Box(
                                    modifier = Modifier.size(30.dp).background(if (isSelected) AccentBlue else Color.Transparent, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = "$day", color = if (isSelected) Color.White else Color(0xFF16181A), fontSize = MaterialTheme.typography.labelMedium.fontSize)
                                }
                                if (day in markedDays) {
                                    Box(modifier = Modifier.padding(top = 2.dp).size(5.dp).background(if (isSelected) Color(0xFFB0CBDC) else AccentBlue, CircleShape))
                                } else {
                                    Spacer(modifier = Modifier.height(7.dp))
                                }
                            } else {
                                Spacer(modifier = Modifier.size(30.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}
