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
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.TrendingUp
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
private val InfoText = Color(0xFF294767)

private data class ReasonFact(val icon: ImageVector, val label: String, val value: String)

private val facts = listOf(
    ReasonFact(Icons.Filled.TrendingUp, "운동 목표", "체지방 감량 · 균형형"),
    ReasonFact(Icons.Filled.FitnessCenter, "운동 경험", "초급~중급"),
    ReasonFact(Icons.Filled.CalendarMonth, "운동 가능 횟수", "주 4회"),
    ReasonFact(Icons.Filled.Schedule, "운동 가능 시간", "회당 약 60분"),
    ReasonFact(Icons.Filled.Home, "운동 환경", "헬스장"),
    ReasonFact(Icons.Filled.FitnessCenter, "사용 가능 기구", "바벨 · 덤벨 · 머신 등")
)

private data class ReasonCard(val title: String, val tag: String, val body: String)

private val reasonCards = listOf(
    ReasonCard("근력 운동 70%", "체지방 감량", "체지방을 감량하면서 근육을 유지할 수 있도록 근력 운동의 비중을 높였어요."),
    ReasonCard("유산소 30%", "균형형", "근력 운동과 함께 유산소를 배치해 전체 활동량을 높일 수 있도록 구성했어요."),
    ReasonCard("주 4회 분할 루틴", "주 4회 + 60분", "회원님이 선택한 운동 가능 요일과 회당 60분을 기준으로 상체 PUSH · 하체 · 상체 PULL · 전신 운동으로 나눴어요.")
)

private data class DayReason(val label: String, val title: String, val detail: String)

private val dayReasons = listOf(
    DayReason("월", "월요일 · 상체 PUSH", "벤치프레스, 숄더프레스, 체스트프레스를 중심으로 가슴과 어깨의 주요 근육을 함께 운동하도록 구성했어요."),
    DayReason("화", "화요일 · 하체", "하체 주요 근육을 집중적으로 운동"),
    DayReason("목", "목요일 · 상체 PULL", "등 · 이두 중심으로 상체 균형 보완"),
    DayReason("토", "토요일 · 전신 + 유산소", "주간 운동량을 보완하고 유산소를 함께 수행")
)

@Composable
fun RoutineAiReasonScreen(
    onBack: () -> Unit = {},
    onConfirm: () -> Unit = {}
) {
    Column(modifier = Modifier.fillMaxSize().background(ScreenBackground)) {
        Row(
            modifier = Modifier.fillMaxWidth().background(Color.White).padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "뒤로")
            }
            Text(text = "추천 이유", fontWeight = FontWeight.Medium, fontSize = MaterialTheme.typography.titleMedium.fontSize)
        }

        Column(modifier = Modifier.weight(1f).verticalScroll(rememberScrollState())) {
            Column(modifier = Modifier.fillMaxWidth().background(FitMateBlue).padding(24.dp)) {
                Text(text = "이런 이유로\n이번 루틴을\n추천했어요", color = Color.White, fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.headlineSmall.fontSize)
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = "회원님의 목표, 운동 경험, 운동 환경과 사용 가능한 시간을 함께 고려했어요.", color = Color.White.copy(alpha = 0.75f), fontSize = MaterialTheme.typography.bodySmall.fontSize)
            }

            Column(modifier = Modifier.padding(20.dp)) {
                Text(text = "AI가 참고한 정보", color = TextMuted, fontSize = MaterialTheme.typography.labelSmall.fontSize, fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.height(12.dp))

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    facts.chunked(2).forEach { pair ->
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            pair.forEach { fact ->
                                Surface(
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(14.dp),
                                    color = Color.White,
                                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                                ) {
                                    Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp)) {
                                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                            Icon(imageVector = fact.icon, contentDescription = null, tint = FitMateBlue, modifier = Modifier.size(16.dp))
                                            Text(text = fact.label, color = TextMuted, fontSize = MaterialTheme.typography.labelSmall.fontSize)
                                        }
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(text = fact.value, fontWeight = FontWeight.Medium, fontSize = MaterialTheme.typography.bodyMedium.fontSize)
                                    }
                                }
                            }
                            if (pair.size < 2) Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
                Text(text = "그래서 이렇게 구성했어요", color = TextMuted, fontSize = MaterialTheme.typography.labelSmall.fontSize, fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.height(12.dp))
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    reasonCards.forEach { card ->
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            color = Color.White,
                            border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                    Text(text = card.title, fontWeight = FontWeight.Medium, fontSize = MaterialTheme.typography.bodyLarge.fontSize)
                                    Surface(shape = RoundedCornerShape(100.dp), color = IconBg, border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF9BC0DE))) {
                                        Text(text = card.tag, color = FitMateBlue, fontSize = MaterialTheme.typography.labelSmall.fontSize, modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp))
                                    }
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(text = card.body, color = TextMuted, fontSize = MaterialTheme.typography.bodySmall.fontSize)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
                Text(text = "요일별 추천 이유", color = TextMuted, fontSize = MaterialTheme.typography.labelSmall.fontSize, fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.height(12.dp))
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White,
                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                ) {
                    Column {
                        dayReasons.forEachIndexed { index, reason ->
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Box(
                                    modifier = Modifier.size(36.dp).clip(RoundedCornerShape(10.dp)).background(if (index == 0) FitMateBlue else Color(0xFFF8F9FA)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = reason.label, color = if (index == 0) Color.White else TextMuted, fontSize = MaterialTheme.typography.labelSmall.fontSize)
                                }
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(text = reason.title, fontWeight = FontWeight.Medium, color = if (index == 0) FitMateBlue else Color(0xFF16181A))
                                    Text(text = reason.detail, color = TextMuted, fontSize = MaterialTheme.typography.labelMedium.fontSize)
                                }
                            }
                            if (index < dayReasons.lastIndex) {
                                Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(CardBorder))
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = IconBg,
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF9BC0DE))
                ) {
                    Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                        Box(modifier = Modifier.size(56.dp).clip(RoundedCornerShape(7.dp)).background(CardBorder))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "앞으로 추천은 더 달라질 수 있어요", color = FitMateBlue, fontWeight = FontWeight.Medium)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(text = "실제 운동 기록과 체감 난이도가 쌓이면 중량, 반복수, 세트 수와 운동 구성을 회원님의 수행 능력에 맞게 계속 조정해요.", color = InfoText, fontSize = MaterialTheme.typography.bodySmall.fontSize)
                        }
                    }
                }
            }
        }

        Column(modifier = Modifier.background(ScreenBackground).padding(20.dp)) {
            Button(
                onClick = onConfirm,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = FitMateBlue),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(text = "확인했어요")
            }
        }
    }
}
