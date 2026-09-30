package com.fitmate.presentation.mypage

import androidx.compose.animation.animateContentSize
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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Healing
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.TrendingUp
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private val FitMateBlue = Color(0xFF3E78AD)
private val AccentBlue = Color(0xFF42688A)
private val ScreenBackground = Color(0xFFF8F9FA)
private val CardBorder = Color(0xFFE2E6EA)
private val TextMuted = Color(0xFF6C757D)
private val SelectedBg = Color(0xFFE1ECF4)
private val InfoBg = Color(0xFFE1ECF4)

@Composable
fun BodyProfileScreen(
    onBack: () -> Unit = {},
    onSave: () -> Unit = {}
) {
    var expandedSection by remember { mutableStateOf<String?>(null) }

    var gender by remember { mutableStateOf("여성") }
    var age by remember { mutableIntStateOf(28) }
    var height by remember { mutableIntStateOf(163) }
    var weight by remember { mutableIntStateOf(71) }
    var mainGoal by remember { mutableStateOf("체지방 감량") }
    var detailGoal by remember { mutableStateOf("근대비 중심") }
    var experience by remember { mutableStateOf("중급") }
    var frequency by remember { mutableIntStateOf(4) }
    var environment by remember { mutableStateOf("헬스장") }
    var painArea by remember { mutableStateOf("없음") }

    Column(modifier = Modifier.fillMaxSize().background(ScreenBackground)) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().background(Color.White)) {
            IconButton(onClick = onBack) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "뒤로")
            }
            Text(text = "내 운동 프로필", fontWeight = FontWeight.Medium, fontSize = MaterialTheme.typography.titleMedium.fontSize)
        }

        Column(modifier = Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(20.dp)) {
            Text(text = "기본 정보", color = TextMuted, fontSize = MaterialTheme.typography.labelSmall.fontSize)
            Spacer(modifier = Modifier.height(8.dp))
            AccordionCard(
                icon = Icons.Filled.Person,
                label = "기본 정보",
                valuePreview = "$gender · ${age}세 · ${height}cm · ${weight}kg",
                expanded = expandedSection == "basic",
                onToggle = { expandedSection = if (expandedSection == "basic") null else "basic" }
            ) {
                Text(text = "성별", color = TextMuted, fontSize = MaterialTheme.typography.labelSmall.fontSize)
                Spacer(modifier = Modifier.height(8.dp))
                ChipRow(options = listOf("남성", "여성", "선택하지 않음"), selected = gender, onSelect = { gender = it })
                Spacer(modifier = Modifier.height(16.dp))
                StepperRow(label = "나이", value = age, suffix = "세", onChange = { age = it.coerceAtLeast(1) })
                Spacer(modifier = Modifier.height(12.dp))
                StepperRow(label = "키", value = height, suffix = "cm", onChange = { height = it.coerceAtLeast(1) })
                Spacer(modifier = Modifier.height(12.dp))
                StepperRow(label = "몸무게", value = weight, suffix = "kg", onChange = { weight = it.coerceAtLeast(1) })
            }

            Spacer(modifier = Modifier.height(20.dp))
            Text(text = "운동 설정", color = TextMuted, fontSize = MaterialTheme.typography.labelSmall.fontSize)
            Spacer(modifier = Modifier.height(8.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                AccordionCard(
                    icon = Icons.Filled.Flag,
                    label = "운동 목표",
                    valuePreview = "$mainGoal · $detailGoal",
                    expanded = expandedSection == "goal",
                    onToggle = { expandedSection = if (expandedSection == "goal") null else "goal" }
                ) {
                    Text(text = "주요 목표", color = TextMuted, fontSize = MaterialTheme.typography.labelSmall.fontSize)
                    Spacer(modifier = Modifier.height(8.dp))
                    ChipRow(options = listOf("체지방 감량", "근육 성장", "근력 향상", "신체 재구성", "체력 향상", "운동 습관 형성"), selected = mainGoal, onSelect = { mainGoal = it })
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = "세부 목표", color = TextMuted, fontSize = MaterialTheme.typography.labelSmall.fontSize)
                    Spacer(modifier = Modifier.height(8.dp))
                    ChipRow(options = listOf("근대비 중심", "특정 부위 집중", "전신 균형 성장"), selected = detailGoal, onSelect = { detailGoal = it })
                }

                AccordionCard(
                    icon = Icons.Filled.MilitaryTech,
                    label = "운동 경력",
                    valuePreview = experience,
                    expanded = expandedSection == "experience",
                    onToggle = { expandedSection = if (expandedSection == "experience") null else "experience" }
                ) {
                    ChipRow(options = listOf("초급", "중급", "고급"), selected = experience, onSelect = { experience = it })
                }

                AccordionCard(
                    icon = Icons.Filled.CalendarMonth,
                    label = "운동 가능 조건",
                    valuePreview = "주 ${frequency}회 · 60분",
                    expanded = expandedSection == "condition",
                    onToggle = { expandedSection = if (expandedSection == "condition") null else "condition" }
                ) {
                    StepperRow(label = "주간 운동 횟수", value = frequency, suffix = "회", onChange = { frequency = it.coerceIn(1, 7) })
                }

                AccordionCard(
                    icon = Icons.Filled.LocationOn,
                    label = "운동 환경",
                    valuePreview = environment,
                    expanded = expandedSection == "environment",
                    onToggle = { expandedSection = if (expandedSection == "environment") null else "environment" }
                ) {
                    ChipRow(options = listOf("헬스장", "집", "야외"), selected = environment, onSelect = { environment = it })
                }

                AccordionCard(
                    icon = Icons.Filled.FitnessCenter,
                    label = "사용 가능 기구",
                    valuePreview = "12개",
                    expanded = expandedSection == "equipment",
                    onToggle = { expandedSection = if (expandedSection == "equipment") null else "equipment" }
                ) {
                    Text(text = "내 운동환경 메뉴에서 기구를 편집할 수 있어요.", color = TextMuted, fontSize = MaterialTheme.typography.bodySmall.fontSize)
                }

                AccordionCard(
                    icon = Icons.Filled.Healing,
                    label = "부담 부위",
                    valuePreview = painArea,
                    expanded = expandedSection == "pain",
                    onToggle = { expandedSection = if (expandedSection == "pain") null else "pain" }
                ) {
                    ChipRow(options = listOf("없음", "어깨", "허리", "무릎", "손목"), selected = painArea, onSelect = { painArea = it })
                }

                AccordionCard(
                    icon = Icons.Filled.TrendingUp,
                    label = "최근 수행 능력",
                    valuePreview = "벤치프레스 40kg × 8회",
                    expanded = expandedSection == "performance",
                    onToggle = { expandedSection = if (expandedSection == "performance") null else "performance" }
                ) {
                    Text(text = "운동 기록이 쌓이면 자동으로 업데이트돼요.", color = TextMuted, fontSize = MaterialTheme.typography.bodySmall.fontSize)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp), color = InfoBg, border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFB0CBDC))) {
                Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Icon(imageVector = Icons.Filled.AutoAwesome, contentDescription = null, tint = AccentBlue, modifier = Modifier.size(18.dp))
                    Text(text = "프로필이 변경되면 다음 루틴부터 새로운 정보를 반영해요.", color = Color(0xFF27384A), fontSize = MaterialTheme.typography.bodySmall.fontSize)
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
                Text(text = "수정하기")
            }
        }
    }
}

@Composable
private fun AccordionCard(
    icon: ImageVector,
    label: String,
    valuePreview: String,
    expanded: Boolean,
    onToggle: () -> Unit,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth().animateContentSize(),
        shape = RoundedCornerShape(14.dp),
        color = Color.White,
        border = androidx.compose.foundation.BorderStroke(1.dp, if (expanded) AccentBlue else CardBorder),
        onClick = onToggle
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(modifier = Modifier.size(36.dp).background(SelectedBg, RoundedCornerShape(10.dp)), contentAlignment = Alignment.Center) {
                    Icon(imageVector = icon, contentDescription = null, tint = AccentBlue, modifier = Modifier.size(18.dp))
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = label, color = TextMuted, fontSize = MaterialTheme.typography.labelSmall.fontSize)
                    Text(text = valuePreview, fontWeight = FontWeight.Medium)
                }
                Icon(imageVector = Icons.Filled.ExpandMore, contentDescription = null, tint = TextMuted)
            }
            if (expanded) {
                Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(CardBorder))
                Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                    content()
                }
            }
        }
    }
}

@Composable
private fun ChipRow(options: List<String>, selected: String, onSelect: (String) -> Unit) {
    Row(modifier = Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        options.forEach { option ->
            val isSelected = option == selected
            Surface(
                shape = RoundedCornerShape(100.dp),
                color = if (isSelected) SelectedBg else Color.White,
                border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) AccentBlue else CardBorder),
                onClick = { onSelect(option) }
            ) {
                Text(
                    text = option,
                    color = if (isSelected) AccentBlue else TextMuted,
                    fontWeight = FontWeight.Bold,
                    fontSize = MaterialTheme.typography.labelMedium.fontSize,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                )
            }
        }
    }
}

@Composable
private fun StepperRow(label: String, value: Int, suffix: String, onChange: (Int) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Text(text = label, color = TextMuted, fontSize = MaterialTheme.typography.labelSmall.fontSize)
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            StepButton(icon = Icons.Filled.Remove, onClick = { onChange(value - 1) })
            Text(text = "$value$suffix", fontWeight = FontWeight.Medium, modifier = Modifier.padding(horizontal = 4.dp))
            StepButton(icon = Icons.Filled.Add, onClick = { onChange(value + 1) })
        }
    }
}

@Composable
private fun StepButton(icon: ImageVector, onClick: () -> Unit) {
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
