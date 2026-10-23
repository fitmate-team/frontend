package com.fitmate.presentation.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fitmate.domain.model.Gender
import com.fitmate.presentation.designsystem.component.FitMatePrimaryButton
import com.fitmate.presentation.designsystem.component.SelectableBox
import com.fitmate.presentation.designsystem.component.UnitTextField
import com.fitmate.presentation.designsystem.theme.Blue500
import com.fitmate.presentation.designsystem.theme.FitMateTheme
import com.fitmate.presentation.designsystem.theme.Spacing6

private val Genders = listOf(Gender.MALE to "남성", Gender.FEMALE to "여성")

// 1.2 회원가입 (기본 정보)
@Composable
fun BasicBodyInfoScreen(
    gender: Gender?,
    age: String,
    height: String,
    weight: String,
    onGenderSelect: (Gender) -> Unit,
    onAgeChange: (String) -> Unit,
    onHeightChange: (String) -> Unit,
    onWeightChange: (String) -> Unit,
    onBack: () -> Unit,
    onNext: () -> Unit
) {
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
                Genders.forEach { (option, label) ->
                    SelectableBox(
                        text = label,
                        selected = gender == option,
                        onClick = { onGenderSelect(option) },
                        selectedTextColor = Blue500,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(Spacing6))
        SignupLabeledField(label = "나이") {
            UnitTextField(value = age, onValueChange = onAgeChange, unit = "세")
        }
        Spacer(modifier = Modifier.height(Spacing6))
        SignupLabeledField(label = "키") {
            UnitTextField(value = height, onValueChange = onHeightChange, unit = "cm", keyboardType = KeyboardType.Decimal)
        }
        Spacer(modifier = Modifier.height(Spacing6))
        SignupLabeledField(label = "몸무게") {
            UnitTextField(value = weight, onValueChange = onWeightChange, unit = "kg", keyboardType = KeyboardType.Decimal)
        }
    }
}

@Preview(widthDp = 394, heightDp = 892)
@Composable
private fun BasicBodyInfoScreenPreview() {
    FitMateTheme {
        BasicBodyInfoScreen(
            gender = Gender.FEMALE,
            age = "25",
            height = "",
            weight = "",
            onGenderSelect = {},
            onAgeChange = {},
            onHeightChange = {},
            onWeightChange = {},
            onBack = {},
            onNext = {}
        )
    }
}
