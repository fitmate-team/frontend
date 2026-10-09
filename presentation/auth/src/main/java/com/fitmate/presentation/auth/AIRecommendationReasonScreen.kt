package com.fitmate.presentation.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Blue700 = Color(0xFF3E78AD)
private val Blue900 = Color(0xFF294767)
private val Blue200 = Color(0xFFDBE9F5)
private val Blue400 = Color(0xFF9BC0DE)
private val Gray200 = Color(0xFFE2E6EA)
private val Gray300 = Color(0xFFA7AEB3)
private val Gray500 = Color(0xFF6C757D)
private val Gray900 = Color(0xFF16181A)
private val ScreenBg = Color(0xFFF8F9FA)

private data class InfoTile(val icon: ImageVector, val label: String, val value: String)
private data class ReasonCard(val title: String, val tag: String, val desc: String)
private data class DayReason(val day: String, val title: String, val desc: String)

@Composable
private fun InfoTileView(tile: InfoTile) {
    Column(
        modifier = Modifier
            .background(Color.White, RoundedCornerShape(14.dp))
            .border(BorderStroke(1.dp, Gray200), RoundedCornerShape(14.dp))
            .padding(horizontal = 14.dp, vertical = 12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(tile.icon, contentDescription = null, tint = Blue700, modifier = Modifier.size(18.dp))
            Text(tile.label, fontSize = 11.sp, color = Gray500, modifier = Modifier.padding(start = 6.dp))
        }
        Text(tile.value, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Gray900, modifier = Modifier.padding(top = 6.dp))
    }
}

@Composable
private fun ReasonCardView(card: ReasonCard) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(14.dp))
            .border(BorderStroke(1.dp, Gray200), RoundedCornerShape(14.dp))
            .padding(16.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text(card.title, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Gray900)
            Box(
                modifier = Modifier
                    .background(Blue200, RoundedCornerShape(100.dp))
                    .border(BorderStroke(1.dp, Blue400), RoundedCornerShape(100.dp))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) { Text(card.tag, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Blue700) }
        }
        Text(card.desc, fontSize = 13.sp, color = Gray500, modifier = Modifier.padding(top = 8.dp))
    }
}

@Composable
private fun DayReasonRow(reason: DayReason, isFirst: Boolean) {
    Column(modifier = if (isFirst) Modifier else Modifier.border(BorderStroke(0.dp, Color.Transparent))) {
        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 14.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(36.dp).background(if (isFirst) Blue700 else ScreenBg, RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) { Text(reason.day, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = if (isFirst) Color.White else Gray500) }
            Column(modifier = Modifier.weight(1f).padding(start = 12.dp)) {
                Text(reason.title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = if (isFirst) Blue700 else Gray900)
                Text(reason.desc, fontSize = 12.sp, color = Gray500, modifier = Modifier.padding(top = 2.dp))
            }
        }
    }
}

@Composable
fun AIRecommendationReasonScreen(
    onBack: () -> Unit,
    onConfirm: () -> Unit
) {
    val infoTiles = listOf(
        InfoTile(Icons.Default.TrendingUp, "운동 목표", "체지방 감량 · 균형형"),
        InfoTile(Icons.Default.FitnessCenter, "운동 경험", "초급~중급"),
        InfoTile(Icons.Default.CalendarMonth, "운동 가능 횟수", "주 4회"),
        InfoTile(Icons.Default.Schedule, "운동 가능 시간", "회당 약 60분"),
        InfoTile(Icons.Default.Home, "운동 환경", "헬스장"),
        InfoTile(Icons.Default.FitnessCenter, "사용 가능 기구", "바벨 · 덤벨 · 머신 등")
    )
    val reasonCards = listOf(
        ReasonCard("근력 운동 70%", "체지방 감량", "체지방을 감량하면서 근육을 유지할 수 있도록 근력 운동의 비중을 높였어요."),
        ReasonCard("유산소 30%", "균형형", "근력 운동과 함께 유산소를 배치해 전체 활동량을 높일 수 있도록 구성했어요."),
        ReasonCard("주 4회 분할 루틴", "주 4회 + 60분", "회원님이 선택한 운동 가능 요일과 회당 60분을 기준으로 상체 PUSH · 하체 · 상체 PULL · 전신 운동으로 나눴어요.")
    )
    val dayReasons = listOf(
        DayReason("월", "월요일 · 상체 PUSH", "벤치프레스, 숄더프레스, 체스트프레스를 중심으로 가슴과 어깨의 주요 근육을 함께 운동하도록 구성했어요."),
        DayReason("화", "화요일 · 하체", "하체 주요 근육을 집중적으로 운동"),
        DayReason("목", "목요일 · 상체 PULL", "등 · 이두 중심으로 상체 균형 보완"),
        DayReason("토", "토요일 · 전신 + 유산소", "주간 운동량을 보완하고 유산소를 함께 수행")
    )

    Column(modifier = Modifier.fillMaxSize().background(ScreenBg)) {
        Row(
            modifier = Modifier.fillMaxWidth().background(Color.White).height(80.dp).padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "뒤로", tint = Gray900) }
            Text("추천 이유", fontSize = 18.sp, color = Gray900)
        }

        LazyColumn(modifier = Modifier.weight(1f)) {
            item {
                Column(modifier = Modifier.fillMaxWidth().background(Blue700).padding(20.dp)) {
                    Text("이런 이유로\n이번 루틴을\n추천했어요", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White, lineHeight = 32.sp)
                    Text(
                        "회원님의 목표, 운동 경험,\n운동 환경과 사용 가능한 시간을\n함께 고려했어요.",
                        fontSize = 13.sp,
                        color = Color.White.copy(alpha = 0.75f),
                        modifier = Modifier.padding(top = 10.dp)
                    )
                }

                Column(modifier = Modifier.padding(20.dp)) {
                    Text("AI가 참고한 정보", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Gray500)
                    Column(modifier = Modifier.padding(top = 12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        infoTiles.chunked(2).forEach { pair ->
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                pair.forEach { tile -> Box(modifier = Modifier.weight(1f)) { InfoTileView(tile) } }
                            }
                        }
                    }

                    Text("그래서 이렇게 구성했어요", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Gray500, modifier = Modifier.padding(top = 28.dp))
                    Column(modifier = Modifier.padding(top = 12.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        reasonCards.forEach { ReasonCardView(it) }
                    }

                    Text("요일별 추천 이유", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Gray500, modifier = Modifier.padding(top = 28.dp))
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp)
                            .background(Color.White, RoundedCornerShape(16.dp))
                            .border(BorderStroke(1.dp, Gray200), RoundedCornerShape(16.dp))
                            .padding(horizontal = 16.dp)
                    ) {
                        dayReasons.forEachIndexed { index, reason -> DayReasonRow(reason, isFirst = index == 0) }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 24.dp)
                            .background(Blue200, RoundedCornerShape(16.dp))
                            .border(BorderStroke(1.dp, Blue400), RoundedCornerShape(16.dp))
                            .padding(16.dp)
                    ) {
                        Box(modifier = Modifier.size(56.dp).background(Gray200, RoundedCornerShape(7.dp)).border(BorderStroke(1.dp, Gray300), RoundedCornerShape(7.dp)))
                        Column(modifier = Modifier.weight(1f).padding(start = 14.dp)) {
                            Text("앞으로 추천은 더 달라질 수 있어요", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Blue700)
                            Text(
                                "실제 운동 기록과 체감 난이도가 쌓이면 중량, 반복수, 세트 수와 운동 구성을 회원님의 수행 능력에 맞게 계속 조정해요.",
                                fontSize = 13.sp,
                                color = Blue900,
                                modifier = Modifier.padding(top = 6.dp)
                            )
                        }
                    }
                }
            }
        }

        Button(
            onClick = onConfirm,
            colors = ButtonDefaults.buttonColors(containerColor = Blue700),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp).padding(vertical = 16.dp).height(52.dp)
        ) { Text("확인했어요", fontSize = 16.sp, fontWeight = FontWeight.Bold) }
    }
}
