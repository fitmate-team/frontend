package com.fitmate.presentation.record

import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.SmartToy
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

private val FitMateBlue = Color(0xFF3E78AD)
private val AccentBlue = Color(0xFF42688A)
private val ScreenBackground = Color(0xFFF8F9FA)
private val CardBorder = Color(0xFFE2E6EA)
private val TextMuted = Color(0xFF6C757D)
private val InfoBg = Color(0xFFE1ECF4)

private enum class AnalysisTab(val label: String) { WEIGHT_LIFTED("중량"), BODY_WEIGHT("몸무게"), ATTENDANCE("출석률") }

private val weightChartValues = listOf(35f, 37.5f, 35f, 40f, 42.5f, 40f, 42.5f)
private val bodyWeightChartValues = listOf(72.4f, 72f, 71.5f, 71.8f, 71f, 70.5f, 70.8f)
private val attendanceChartValues = listOf(70f, 75f, 72f, 80f, 78f, 84f, 86f)

@Composable
fun GrowthAnalysisScreen(
    onBack: () -> Unit = {}
) {
    var selectedTab by remember { mutableStateOf(AnalysisTab.WEIGHT_LIFTED) }

    Column(modifier = Modifier.fillMaxSize().background(ScreenBackground)) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().background(Color.White)) {
            IconButton(onClick = onBack) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "뒤로")
            }
            Text(text = "성장 분석", fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.titleMedium.fontSize)
        }

        Row(modifier = Modifier.fillMaxWidth().background(Color.White)) {
            AnalysisTab.entries.forEach { tab ->
                val isSelected = tab == selectedTab
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(vertical = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    androidx.compose.foundation.layout.Box(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        androidx.compose.foundation.layout.Box(modifier = Modifier.padding(6.dp)) {
                            androidx.compose.material3.TextButton(onClick = { selectedTab = tab }) {
                                Text(text = tab.label, color = if (isSelected) AccentBlue else TextMuted, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
                            }
                        }
                    }
                    androidx.compose.foundation.layout.Box(
                        modifier = Modifier.fillMaxWidth().height(2.dp).background(if (isSelected) AccentBlue else Color.Transparent)
                    )
                }
            }
        }
        androidx.compose.foundation.layout.Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(CardBorder))

        Column(modifier = Modifier.weight(1f).padding(16.dp)) {
            when (selectedTab) {
                AnalysisTab.WEIGHT_LIFTED -> WeightLiftedTab()
                AnalysisTab.BODY_WEIGHT -> BodyWeightTab()
                AnalysisTab.ATTENDANCE -> AttendanceTab()
            }
        }
    }
}

@Composable
private fun WeightLiftedTab() {
    Surface(shape = RoundedCornerShape(10.dp), color = Color.White, border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)) {
        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 11.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = "벤치프레스", fontWeight = FontWeight.SemiBold)
            Text(text = "▾", color = TextMuted)
        }
    }
    Spacer(modifier = Modifier.height(20.dp))
    Text(text = "최근 3개월 · 최고 기록", color = TextMuted, fontSize = MaterialTheme.typography.bodySmall.fontSize)
    Spacer(modifier = Modifier.height(4.dp))
    Row(verticalAlignment = Alignment.Bottom) {
        Text(text = "42.5kg", fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.displaySmall.fontSize)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = "× 8회", color = TextMuted, fontSize = MaterialTheme.typography.bodyMedium.fontSize, modifier = Modifier.padding(bottom = 6.dp))
    }
    Spacer(modifier = Modifier.height(4.dp))
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(text = "35kg", color = TextMuted, fontSize = MaterialTheme.typography.bodyMedium.fontSize)
        Box(modifier = Modifier.weight(1f).height(1.dp).background(Color(0xFFB0CBDC)))
        Text(text = "42.5kg", color = AccentBlue, fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.bodyMedium.fontSize)
    }
    Spacer(modifier = Modifier.height(16.dp))
    ChartCard(values = weightChartValues, badge = "+21%")
    Spacer(modifier = Modifier.height(20.dp))
    AiInsightCard(text = "최근 8주 동안 벤치프레스 수행 중량이 꾸준히 증가하고 있어요. 현재 속도라면 약 6주 후 50kg에 도전할 수 있어요.")
}

@Composable
private fun BodyWeightTab() {
    Text(text = "최근 3개월 · 몸무게 변화", color = TextMuted, fontSize = MaterialTheme.typography.bodySmall.fontSize)
    Spacer(modifier = Modifier.height(4.dp))
    Text(text = "70.8kg", fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.displaySmall.fontSize)
    Text(text = "최근 3개월 -1.6kg", color = TextMuted, fontSize = MaterialTheme.typography.bodyLarge.fontSize)
    Spacer(modifier = Modifier.height(16.dp))
    ChartCard(values = bodyWeightChartValues, badge = "-1.8kg")
}

@Composable
private fun AttendanceTab() {
    Text(text = "이번 달 운동 출석률", color = TextMuted, fontSize = MaterialTheme.typography.bodySmall.fontSize)
    Spacer(modifier = Modifier.height(4.dp))
    Row(verticalAlignment = Alignment.Bottom) {
        Text(text = "86%", fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.displaySmall.fontSize)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = "3 / 4회", color = TextMuted, fontSize = MaterialTheme.typography.bodyMedium.fontSize, modifier = Modifier.padding(bottom = 6.dp))
    }
    Text(text = "전월 대비 +6% 증가", color = TextMuted, fontSize = MaterialTheme.typography.bodyLarge.fontSize)
    Spacer(modifier = Modifier.height(12.dp))
    Box(modifier = Modifier.fillMaxWidth().height(6.dp).background(CardBorder, RoundedCornerShape(100.dp))) {
        Box(modifier = Modifier.fillMaxWidth(0.86f).height(6.dp).background(AccentBlue, RoundedCornerShape(100.dp)))
    }
    Spacer(modifier = Modifier.height(4.dp))
    Text(text = "목표까지 1회 남았어요", color = TextMuted, fontSize = MaterialTheme.typography.bodySmall.fontSize)
    Spacer(modifier = Modifier.height(16.dp))
    ChartCard(values = attendanceChartValues, badge = "86%")
}

@Composable
private fun ChartCard(values: List<Float>, badge: String) {
    Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), color = Color.White, border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)) {
        Column(modifier = Modifier.padding(12.dp)) {
            Canvas(modifier = Modifier.fillMaxWidth().height(140.dp)) {
                val min = values.min()
                val max = values.max()
                val range = (max - min).coerceAtLeast(0.01f)
                val stepX = size.width / (values.size - 1)
                val points = values.mapIndexed { i, v ->
                    Offset(x = i * stepX, y = size.height - ((v - min) / range) * size.height)
                }
                for (i in 0 until points.size - 1) {
                    drawLine(color = AccentBlue, start = points[i], end = points[i + 1], strokeWidth = 4f)
                }
                points.forEach { p ->
                    drawCircle(color = AccentBlue, radius = 5f, center = p)
                    drawCircle(color = Color.White, radius = 2.5f, center = p)
                }
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "7월", color = TextMuted, fontSize = MaterialTheme.typography.labelSmall.fontSize)
                Text(text = "8월", color = TextMuted, fontSize = MaterialTheme.typography.labelSmall.fontSize)
                Text(text = "9월", color = TextMuted, fontSize = MaterialTheme.typography.labelSmall.fontSize)
            }
        }
    }
}

@Composable
private fun AiInsightCard(text: String) {
    Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), color = InfoBg, border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFB0CBDC))) {
        Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Icon(imageVector = Icons.Filled.SmartToy, contentDescription = null, tint = AccentBlue)
            Column {
                Text(text = "AI 인사이트", color = AccentBlue, fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.labelMedium.fontSize)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = text, fontSize = MaterialTheme.typography.bodySmall.fontSize)
            }
        }
    }
}
