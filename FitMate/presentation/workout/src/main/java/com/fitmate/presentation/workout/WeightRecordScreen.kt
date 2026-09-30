package com.fitmate.presentation.workout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
private val CardBorder = Color(0xFFD5DCE2)
private val TextMuted = Color(0xFF6C757D)
private val InfoBg = Color(0xFFE1ECF4)

private const val PreviousWeightKg = 72.4

@Composable
fun WeightRecordScreen(
    onBack: () -> Unit = {},
    onRecordAndContinue: (Double?) -> Unit = {},
    onSkip: () -> Unit = {}
) {
    var input by remember { mutableStateOf("") }
    val weight = input.toDoubleOrNull()
    val delta = weight?.let { it - PreviousWeightKg }

    Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
        IconButton(onClick = onBack, modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp)) {
            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "뒤로")
        }

        Column(modifier = Modifier.weight(1f).padding(horizontal = 20.dp)) {
            Text(text = "오늘의 성장도 기록해볼까요?", fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.headlineSmall.fontSize)
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = "운동 기록과 함께 신체 변화를 기록하면 내 성장 변화를 더 정확하게 확인할 수 있어요.", color = TextMuted)

            Spacer(modifier = Modifier.height(32.dp))
            Text(text = "오늘 몸무게", color = TextMuted, fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.labelSmall.fontSize)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = input,
                onValueChange = { input = it.filter { c -> c.isDigit() || c == '.' } },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text(text = "몸무게 입력") },
                suffix = { Text(text = "kg", color = TextMuted, fontWeight = FontWeight.Bold) },
                textStyle = androidx.compose.ui.text.TextStyle(fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.headlineSmall.fontSize),
                singleLine = true,
                shape = RoundedCornerShape(14.dp)
            )

            if (weight != null && delta != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = "이전 기록 ${PreviousWeightKg}kg", color = TextMuted, fontSize = MaterialTheme.typography.labelMedium.fontSize)
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    val deltaText = (if (delta >= 0) "+" else "") + "%.1fkg".format(delta)
                    Text(text = deltaText, color = Color(0xFF42688A), fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.titleLarge.fontSize)
                    Text(
                        text = if (delta < 0) "이전 기록보다 ${"%.1f".format(-delta)}kg 감소했어요" else "이전 기록보다 ${"%.1f".format(delta)}kg 증가했어요",
                        color = TextMuted,
                        fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                color = InfoBg,
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFB0CBDC))
            ) {
                Row(modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Icon(imageVector = Icons.Filled.Info, contentDescription = null, tint = Color(0xFF27384A), modifier = Modifier.size(18.dp))
                    Text(text = "몸무게는 선택적으로 기록할 수 있어요. 기록한 정보는 성장 분석에 반영돼요.", color = Color(0xFF27384A), fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.bodySmall.fontSize)
                }
            }
        }

        Column(modifier = Modifier.padding(20.dp)) {
            Button(
                onClick = { onRecordAndContinue(weight) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = if (weight != null) FitMateBlue else Color(0xFFE2E6EA)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(text = "기록하고 다음", color = if (weight != null) Color.White else TextMuted)
            }
            TextButton(onClick = onSkip, modifier = Modifier.fillMaxWidth()) {
                Text(text = "건너뛰기", color = TextMuted)
            }
        }
    }
}
