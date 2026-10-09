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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private val FitMateBlue = Color(0xFF3E78AD)
private val ScreenBackground = Color(0xFFF8F9FA)
private val CardBorder = Color(0xFFE2E6EA)
private val TextMuted = Color(0xFF6C757D)
private val IconBg = Color(0xFFDBE9F5)
private val SundayRed = Color(0xFFC9675E)

private val allDays = listOf("월", "화", "수", "목", "금", "토", "일")

@Composable
fun ManualDaySelectionScreen(
    onBack: () -> Unit = {},
    onNext: (List<String>) -> Unit = {}
) {
    var selectedDays by remember { mutableStateOf(setOf("월", "화", "목", "토")) }

    Column(modifier = Modifier.fillMaxSize().background(ScreenBackground)) {
        IconButton(onClick = onBack, modifier = Modifier.padding(4.dp)) {
            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "뒤로")
        }

        Column(modifier = Modifier.weight(1f).padding(horizontal = 20.dp)) {
            Text(text = "어떤 요일에 운동하고 싶나요?", fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.headlineMedium.fontSize)
            Spacer(modifier = Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                allDays.forEach { day ->
                    val selected = day in selectedDays
                    Surface(
                        modifier = Modifier.weight(1f).height(52.dp),
                        shape = RoundedCornerShape(12.dp),
                        color = if (selected) IconBg else Color.White,
                        border = androidx.compose.foundation.BorderStroke(1.dp, if (selected) FitMateBlue else CardBorder),
                        onClick = {
                            selectedDays = if (selected) selectedDays - day else selectedDays + day
                        }
                    ) {
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                            Text(
                                text = day,
                                fontWeight = FontWeight.Bold,
                                color = when {
                                    selected -> FitMateBlue
                                    day == "일" -> SundayRed
                                    else -> TextMuted
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = FitMateBlue
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 20.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = "선택한 요일", color = Color(0xFF9BC0DE), fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.labelSmall.fontSize)
                        Text(text = "주 ${selectedDays.size}회", color = Color.White, fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.headlineSmall.fontSize)
                        Text(
                            text = allDays.filter { it in selectedDays }.joinToString(" · "),
                            color = Color(0xFF9BC0DE),
                            fontWeight = FontWeight.Bold,
                            fontSize = MaterialTheme.typography.bodyMedium.fontSize
                        )
                    }
                    Box(
                        modifier = Modifier.size(52.dp).background(Color.White.copy(alpha = 0.12f), RoundedCornerShape(14.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = Icons.Filled.CalendarMonth, contentDescription = null, tint = Color.White)
                    }
                }
            }
        }

        Column(modifier = Modifier.background(ScreenBackground).padding(20.dp)) {
            Button(
                onClick = { onNext(allDays.filter { it in selectedDays }) },
                modifier = Modifier.fillMaxWidth(),
                enabled = selectedDays.isNotEmpty(),
                colors = ButtonDefaults.buttonColors(containerColor = FitMateBlue),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(text = "운동 추가하기")
            }
        }
    }
}
