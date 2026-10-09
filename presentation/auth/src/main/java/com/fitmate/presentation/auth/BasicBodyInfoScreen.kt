package com.fitmate.presentation.auth

import androidx.compose.foundation.background
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
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
    Row(
        modifier = Modifier.fillMaxWidth().height(80.dp).padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBack) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "뒤로", tint = Gray900)
        }
        Box(
            modifier = Modifier.weight(1f).padding(horizontal = 8.dp).height(4.dp).background(Gray200, RoundedCornerShape(2.dp))
        ) {
            Box(modifier = Modifier.fillMaxWidth(step / 8f).height(4.dp).background(Blue700, RoundedCornerShape(2.dp)))
        }
        Text("$step / 8", fontSize = 12.sp, color = Gray500, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun BasicBodyInfoScreen(
    onBack: () -> Unit,
    onNext: () -> Unit
) {
    var nickname by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf<String?>(null) }
    var age by remember { mutableStateOf(24) }
    var height by remember { mutableStateOf(175) }
    var weight by remember { mutableStateOf(72) }

    Column(modifier = Modifier.fillMaxSize().background(ScreenBg)) {
        SignupHeader(step = 1, onBack = onBack)

        Column(modifier = Modifier.weight(1f).padding(20.dp)) {
            Text("기본 정보를 알려주세요", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Gray900)
            Text("더 정확한 운동 추천에 활용돼요.", fontSize = 14.sp, color = Gray500, modifier = Modifier.padding(top = 8.dp, bottom = 20.dp))

            Text("닉네임", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Gray900)
            OutlinedTextField(value = nickname, onValueChange = { nickname = it }, singleLine = true, modifier = Modifier.fillMaxWidth().padding(top = 8.dp, bottom = 20.dp))

            Text("성별", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Gray900)
            Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp, bottom = 20.dp)) {
                listOf("남성", "여성").forEach { g ->
                    OutlinedButton(
                        onClick = { gender = g },
                        colors = if (gender == g) ButtonDefaults.outlinedButtonColors(containerColor = Blue200) else ButtonDefaults.outlinedButtonColors(),
                        modifier = Modifier.weight(1f).padding(end = if (g == "남성") 12.dp else 0.dp)
                    ) { Text(g) }
                }
            }

            NumberStepperField(label = "나이", value = age, unit = "세", onChange = { age = it })
            NumberStepperField(label = "키", value = height, unit = "cm", onChange = { height = it })
            NumberStepperField(label = "몸무게", value = weight, unit = "kg", onChange = { weight = it })
        }

        Button(
            onClick = onNext,
            colors = ButtonDefaults.buttonColors(containerColor = Blue700),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp).padding(bottom = 24.dp).height(52.dp)
        ) {
            Text("다음", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun NumberStepperField(label: String, value: Int, unit: String, onChange: (Int) -> Unit) {
    Column(modifier = Modifier.padding(bottom = 20.dp)) {
        Text(label, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Gray900)
        OutlinedTextField(
            value = value.toString(),
            onValueChange = { it.toIntOrNull()?.let(onChange) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            trailingIcon = { Text(unit, color = Gray500) },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        )
    }
}
