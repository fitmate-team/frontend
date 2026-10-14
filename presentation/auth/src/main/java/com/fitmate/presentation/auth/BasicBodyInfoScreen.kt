package com.fitmate.presentation.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fitmate.presentation.designsystem.component.FitMatePrimaryButton
import com.fitmate.presentation.designsystem.component.SelectableBox
import com.fitmate.presentation.designsystem.component.UnitTextField
import com.fitmate.presentation.designsystem.theme.Blue500
import com.fitmate.presentation.designsystem.theme.FitMateTheme
import com.fitmate.presentation.designsystem.theme.Spacing6

private val Genders = listOf("남성", "여성")

// 1.2 회원가입 (기본 정보)
@Composable
fun BasicBodyInfoScreen(
    onBack: () -> Unit,
    onNext: () -> Unit
) {
    var gender by remember { mutableStateOf<String?>(null) }
    var age by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }

    SignupStepScaffold(
        step = 1,
        onBack = onBack,
        title = "기본 정보를 알려주세요",
        subtitle = "더 정확한 운동 추천에 활용돼요.",
        contentTopSpacing = 56.dp,
        bottomBar = { FitMatePrimaryButton(text = "다음", onClick = onNext) }
    ) {
        SignupLabeledField(label = "성별") {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Genders.forEach { option ->
                    SelectableBox(
                        text = option,
                        selected = gender == option,
                        onClick = { gender = option },
                        selectedTextColor = Blue500,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(Spacing6))
        SignupLabeledField(label = "나이") {
            UnitTextField(value = age, onValueChange = { age = it }, unit = "세")
        }
        Spacer(modifier = Modifier.height(Spacing6))
        SignupLabeledField(label = "키") {
            UnitTextField(value = height, onValueChange = { height = it }, unit = "cm")
        }
        Spacer(modifier = Modifier.height(Spacing6))
        SignupLabeledField(label = "몸무게") {
            UnitTextField(value = weight, onValueChange = { weight = it }, unit = "kg")
        }
    }
}

@Preview(widthDp = 394, heightDp = 892)
@Composable
private fun BasicBodyInfoScreenPreview() {
    FitMateTheme {
        BasicBodyInfoScreen(onBack = {}, onNext = {})
    }
}
