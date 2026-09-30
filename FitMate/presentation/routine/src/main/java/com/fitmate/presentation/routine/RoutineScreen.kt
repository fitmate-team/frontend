package com.fitmate.presentation.routine

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bed
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private val FitMateBlue = Color(0xFF3E78AD)
private val ScreenBackground = Color(0xFFF8F9FA)
private val CardBorder = Color(0xFFE2E6EA)
private val TextMuted = Color(0xFF6C757D)
private val IconBg = Color(0xFFDBE9F5)
private val IconBorder = Color(0xFF9BC0DE)

private data class DayPlan(
    val label: String,
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val isRest: Boolean = false
)

private val weekPlan = listOf(
    DayPlan("월", "상체 PUSH", "5종목 · 약 55분", Icons.Filled.Upload),
    DayPlan("화", "하체", "5종목 · 약 60분", Icons.Filled.DirectionsWalk),
    DayPlan("수", "휴식", "", Icons.Filled.Bed, isRest = true),
    DayPlan("목", "상체 PULL", "5종목 · 약 55분", Icons.Filled.Download),
    DayPlan("금", "휴식", "", Icons.Filled.Bed, isRest = true),
    DayPlan("토", "전신 + 유산소", "6종목 · 약 60분", Icons.Filled.DirectionsRun),
    DayPlan("일", "휴식", "", Icons.Filled.Bed, isRest = true)
)

@Composable
fun RoutineScreen(
    hasRoutine: Boolean = true,
    onCreateRoutine: () -> Unit = {},
    onOpenDay: (String) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ScreenBackground)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(52.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "나의 루틴", fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.headlineSmall.fontSize)

            if (hasRoutine) {
                Surface(
                    shape = RoundedCornerShape(100.dp),
                    color = FitMateBlue,
                    onClick = onCreateRoutine
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(imageVector = Icons.Filled.Add, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                        Text(text = "루틴 새로 생성하기", color = Color.White, fontSize = MaterialTheme.typography.labelMedium.fontSize)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            weekPlan.forEachIndexed { index, day ->
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(text = day.label, fontSize = MaterialTheme.typography.labelSmall.fontSize, color = if (index == 0 && hasRoutine) FitMateBlue else TextMuted)
                    if (index == 0 && hasRoutine) {
                        Box(
                            modifier = Modifier.size(32.dp).clip(RoundedCornerShape(10.dp)).background(FitMateBlue),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "✓", color = Color.White)
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .border(1.dp, CardBorder, RoundedCornerShape(10.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "${index + 1}", fontSize = MaterialTheme.typography.labelSmall.fontSize, color = TextMuted)
                        }
                    }
                }
            }
        }

        if (hasRoutine) {
            Spacer(modifier = Modifier.height(20.dp))
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                weekPlan.forEach { day ->
                    if (day.isRest) {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            color = ScreenBackground,
                            border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                Box(
                                    modifier = Modifier.size(40.dp).clip(RoundedCornerShape(10.dp)).background(ScreenBackground).border(1.dp, CardBorder, RoundedCornerShape(10.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(imageVector = day.icon, contentDescription = null, tint = TextMuted, modifier = Modifier.size(20.dp))
                                }
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Text(text = day.label, fontSize = MaterialTheme.typography.labelSmall.fontSize, color = TextMuted)
                                    Text(text = day.title, color = TextMuted)
                                }
                            }
                        }
                    } else {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            color = Color.White,
                            border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
                            onClick = { onOpenDay(day.label) }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                Box(
                                    modifier = Modifier.size(40.dp).clip(RoundedCornerShape(10.dp)).background(IconBg).border(1.dp, IconBorder, RoundedCornerShape(10.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(imageVector = day.icon, contentDescription = null, tint = FitMateBlue, modifier = Modifier.size(20.dp))
                                }
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                                        Text(text = day.label, fontSize = MaterialTheme.typography.labelSmall.fontSize, color = TextMuted)
                                        Text(text = day.title, fontWeight = FontWeight.Medium)
                                    }
                                    Text(text = day.subtitle, color = TextMuted, fontSize = MaterialTheme.typography.bodySmall.fontSize)
                                }
                                Icon(imageVector = Icons.Filled.ChevronRight, contentDescription = null, tint = CardBorder)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                color = IconBg,
                border = androidx.compose.foundation.BorderStroke(1.dp, IconBorder)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(imageVector = Icons.Filled.AutoAwesome, contentDescription = null, tint = FitMateBlue, modifier = Modifier.size(18.dp))
                    Text(text = "이번 주 운동 구성이 균형적이에요.", color = FitMateBlue, fontWeight = FontWeight.Medium, fontSize = MaterialTheme.typography.labelLarge.fontSize)
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        } else {
            Column(
                modifier = Modifier.fillMaxWidth().padding(top = 160.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "생성된 루틴이 없어요! 루틴을 생성하러 가볼까요?",
                    color = TextMuted,
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
                Button(
                    onClick = onCreateRoutine,
                    colors = ButtonDefaults.buttonColors(containerColor = FitMateBlue),
                    shape = RoundedCornerShape(100.dp)
                ) {
                    Icon(imageVector = Icons.Filled.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "루틴 생성하기")
                }
                Spacer(modifier = Modifier.height(160.dp))
            }
        }
    }
}
