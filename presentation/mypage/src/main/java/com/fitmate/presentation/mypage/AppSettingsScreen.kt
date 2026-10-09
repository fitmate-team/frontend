package com.fitmate.presentation.mypage

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private val FitMateBlue = Color(0xFF3E78AD)
private val ScreenBackground = Color(0xFFF8F9FA)
private val CardBorder = Color(0xFFE2E6EA)
private val TextMuted = Color(0xFF6C757D)
private val DangerRed = Color(0xFFD32F2F)

private data class SettingRow(val label: String, val value: String)

private val screenSettings = listOf(SettingRow("테마", "시스템 설정"))
private val integrationSettings = listOf(SettingRow("Galaxy Watch / Wear OS", "연결됨"), SettingRow("건강 데이터 연동", "연결 안 됨"))
private val accountFields = listOf(SettingRow("키", "163cm"), SettingRow("몸무게", "70.8kg"))

@Composable
fun AppSettingsScreen(
    onBack: () -> Unit = {},
    onSave: () -> Unit = {},
    onLogout: () -> Unit = {},
    onDeleteAccount: () -> Unit = {}
) {
    Column(modifier = Modifier.fillMaxSize().background(ScreenBackground)) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().background(Color.White)) {
            IconButton(onClick = onBack) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "뒤로")
            }
            Text(text = "앱 설정", fontWeight = FontWeight.Medium, fontSize = MaterialTheme.typography.titleMedium.fontSize)
        }

        Column(modifier = Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(20.dp)) {
            SettingSection(title = "화면", rows = screenSettings)
            Spacer(modifier = Modifier.height(20.dp))
            SettingSection(title = "연동", rows = integrationSettings)
            Spacer(modifier = Modifier.height(20.dp))
            SettingSection(title = "계정", rows = accountFields, actionLabel = "로그아웃", onAction = onLogout, dangerLabel = "회원 탈퇴", onDanger = onDeleteAccount)
        }

        Column(modifier = Modifier.background(ScreenBackground).padding(20.dp)) {
            Button(
                onClick = onSave,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = FitMateBlue),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(text = "변경사항 저장")
            }
        }
    }
}

@Composable
private fun SettingSection(
    title: String,
    rows: List<SettingRow>,
    actionLabel: String? = null,
    onAction: () -> Unit = {},
    dangerLabel: String? = null,
    onDanger: () -> Unit = {}
) {
    Text(text = title, color = TextMuted, fontWeight = FontWeight.Medium, fontSize = MaterialTheme.typography.labelSmall.fontSize)
    Spacer(modifier = Modifier.height(8.dp))
    Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), color = Color.White, border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)) {
        Column {
            if (actionLabel != null) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 18.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    androidx.compose.material3.TextButton(onClick = onAction) {
                        Text(text = actionLabel, fontWeight = FontWeight.Medium)
                    }
                }
                Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(CardBorder))
            }
            if (dangerLabel != null) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 18.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    androidx.compose.material3.TextButton(onClick = onDanger) {
                        Text(text = dangerLabel, color = DangerRed, fontWeight = FontWeight.Medium)
                    }
                }
                Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(CardBorder))
            }
            rows.forEachIndexed { index, row ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 18.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = row.label, color = TextMuted, modifier = Modifier.weight(1f), fontSize = MaterialTheme.typography.bodyMedium.fontSize)
                    Text(text = row.value, fontWeight = FontWeight.Medium, fontSize = MaterialTheme.typography.bodyLarge.fontSize)
                    Icon(imageVector = Icons.Filled.ChevronRight, contentDescription = null, tint = CardBorder)
                }
                if (index < rows.lastIndex) {
                    Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(CardBorder))
                }
            }
        }
    }
}
