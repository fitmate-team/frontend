package com.fitmate.presentation.mypage

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private val AccentBlue = Color(0xFF42688A)
private val ScreenBackground = Color(0xFFF8F9FA)
private val CardBorder = Color(0xFFE2E6EA)
private val TextMuted = Color(0xFF6C757D)
private val InfoBg = Color(0xFFE1ECF4)

private val gymEquipment = listOf("바벨", "덤벨", "벤치")
private val homeEquipment = listOf("바벨", "덤벨", "벤치")

@Composable
fun WorkoutEnvironmentScreen(
    onBack: () -> Unit = {},
    onEditGym: () -> Unit = {},
    onEditHome: () -> Unit = {}
) {
    Column(modifier = Modifier.fillMaxSize().background(ScreenBackground)) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().background(Color.White)) {
            IconButton(onClick = onBack) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "뒤로")
            }
            Text(text = "Fit Gym", fontWeight = FontWeight.SemiBold, fontSize = MaterialTheme.typography.titleMedium.fontSize)
        }

        Column(modifier = Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            EnvironmentCard(
                title = "Fit Gym",
                subtitleIcon = Icons.Filled.LocationOn,
                subtitle = "서울 노원구 상계동 123-45",
                equipmentLabel = "등록된 운동 기구 12개",
                equipment = gymEquipment,
                extraCount = 9,
                editLabel = "헬스장 및 운동기구 편집",
                onEdit = onEditGym
            )
            EnvironmentCard(
                title = "홈트 운동 환경",
                subtitleIcon = null,
                subtitle = "홈트 가능한 운동기구 6개",
                equipmentLabel = "등록된 기구",
                equipment = homeEquipment,
                extraCount = 9,
                editLabel = "홈트 운동기구 편집",
                onEdit = onEditHome
            )

            Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp), color = InfoBg, border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFB0CBDC))) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Icon(imageVector = Icons.Filled.AutoAwesome, contentDescription = null, tint = AccentBlue, modifier = Modifier.size(18.dp))
                        Text(text = "운동환경도 루틴에 반영돼요", color = AccentBlue, fontWeight = FontWeight.SemiBold, fontSize = MaterialTheme.typography.bodyMedium.fontSize)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "FitMate는 등록된 운동장소와 운동기구를 확인해 실제로 할 수 있는 운동으로 루틴을 구성해요.", color = AccentBlue, fontSize = MaterialTheme.typography.bodySmall.fontSize)
                }
            }
        }
    }
}

@Composable
private fun EnvironmentCard(
    title: String,
    subtitleIcon: androidx.compose.ui.graphics.vector.ImageVector?,
    subtitle: String,
    equipmentLabel: String,
    equipment: List<String>,
    extraCount: Int,
    editLabel: String,
    onEdit: () -> Unit
) {
    Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), color = Color.White, border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(text = title, fontWeight = FontWeight.Medium, fontSize = MaterialTheme.typography.titleMedium.fontSize)
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                subtitleIcon?.let { Icon(imageVector = it, contentDescription = null, tint = AccentBlue, modifier = Modifier.size(18.dp)) }
                Text(text = subtitle, color = TextMuted, fontSize = MaterialTheme.typography.bodySmall.fontSize)
            }
            Spacer(modifier = Modifier.height(10.dp))
            Box(modifier = Modifier.fillMaxWidth().height(2.dp).background(CardBorder))
            Spacer(modifier = Modifier.height(10.dp))
            if (equipmentLabel.contains("12") || equipmentLabel.contains("등록")) {
                Text(text = equipmentLabel, color = TextMuted, fontSize = MaterialTheme.typography.bodySmall.fontSize)
                Spacer(modifier = Modifier.height(8.dp))
            }
            Row(modifier = Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(9.dp)) {
                equipment.forEach { item ->
                    Surface(shape = RoundedCornerShape(100.dp), color = InfoBg, border = androidx.compose.foundation.BorderStroke(1.dp, AccentBlue)) {
                        Text(text = item, color = AccentBlue, fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.bodyMedium.fontSize, modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp))
                    }
                }
                Surface(shape = RoundedCornerShape(100.dp), color = Color.White, border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)) {
                    Text(text = "+$extraCount", color = TextMuted, fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.bodyMedium.fontSize, modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp))
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Box(modifier = Modifier.fillMaxWidth().height(2.dp).background(CardBorder))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onEdit)
                    .padding(top = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(imageVector = Icons.Filled.Edit, contentDescription = null, tint = TextMuted, modifier = Modifier.size(18.dp))
                Text(text = editLabel, color = TextMuted, fontSize = MaterialTheme.typography.bodySmall.fontSize, modifier = Modifier.weight(1f))
            }
        }
    }
}
