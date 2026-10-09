package com.fitmate.presentation.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.unit.sp

private val Blue700 = Color(0xFF3E78AD)
private val Blue200 = Color(0xFFDBE9F5)
private val Gray200 = Color(0xFFE2E6EA)
private val Gray500 = Color(0xFF6C757D)
private val Gray900 = Color(0xFF16181A)
private val ScreenBg = Color(0xFFF8F9FA)

@Composable
private fun SignupHeader(step: Int, onBack: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth().height(80.dp).padding(horizontal = 20.dp), verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "뒤로", tint = Gray900) }
        Box(modifier = Modifier.weight(1f).padding(horizontal = 8.dp).height(4.dp).background(Gray200, RoundedCornerShape(2.dp))) {
            Box(modifier = Modifier.fillMaxWidth(step / 8f).height(4.dp).background(Blue700, RoundedCornerShape(2.dp)))
        }
        Text("$step / 8", fontSize = 12.sp, color = Gray500, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun EquipmentChip(label: String, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clickable(onClick = onClick)
            .background(if (isSelected) Blue200 else Color.White, RoundedCornerShape(100.dp))
            .border(BorderStroke(1.dp, if (isSelected) Blue700 else Gray200), RoundedCornerShape(100.dp))
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Text(label, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = if (isSelected) Blue700 else Gray500)
    }
}

@Composable
private fun EquipmentGroup(title: String, items: List<String>, selected: Set<String>, onToggle: (String) -> Unit) {
    Column(modifier = Modifier.padding(top = 24.dp)) {
        Text(title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Gray500)
        Column(modifier = Modifier.padding(top = 12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items.chunked(3).forEach { rowItems ->
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    rowItems.forEach { item -> EquipmentChip(label = item, isSelected = item in selected, onClick = { onToggle(item) }) }
                }
            }
        }
    }
}

@Composable
fun HomeEquipmentScreen(
    onBack: () -> Unit,
    onNext: () -> Unit
) {
    var selected by remember {
        mutableStateOf(setOf("운동매트", "요가블록", "폼룰러", "조절식 덤벨", "케틀벨", "바벨", "미니 밴드", "튜빙 밴드", "푸쉬운동", "딥스바", "조절식 밴치", "러닝머신"))
    }
    fun toggle(item: String) { selected = if (item in selected) selected - item else selected + item }

    Column(modifier = Modifier.fillMaxSize().background(ScreenBg)) {
        SignupHeader(step = 7, onBack = onBack)

        LazyColumn(modifier = Modifier.weight(1f).padding(horizontal = 20.dp)) {
            item {
                Text(
                    "어떤 운동기구를\n사용할 수 있나요?",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Gray900,
                    modifier = Modifier.padding(top = 24.dp)
                )
                Text("없는 기구가 필요하면 다른 운동으로 바꿔드릴게요.", fontSize = 14.sp, color = Gray500, modifier = Modifier.padding(top = 8.dp))

                EquipmentGroup("기본장비", listOf("운동매트", "요가블록", "폼룰러"), selected, ::toggle)
                EquipmentGroup("웨이트", listOf("덤벨", "조절식 덤벨", "케틀벨", "바벨", "원판"), selected, ::toggle)
                EquipmentGroup("저항 운동", listOf("밴드", "미니 밴드", "튜빙 밴드"), selected, ::toggle)
                EquipmentGroup("상체 운동", listOf("풀업바", "푸쉬운동", "딥스바"), selected, ::toggle)
                EquipmentGroup("벤치/보조", listOf("플랫 벤치", "조절식 밴치"), selected, ::toggle)
                EquipmentGroup("유산소", listOf("러닝머신", "실내 사이클", "스텝퍼", "줄넘기", "로잉머신"), selected, ::toggle)
            }
        }

        Button(
            onClick = onNext,
            colors = ButtonDefaults.buttonColors(containerColor = Blue700),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp).padding(vertical = 16.dp).height(52.dp)
        ) { Text("다음", fontSize = 16.sp, fontWeight = FontWeight.Bold) }
    }
}
