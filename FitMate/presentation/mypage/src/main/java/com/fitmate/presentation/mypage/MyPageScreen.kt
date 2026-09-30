package com.fitmate.presentation.mypage

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private val FitMateBlue = Color(0xFF3E78AD)
private val ScreenBackground = Color(0xFFF8F9FA)
private val CardBorder = Color(0xFFE2E6EA)
private val TextMuted = Color(0xFF6C757D)
private val InfoBg = Color(0xFFEFF6FA)

private data class MenuEntry(val icon: ImageVector, val label: String)

private val menuEntries = listOf(
    MenuEntry(Icons.Filled.Person, "내 신체정보"),
    MenuEntry(Icons.Filled.LocationOn, "내 운동환경"),
    MenuEntry(Icons.Filled.Psychology, "AI Trainer 설정"),
    MenuEntry(Icons.Filled.Settings, "앱 설정")
)

@Composable
fun MyPageScreen(
    onOpenMenu: (String) -> Unit = {}
) {
    Column(modifier = Modifier.fillMaxSize().background(ScreenBackground)) {
        Column(modifier = Modifier.fillMaxWidth().background(Color.White).padding(horizontal = 20.dp, vertical = 16.dp)) {
            Text(text = "마이페이지", fontWeight = FontWeight.Medium, fontSize = MaterialTheme.typography.titleLarge.fontSize)
        }

        Column(modifier = Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(horizontal = 20.dp, vertical = 12.dp)) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = Color.White
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Box(modifier = Modifier.size(59.dp).background(CardBorder, CircleShape))
                        Column {
                            Text(text = "김도연", fontWeight = FontWeight.Medium, fontSize = MaterialTheme.typography.bodyLarge.fontSize)
                            Text(text = "중급 · 주 4회", color = TextMuted, fontSize = MaterialTheme.typography.labelMedium.fontSize)
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Surface(shape = RoundedCornerShape(16.dp), color = InfoBg, modifier = Modifier.fillMaxWidth()) {
                        Row(modifier = Modifier.padding(20.dp), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = "목표", color = TextMuted, fontSize = MaterialTheme.typography.labelSmall.fontSize)
                                Text(text = "체지방 감량 · 균형형", fontWeight = FontWeight.SemiBold, fontSize = MaterialTheme.typography.bodyMedium.fontSize)
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = "내 헬스장", color = TextMuted, fontSize = MaterialTheme.typography.labelSmall.fontSize)
                                Text(text = "Fit Gym", fontWeight = FontWeight.SemiBold, fontSize = MaterialTheme.typography.bodyMedium.fontSize)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            Text(text = "내 정보 · 설정", fontWeight = FontWeight.Medium, fontSize = MaterialTheme.typography.bodyLarge.fontSize)
            Spacer(modifier = Modifier.height(8.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = Color.White
            ) {
                Column {
                    menuEntries.forEachIndexed { index, entry ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onOpenMenu(entry.label) }
                                .padding(horizontal = 18.dp, vertical = 16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            Icon(imageVector = entry.icon, contentDescription = null, tint = TextMuted)
                            Text(text = entry.label, modifier = Modifier.weight(1f), fontSize = MaterialTheme.typography.bodyLarge.fontSize)
                            Icon(imageVector = Icons.Filled.ChevronRight, contentDescription = null, tint = CardBorder)
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
