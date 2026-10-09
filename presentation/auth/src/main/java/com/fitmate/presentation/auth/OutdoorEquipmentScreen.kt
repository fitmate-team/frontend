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
fun OutdoorEquipmentScreen(
    onBack: () -> Unit,
    onNext: () -> Unit
) {
    var selected by remember { mutableStateOf(setOf<String>()) }
    fun toggle(item: String) { selected = if (item in selected) selected - item else selected + item }

    Column(modifier = Modifier.fillMaxSize().background(ScreenBg)) {
        SignupHeader(step = 7, onBack = onBack)

        Column(modifier = Modifier.weight(1f).padding(20.dp)) {
            Text(
                "어떤 운동기구를\n사용할 수 있나요?",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Gray900
            )
            Text("없는 기구가 필요하면 다른 운동으로 바꿔드릴게요.", fontSize = 14.sp, color = Gray500, modifier = Modifier.padding(top = 8.dp, bottom = 32.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("자전거", "줄넘기").forEach { item ->
                    EquipmentChip(label = item, isSelected = item in selected, onClick = { toggle(item) })
                }
            }
        }

        Button(
            onClick = onNext,
            colors = ButtonDefaults.buttonColors(containerColor = Blue700),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp).padding(bottom = 24.dp).height(52.dp)
        ) { Text("다음", fontSize = 16.sp, fontWeight = FontWeight.Bold) }
    }
}
