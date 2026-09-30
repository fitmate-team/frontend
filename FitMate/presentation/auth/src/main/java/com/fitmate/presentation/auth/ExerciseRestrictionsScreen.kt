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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
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
private fun AreaChip(label: String, isSelected: Boolean, onClick: () -> Unit) {
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
fun ExerciseRestrictionsScreen(
    onBack: () -> Unit,
    onAddExercise: () -> Unit,
    onNext: () -> Unit
) {
    var noneSelected by remember { mutableStateOf(false) }
    var selectedAreas by remember { mutableStateOf(setOf<String>()) }

    Column(modifier = Modifier.fillMaxSize().background(ScreenBg)) {
        SignupHeader(step = 7, onBack = onBack)

        LazyColumn(modifier = Modifier.weight(1f).padding(horizontal = 20.dp)) {
            item {
                Text(
                    "운동할 때 고려해야 할\n부분이 있나요?",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Gray900,
                    modifier = Modifier.padding(top = 24.dp)
                )
                Text(
                    "부담을 느끼는 부위나 피하고 싶은 운동을 설정하면\n운동 추천 시 고려할게요.",
                    fontSize = 14.sp,
                    color = Gray500,
                    modifier = Modifier.padding(top = 8.dp, bottom = 24.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { noneSelected = !noneSelected }
                        .background(Color.White, RoundedCornerShape(12.dp))
                        .border(BorderStroke(1.dp, Gray200), RoundedCornerShape(12.dp))
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier.size(22.dp).background(if (noneSelected) Blue700 else Color.Transparent, CircleShape)
                            .border(BorderStroke(1.dp, if (noneSelected) Blue700 else Gray200), CircleShape)
                    )
                    Text("특별히 없어요", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Gray900, modifier = Modifier.padding(start = 12.dp))
                }

                Text("신경 쓰이는 부위", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Gray900, modifier = Modifier.padding(top = 24.dp, bottom = 12.dp))
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("허리", "무릎", "어깨", "손목", "발목", "목").chunked(3).forEach { rowItems ->
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            rowItems.forEach { area ->
                                AreaChip(label = area, isSelected = area in selectedAreas, onClick = {
                                    selectedAreas = if (area in selectedAreas) selectedAreas - area else selectedAreas + area
                                })
                            }
                        }
                    }
                }

                Text("피하고 싶은 운동", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Gray900, modifier = Modifier.padding(top = 24.dp, bottom = 12.dp))
                Row(
                    modifier = Modifier
                        .clickable(onClick = onAddExercise)
                        .border(BorderStroke(1.dp, Gray200), RoundedCornerShape(100.dp))
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, tint = Gray500, modifier = Modifier.size(16.dp))
                    Text("운동 추가", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Gray500, modifier = Modifier.padding(start = 4.dp))
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp, bottom = 24.dp)
                        .background(Blue200, RoundedCornerShape(12.dp))
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Icon(Icons.Default.Info, contentDescription = null, tint = Blue700, modifier = Modifier.padding(top = 2.dp))
                    Text(
                        "입력한 정보는 운동 선호 및 제한 설정에만 사용되며, 의학적 진단이나 치료 목적으로 활용되지 않아요.",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Blue700,
                        modifier = Modifier.padding(start = 10.dp)
                    )
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
