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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
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

private data class ExerciseItem(val name: String, val tag: String)

@Composable
fun ExerciseSearchScreen(
    onBack: () -> Unit,
    onConfirm: () -> Unit
) {
    var query by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("전체") }
    val exercises = remember {
        listOf(
            ExerciseItem("벤치프레스", "가슴 · 프리웨이트"),
            ExerciseItem("체스트프레스", "가슴 · 머신"),
            ExerciseItem("인클라인 덤벨프레스", "가슴 · 프리웨이트"),
            ExerciseItem("숄더프레스", "어깨 · 프리웨이트"),
            ExerciseItem("랫풀다운", "등 · 머신"),
            ExerciseItem("스쿼트", "하체 · 프리웨이트"),
            ExerciseItem("레그프레스", "하체 · 머신")
        )
    }
    var selectedExercises by remember { mutableStateOf(setOf("벤치프레스", "체스트프레스", "인클라인 덤벨프레스", "숄더프레스")) }

    Column(modifier = Modifier.fillMaxSize().background(ScreenBg)) {
        Row(modifier = Modifier.fillMaxWidth().padding(top = 16.dp, start = 4.dp, end = 20.dp), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "뒤로", tint = Gray900) }
            Text("운동 검색", fontSize = 18.sp, color = Gray900)
        }

        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Gray500) },
            placeholder = { Text("운동 검색") },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 16.dp)
        )

        LazyRow(
            modifier = Modifier.padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(listOf("전체", "가슴", "등", "하체", "어깨", "팔", "코어", "유산소")) { category ->
                val isSelected = selectedCategory == category
                Box(
                    modifier = Modifier
                        .clickable { selectedCategory = category }
                        .background(if (isSelected) Blue200 else Color.White, RoundedCornerShape(100.dp))
                        .border(BorderStroke(1.dp, if (isSelected) Blue700 else Gray200), RoundedCornerShape(100.dp))
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                ) { Text(category, fontSize = 13.sp, fontWeight = FontWeight.Medium, color = if (isSelected) Blue700 else Gray500) }
            }
        }

        LazyColumn(
            modifier = Modifier.weight(1f).padding(horizontal = 20.dp).padding(top = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(exercises) { exercise ->
                val isSelected = exercise.name in selectedExercises
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            selectedExercises = if (isSelected) selectedExercises - exercise.name else selectedExercises + exercise.name
                        }
                        .background(if (isSelected) Blue200 else Color.White, RoundedCornerShape(14.dp))
                        .border(BorderStroke(1.dp, if (isSelected) Blue700 else Gray200), RoundedCornerShape(14.dp))
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(exercise.name, fontSize = 15.sp, fontWeight = FontWeight.Medium, color = if (isSelected) Blue700 else Gray900)
                        Text(exercise.tag, fontSize = 12.sp, color = Gray500, modifier = Modifier.padding(top = 3.dp))
                    }
                    Box(
                        modifier = Modifier.size(36.dp).background(if (isSelected) Blue700 else ScreenBg, RoundedCornerShape(10.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            if (isSelected) Icons.Default.Check else Icons.Default.Add,
                            contentDescription = null,
                            tint = if (isSelected) Color.White else Gray500
                        )
                    }
                }
            }
        }

        Button(
            onClick = onConfirm,
            colors = ButtonDefaults.buttonColors(containerColor = Blue700),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp).padding(vertical = 16.dp).height(52.dp)
        ) { Text("확인", fontSize = 16.sp, fontWeight = FontWeight.Bold) }
    }
}
