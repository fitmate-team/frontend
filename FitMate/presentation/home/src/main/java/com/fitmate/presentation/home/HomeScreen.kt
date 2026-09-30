package com.fitmate.presentation.home

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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Watch
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private val FitMateBlue = Color(0xFF3E78AD)
private val FitMateYellow = Color(0xFFFFE066)
private val ScreenBackground = Color(0xFFF8F9FA)
private val CardBorder = Color(0xFFE2E6EA)
private val TextMuted = Color(0xFF6C757D)
private val TextFaint = Color(0xFFA7AEB3)
private val ChipBackground = Color(0xFFEDF0F2)
private val CheckGreen = Color(0xFF428A54)

private val weekDayLabels = listOf("월", "화", "수", "목", "금", "토", "일")

@Composable
fun HomeScreen(
    userName: String? = "도연",
    onStartWorkout: () -> Unit = {},
    onNavigateToWatchSync: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ScreenBackground)
            .verticalScroll(rememberScrollState())
    ) {
        HeroSection(userName = userName)

        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            Spacer(modifier = Modifier.height(24.dp))

            SectionLabel(text = "오늘의 운동")
            Spacer(modifier = Modifier.height(12.dp))
            TodayWorkoutCard(onStartWorkout = onStartWorkout)

            Spacer(modifier = Modifier.height(12.dp))
            WatchConnectCard(onClick = onNavigateToWatchSync)

            Spacer(modifier = Modifier.height(24.dp))
            SectionLabel(text = "이번 주 활동")
            Spacer(modifier = Modifier.height(12.dp))
            WeekActivityCard()

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun HeroSection(userName: String?) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clipToBounds()
            .background(FitMateBlue)
            .padding(horizontal = 24.dp, vertical = 28.dp)
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .wrapContentSize(align = Alignment.TopStart, unbounded = true)
                .offset(x = 250.dp, y = (-90).dp)
                .size(220.dp)
                .border(1.5.dp, Color.White.copy(alpha = 0.25f), CircleShape)
        )

        Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
            if (userName != null) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.18f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.FitnessCenter,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                    Column {
                        Text(
                            text = "${userName}님, 오늘도 화이팅!",
                            color = Color.White,
                            fontSize = MaterialTheme.typography.titleMedium.fontSize,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "이번 주도 함께 성장해봐요",
                            color = Color.White.copy(alpha = 0.75f),
                            fontSize = MaterialTheme.typography.bodySmall.fontSize
                        )
                    }
                }
            } else {
                Column {
                    Text(
                        text = "오늘도 열심히 운동해봐요!",
                        color = Color.White,
                        fontSize = MaterialTheme.typography.titleMedium.fontSize,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "이번 주도 함께 성장해봐요",
                        color = Color.White.copy(alpha = 0.75f),
                        fontSize = MaterialTheme.typography.bodySmall.fontSize
                    )
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White.copy(alpha = 0.14f))
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "이번 주 운동",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = MaterialTheme.typography.bodySmall.fontSize
                        )
                        Text(
                            text = "4/5회",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    LinearProgressIndicator(
                        progress = { 0.8f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = FitMateYellow,
                        trackColor = Color.White.copy(alpha = 0.22f)
                    )
                }

                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White.copy(alpha = 0.14f))
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.LocalFireDepartment,
                            contentDescription = null,
                            tint = FitMateYellow,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(text = "12일", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                    Text(
                        text = "연속 달성",
                        color = Color.White.copy(alpha = 0.75f),
                        fontSize = MaterialTheme.typography.labelSmall.fontSize
                    )
                }
            }
        }
    }
}

@Composable
private fun SectionLabel(text: String) {
    Text(
        text = text,
        color = TextMuted,
        fontWeight = FontWeight.Medium,
        fontSize = MaterialTheme.typography.bodyMedium.fontSize
    )
}

@Composable
private fun TodayWorkoutCard(onStartWorkout: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(FitMateBlue.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.FitnessCenter,
                        contentDescription = null,
                        tint = FitMateBlue
                    )
                }
                Column {
                    Text(
                        text = "월요일 · 상체 PUSH",
                        fontWeight = FontWeight.Medium,
                        fontSize = MaterialTheme.typography.bodyLarge.fontSize
                    )
                    Text(
                        text = "약 55분 · 5종목 · 16세트",
                        color = TextMuted,
                        fontSize = MaterialTheme.typography.bodySmall.fontSize
                    )
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("벤치프레스", "숄더프레스", "체스트프레스", "+2").forEach { label ->
                    Surface(
                        shape = RoundedCornerShape(100.dp),
                        color = ChipBackground
                    ) {
                        Text(
                            text = label,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            fontSize = MaterialTheme.typography.labelSmall.fontSize
                        )
                    }
                }
            }

            Button(
                onClick = onStartWorkout,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = FitMateBlue),
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(imageVector = Icons.Filled.PlayArrow, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "운동 시작")
            }
        }
    }
}

@Composable
private fun WatchConnectCard(onClick: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = Color.White,
        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF16181A)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = Icons.Filled.Watch, contentDescription = null, tint = Color.White)
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "Galaxy Watch 연동", fontWeight = FontWeight.Medium)
                Text(
                    text = "워치로 오늘 운동 시작하기",
                    color = TextMuted,
                    fontSize = MaterialTheme.typography.bodySmall.fontSize
                )
            }
            Icon(imageVector = Icons.Filled.ChevronRight, contentDescription = null, tint = TextFaint)
        }
    }
}

@Composable
private fun WeekActivityCard() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = Color.White,
        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val completed = listOf(true, true, true, true, false, false, false)
            val today = 4
            weekDayLabels.forEachIndexed { index, label ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    when {
                        completed[index] -> Box(
                            modifier = Modifier
                                .size(30.dp)
                                .clip(CircleShape)
                                .background(FitMateBlue),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }

                        index == today -> Box(
                            modifier = Modifier
                                .size(30.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                                .border(1.5.dp, FitMateBlue, CircleShape)
                        )

                        else -> Box(
                            modifier = Modifier
                                .size(30.dp)
                                .clip(CircleShape)
                                .background(ChipBackground)
                        )
                    }
                    Text(
                        text = label,
                        fontSize = MaterialTheme.typography.labelSmall.fontSize,
                        color = if (completed[index] || index == today) Color(0xFF343A40) else TextFaint
                    )
                }
            }
        }
    }
}
