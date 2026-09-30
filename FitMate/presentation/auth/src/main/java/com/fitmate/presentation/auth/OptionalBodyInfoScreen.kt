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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Blue700 = Color(0xFF3E78AD)
private val Blue200 = Color(0xFFDBE9F5)
private val Blue400 = Color(0xFF9BC0DE)
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
fun OptionalBodyInfoScreen(
    onBack: () -> Unit,
    onNext: () -> Unit
) {
    var muscleMass by remember { mutableStateOf("") }
    var bodyFatPercent by remember { mutableStateOf("") }
    var bodyFatMass by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().background(ScreenBg)) {
        SignupHeader(step = 2, onBack = onBack)

        Column(modifier = Modifier.weight(1f).padding(20.dp)) {
            Text("최근 신체 측정 정보가 있나요?", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Gray900)
            Text("선택 입력이에요.\n모르거나 최근 측정하지 않았다면 건너뛸 수 있어요.", fontSize = 14.sp, color = Gray500, modifier = Modifier.padding(top = 8.dp, bottom = 24.dp))

            OptionalMeasureField(label = "골격근량", value = muscleMass, unit = "kg", onChange = { muscleMass = it })
            OptionalMeasureField(label = "체지방률", value = bodyFatPercent, unit = "%", onChange = { bodyFatPercent = it })
            OptionalMeasureField(label = "체지방량", value = bodyFatMass, unit = "kg", onChange = { bodyFatMass = it })

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Blue200, RoundedCornerShape(12.dp))
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Icon(Icons.Default.Info, contentDescription = null, tint = Blue700, modifier = Modifier.padding(top = 2.dp))
                Text(
                    "최근 측정한 정보를 입력할수록\n더 세밀한 운동 추천에 활용할 수 있어요.",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Blue700,
                    modifier = Modifier.padding(start = 10.dp)
                )
            }
        }

        Column(modifier = Modifier.padding(horizontal = 20.dp).padding(bottom = 24.dp)) {
            Button(
                onClick = onNext,
                colors = ButtonDefaults.buttonColors(containerColor = Blue700),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth().height(52.dp)
            ) { Text("다음", fontSize = 16.sp, fontWeight = FontWeight.Bold) }

            OutlinedButton(
                onClick = onNext,
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Blue700),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth().height(52.dp).padding(top = 8.dp)
            ) { Text("건너뛰기", fontSize = 16.sp, fontWeight = FontWeight.Bold) }
        }
    }
}

@Composable
private fun OptionalMeasureField(label: String, value: String, unit: String, onChange: (String) -> Unit) {
    Column(modifier = Modifier.padding(bottom = 16.dp)) {
        Text(label, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Gray900)
        OutlinedTextField(
            value = value,
            onValueChange = onChange,
            placeholder = { Text("—") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            trailingIcon = { Text(unit, color = Gray500) },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        )
    }
}
