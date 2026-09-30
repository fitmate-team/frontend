package com.fitmate.presentation.mypage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private val FitMateBlue = Color(0xFF3E78AD)
private val AccentBlue = Color(0xFF42688A)
private val ScreenBackground = Color(0xFFF8F9FA)
private val CardBorder = Color(0xFFE2E6EA)
private val TextMuted = Color(0xFF6C757D)
private val SelectedBg = Color(0xFFE1ECF4)

private data class EquipmentCategory(val label: String, val items: List<String>)

private val categories = listOf(
    EquipmentCategory("기본장비", listOf("운동매트", "요가블록", "폼롤러")),
    EquipmentCategory("웨이트", listOf("덤벨", "조절식 덤벨", "케틀벨", "바벨", "원판")),
    EquipmentCategory("저항 운동", listOf("밴드", "미니 밴드", "튜빙 밴드")),
    EquipmentCategory("상체 운동", listOf("풀업바", "푸쉬운동", "딥스바")),
    EquipmentCategory("벤치/보조", listOf("플랫 벤치", "조절식 벤치")),
    EquipmentCategory("유산소", listOf("러닝머신", "실내 사이클", "스텝퍼", "로잉머신", "줄넘기"))
)

private val initiallySelected = setOf("운동매트", "요가블록", "폼롤러", "조절식 덤벨", "케틀벨", "바벨", "미니 밴드", "튜빙 밴드", "푸쉬운동", "딥스바", "조절식 벤치", "러닝머신")

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HomeEquipmentEditScreen(
    title: String = "홈트 기구 편집",
    onBack: () -> Unit = {},
    onSave: () -> Unit = {}
) {
    var selected by remember { mutableStateOf(initiallySelected) }

    Column(modifier = Modifier.fillMaxSize().background(ScreenBackground)) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().background(Color.White)) {
            IconButton(onClick = onBack) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "뒤로")
            }
            Text(text = title, fontWeight = FontWeight.SemiBold, fontSize = MaterialTheme.typography.titleMedium.fontSize)
        }

        Column(modifier = Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(20.dp)) {
            Text(text = "가지고 있는 운동기구를 선택해주세요.\n선택한 기구에 맞춰 홈트 루틴을 추천해드려요.", color = TextMuted, fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.bodyMedium.fontSize)

            categories.forEach { category ->
                Spacer(modifier = Modifier.height(24.dp))
                Text(text = category.label, color = TextMuted, fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.labelSmall.fontSize)
                Spacer(modifier = Modifier.height(10.dp))
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    category.items.forEach { item ->
                        val isSelected = item in selected
                        Surface(
                            shape = RoundedCornerShape(100.dp),
                            color = if (isSelected) SelectedBg else Color.White,
                            border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) AccentBlue else CardBorder),
                            onClick = { selected = if (isSelected) selected - item else selected + item }
                        ) {
                            Text(
                                text = item,
                                color = if (isSelected) AccentBlue else TextMuted,
                                fontWeight = FontWeight.Bold,
                                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Surface(
                shape = RoundedCornerShape(100.dp),
                color = Color.Transparent,
                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
            ) {
                Row(modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Icon(imageVector = Icons.Filled.Add, contentDescription = null, tint = TextMuted)
                    Text(text = "직접 추가", color = TextMuted, fontWeight = FontWeight.Bold)
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
                Text(text = "변경사항 저장")
            }
        }
    }
}
