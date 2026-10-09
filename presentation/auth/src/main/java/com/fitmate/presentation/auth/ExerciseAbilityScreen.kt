package com.fitmate.presentation.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.MonitorWeight
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
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

private data class LiftRecord(val name: String, val icon: ImageVector, var weight: String, var reps: String, var sets: String)

@Composable
private fun LiftRow(record: LiftRecord, onChange: (LiftRecord) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(16.dp))
            .border(BorderStroke(1.dp, Gray200), RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(36.dp).background(Blue200, RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) { Icon(record.icon, contentDescription = null, tint = Blue700, modifier = Modifier.size(18.dp)) }
            Text(record.name, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Gray900, modifier = Modifier.padding(start = 10.dp))
        }
        Row(modifier = Modifier.fillMaxWidth().padding(top = 14.dp)) {
            OutlinedTextField(
                value = record.weight,
                onValueChange = { onChange(record.copy(weight = it)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                trailingIcon = { Text("kg", color = Gray500, fontSize = 12.sp) },
                modifier = Modifier.weight(1f)
            )
            Text("×", fontSize = 16.sp, color = Gray200, modifier = Modifier.padding(horizontal = 8.dp).align(Alignment.CenterVertically))
            OutlinedTextField(
                value = record.reps,
                onValueChange = { onChange(record.copy(reps = it)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                trailingIcon = { Text("회", color = Gray500, fontSize = 12.sp) },
                modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
                value = record.sets,
                onValueChange = { onChange(record.copy(sets = it)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                trailingIcon = { Text("세트", color = Gray500, fontSize = 12.sp) },
                modifier = Modifier.weight(1f).padding(start = 8.dp)
            )
        }
    }
}

@Composable
fun ExerciseAbilityScreen(
    onBack: () -> Unit,
    onNext: () -> Unit
) {
    var records by remember {
        mutableStateOf(
            listOf(
                LiftRecord("벤치프레스", Icons.Default.FitnessCenter, "60", "8", "4"),
                LiftRecord("스쿼트", Icons.Default.FitnessCenter, "80", "8", "3"),
                LiftRecord("데드리프트", Icons.Default.MonitorWeight, "", "", "")
            )
        )
    }

    Column(modifier = Modifier.fillMaxSize().background(ScreenBg)) {
        SignupHeader(step = 8, onBack = onBack)

        LazyColumn(modifier = Modifier.weight(1f).padding(horizontal = 20.dp)) {
            item {
                Text(
                    "최근 운동 기록을\n알고 있나요?",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Gray900,
                    modifier = Modifier.padding(top = 24.dp)
                )
                Text(
                    "최근 수행 기록이 있다면\nAI가 시작 중량을 정하는 데 참고할 수 있어요.",
                    fontSize = 14.sp,
                    color = Gray500,
                    modifier = Modifier.padding(top = 8.dp, bottom = 20.dp)
                )

                Column {
                    records.forEachIndexed { index, record ->
                        LiftRow(record = record) { updated ->
                            records = records.toMutableList().also { it[index] = updated }
                        }
                        androidx.compose.foundation.layout.Spacer(modifier = Modifier.height(12.dp))
                    }
                }

                Row(
                    modifier = Modifier
                        .padding(vertical = 12.dp)
                        .border(BorderStroke(1.dp, Gray200), RoundedCornerShape(100.dp))
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                ) {
                    Text("+ 운동 기록 추가", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Gray500)
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp)
                        .background(Blue200, RoundedCornerShape(12.dp))
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Icon(Icons.Default.Psychology, contentDescription = null, tint = Blue700, modifier = Modifier.padding(top = 2.dp))
                    Text(
                        "입력한 기록을 기반으로\n추정 1RM과 적정 운동 강도를 계산할 수 있어요.",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Blue700,
                        modifier = Modifier.padding(start = 10.dp)
                    )
                }
            }
        }

        Column(modifier = Modifier.padding(horizontal = 20.dp).padding(bottom = 24.dp)) {
            Button(
                onClick = onNext,
                colors = ButtonDefaults.buttonColors(containerColor = Blue700),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth().height(52.dp)
            ) { Text("분석하기", fontSize = 16.sp, fontWeight = FontWeight.Bold) }

            OutlinedButton(
                onClick = onNext,
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Blue700),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth().height(52.dp).padding(top = 8.dp)
            ) { Text("건너뛰기", fontSize = 16.sp, fontWeight = FontWeight.Bold) }
        }
    }
}
