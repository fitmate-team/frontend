package com.fitmate.presentation.mypage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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

private data class ToggleSetting(val title: String, val description: String? = null, val defaultOn: Boolean)

private val settings = listOf(
    ToggleSetting("AI 루틴 조정 제안", "운동 결과를 분석해 다음 루틴 변경을 제안해요.", true),
    ToggleSetting("운동 후 난이도 질문", defaultOn = true),
    ToggleSetting("주간 AI 리포트", defaultOn = true),
    ToggleSetting("AI 추천 이유 표시", defaultOn = true),
    ToggleSetting("루틴 변경 자동 적용", "FitMate는 기본적으로 변경사항을 사용자 확인 후 적용해요.", false)
)

@Composable
fun AiTrainerSettingsScreen(
    onBack: () -> Unit = {}
) {
    val toggleStates = remember { settings.map { mutableStateOf(it.defaultOn) } }

    Column(modifier = Modifier.fillMaxSize().background(ScreenBackground)) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().background(Color.White)) {
            IconButton(onClick = onBack) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "뒤로")
            }
            Text(text = "AI Trainer 설정", fontWeight = FontWeight.Medium, fontSize = MaterialTheme.typography.titleMedium.fontSize)
        }

        Column(modifier = Modifier.padding(20.dp)) {
            Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), color = Color.White, border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)) {
                Column {
                    settings.forEachIndexed { index, setting ->
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 18.dp, vertical = 16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = setting.title, fontWeight = FontWeight.Medium, fontSize = MaterialTheme.typography.bodyLarge.fontSize)
                                setting.description?.let {
                                    Text(text = it, color = TextMuted, fontSize = MaterialTheme.typography.labelSmall.fontSize)
                                }
                            }
                            Switch(
                                checked = toggleStates[index].value,
                                onCheckedChange = { toggleStates[index].value = it },
                                colors = SwitchDefaults.colors(checkedTrackColor = FitMateBlue)
                            )
                        }
                        if (index < settings.lastIndex) {
                            Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(CardBorder))
                        }
                    }
                }
            }
        }
    }
}
