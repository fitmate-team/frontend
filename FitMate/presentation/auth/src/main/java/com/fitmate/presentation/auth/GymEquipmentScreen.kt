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
import androidx.compose.foundation.lazy.items
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
    Column(modifier = Modifier.padding(top = 28.dp)) {
        Text(title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Gray500)
        FlowChips(items = items, selected = selected, onToggle = onToggle, modifier = Modifier.padding(top = 12.dp))
    }
}

@Composable
private fun FlowChips(items: List<String>, selected: Set<String>, onToggle: (String) -> Unit, modifier: Modifier = Modifier) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items.chunked(3).forEach { rowItems ->
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                rowItems.forEach { item ->
                    EquipmentChip(label = item, isSelected = item in selected, onClick = { onToggle(item) })
                }
            }
        }
    }
}

@Composable
fun GymEquipmentScreen(
    onBack: () -> Unit,
    onNext: () -> Unit
) {
    var selected by remember { mutableStateOf(setOf("바벨", "덤벨", "벤치", "레그프레스", "랫풀다운", "케이블", "러닝머신")) }
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

                EquipmentGroup("프리웨이트", listOf("바벨", "덤벨", "벤치", "케틀벨"), selected, ::toggle)
                EquipmentGroup("머신", listOf("스미스머신", "레그프레스", "랫풀다운", "케이블", "레그컬", "레그익스텐션"), selected, ::toggle)
                EquipmentGroup("유산소", listOf("러닝머신", "사이클", "스텝밀", "로잉머신"), selected, ::toggle)
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
