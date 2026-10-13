package com.fitmate.presentation.workout

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fitmate.presentation.designsystem.R
import com.fitmate.presentation.designsystem.theme.*

/** 운동 종료 후 선택적으로 입력하는 신체 정보. null은 입력하지 않은 항목. */
data class WorkoutBodyRecord(
    val weightKg: Double? = null,
    val skeletalMuscleKg: Double? = null,
    val bodyFatPercent: Double? = null,
    val bodyFatKg: Double? = null
)

@Composable
fun WorkoutBodyRecordScreen(
    onBack: () -> Unit,
    onNext: (WorkoutBodyRecord) -> Unit,
    onSkip: () -> Unit,
    modifier: Modifier = Modifier
) {
    var weight by remember { mutableStateOf("") }
    var muscle by remember { mutableStateOf("") }
    var fatPercent by remember { mutableStateOf("") }
    var fatKg by remember { mutableStateOf("") }

    val hasInput = listOf(weight, muscle, fatPercent, fatKg).any { it.isNotBlank() }
    val valid = listOf(weight, muscle, fatPercent, fatKg).all {
        it.isBlank() || (it.toDoubleOrNull()?.let { number -> number >= 0.0 } == true)
    } && (fatPercent.toDoubleOrNull()?.let { it <= 100.0 } ?: true)

    Column(modifier.fillMaxSize().background(Color.White)) {
        Row(
            Modifier.fillMaxWidth().height(68.dp).padding(horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.size(24.dp).clickable(onClick = onBack), contentAlignment = Alignment.Center) {
                Icon(
                    painter = painterResource(R.drawable.ic_workout_back),
                    contentDescription = "뒤로가기",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(Modifier.width(12.dp))
            Text("운동 기록", fontFamily = FitMateNumberFontFamily, fontSize = 20.sp,
                fontWeight = FontWeight.Bold, color = Gray700)
        }
        HorizontalDivider(color = Gray200, thickness = 1.dp)

        Column(
            Modifier.weight(1f).fillMaxWidth().verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp).padding(top = 24.dp, bottom = 20.dp)
        ) {
            Text("오늘의 성장도 기록해볼까요?", fontFamily = FitMateNumberFontFamily,
                fontWeight = FontWeight.Bold, fontSize = 24.sp, color = Gray700,
                maxLines = 1, overflow = TextOverflow.Ellipsis)
            Spacer(Modifier.height(8.dp))
            Text("운동 기록과 함께 신체 변화를 기록하면 내 성장 변화를 더\n정확하게 확인할 수 있어요.",
                fontFamily = FitMateFontFamily, fontSize = 14.sp, lineHeight = 21.sp,
                color = Gray300)
            Spacer(Modifier.height(24.dp))

            BodyRecordField("몸무게", weight, { weight = it }, "kg")
            Spacer(Modifier.height(24.dp))
            BodyRecordField("골격근량", muscle, { muscle = it }, "kg")
            Spacer(Modifier.height(24.dp))
            BodyRecordField("체지방률", fatPercent, { fatPercent = it }, "%")
            Spacer(Modifier.height(24.dp))
            BodyRecordField("체지방량", fatKg, { fatKg = it }, "kg")
        }

        Column(
            Modifier.fillMaxWidth().padding(horizontal = 24.dp)
                .padding(top = 12.dp, bottom = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
                    .background(Color(0xFFE1F2FF), RoundedCornerShape(10.dp))
                    .border(1.dp, BorderDefault, RoundedCornerShape(10.dp))
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    Modifier.size(16.dp).background(Blue600, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("!", color = Color.White, fontSize = 11.sp,
                        fontFamily = FitMateFontFamily, fontWeight = FontWeight.Bold,
                        lineHeight = 12.sp, modifier = Modifier.offset(y = (-0.5).dp))
                }
                Text("신체 정보는 선택적으로 기록할 수 있어요.\n기록한 정보는 성장 분석에 반영돼요.",
                    fontFamily = FitMateFontFamily, fontSize = 12.sp,
                    lineHeight = 17.sp, color = Blue600)
            }
            Spacer(Modifier.height(8.dp))
            Button(
                onClick = {
                    onNext(
                        WorkoutBodyRecord(
                            weightKg = weight.toDoubleOrNull(),
                            skeletalMuscleKg = muscle.toDoubleOrNull(),
                            bodyFatPercent = fatPercent.toDoubleOrNull(),
                            bodyFatKg = fatKg.toDoubleOrNull()
                        )
                    )
                },
                enabled = hasInput && valid,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.dp, if (hasInput && valid) Blue600 else BorderDefault),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Blue600,
                    disabledContainerColor = Gray50,
                    disabledContentColor = Gray500
                )
            ) {
                Text("다음", fontFamily = FitMateFontFamily, fontWeight = FontWeight.Bold,
                    fontSize = 16.sp)
            }
            Spacer(Modifier.height(8.dp))
            OutlinedButton(
                onClick = onSkip,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                border = BorderStroke(1.dp, Blue600),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Blue600)
            ) {
                Text("건너뛰기", fontFamily = FitMateFontFamily,
                    fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    }
}

@Composable
private fun BodyRecordField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    unit: String
) {
    Column(Modifier.fillMaxWidth()) {
        Text(label, fontFamily = FitMateFontFamily, fontWeight = FontWeight.Bold,
            fontSize = 14.sp, color = Gray700)
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = value,
            onValueChange = { input ->
                if (input.isEmpty() || (input.count { it == '.' } <= 1 &&
                            input.all { it.isDigit() || it == '.' } && input.length <= 8)) {
                    onValueChange(input)
                }
            },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            placeholder = { Text("-", color = Gray700, fontFamily = FitMateFontFamily,
                fontWeight = FontWeight.Bold, fontSize = 14.sp) },
            trailingIcon = { Text(unit, color = Gray300, fontSize = 16.sp,
                modifier = Modifier.padding(end = 16.dp)) },
            singleLine = true,
            textStyle = androidx.compose.ui.text.TextStyle(
                fontFamily = FitMateFontFamily, fontWeight = FontWeight.Bold,
                fontSize = 14.sp, color = Gray700
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Blue600,
                unfocusedBorderColor = Gray200,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            )
        )
    }
}
