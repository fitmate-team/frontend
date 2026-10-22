package com.fitmate.presentation.auth

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fitmate.presentation.designsystem.component.FitMateOutlinedButton
import com.fitmate.presentation.designsystem.component.FitMatePrimaryButton
import com.fitmate.presentation.designsystem.component.InfoBanner
import com.fitmate.presentation.designsystem.component.UnitTextField
import com.fitmate.presentation.designsystem.theme.FitMateTheme
import com.fitmate.presentation.designsystem.theme.Spacing6

// 1.3 회원가입 (최근 신체 측정 정보)
@Composable
fun OptionalBodyInfoScreen(
    muscleMass: String,
    bodyFatPercent: String,
    bodyFatMass: String,
    onMuscleMassChange: (String) -> Unit,
    onBodyFatPercentChange: (String) -> Unit,
    onBodyFatMassChange: (String) -> Unit,
    onBack: () -> Unit,
    onNext: () -> Unit,
    onSkip: () -> Unit
) {
    SignupStepScaffold(
        step = 2,
        onBack = onBack,
        title = "최근 신체 측정 정보가 있나요?",
        subtitle = "모르거나 최근 측정하지 않았다면 건너뛸 수 있어요.",
        contentTopSpacing = 56.dp,
        bottomBar = {
            FitMatePrimaryButton(text = "다음", onClick = onNext)
            FitMateOutlinedButton(text = "건너뛰기", onClick = onSkip)
        }
    ) {
        SignupLabeledField(label = "골격근량") {
            UnitTextField(
                value = muscleMass,
                onValueChange = onMuscleMassChange,
                unit = "kg",
                keyboardType = KeyboardType.Decimal
            )
        }
        Spacer(modifier = Modifier.height(Spacing6))
        SignupLabeledField(label = "체지방률") {
            UnitTextField(
                value = bodyFatPercent,
                onValueChange = onBodyFatPercentChange,
                unit = "%",
                keyboardType = KeyboardType.Decimal
            )
        }
        Spacer(modifier = Modifier.height(Spacing6))
        SignupLabeledField(label = "체지방량") {
            UnitTextField(
                value = bodyFatMass,
                onValueChange = onBodyFatMassChange,
                unit = "kg",
                keyboardType = KeyboardType.Decimal
            )
        }
        Spacer(modifier = Modifier.height(Spacing6))
        InfoBanner(text = "최근 측정한 정보를 입력할수록\n더 세밀한 운동 추천에 활용할 수 있어요.")
    }
}

@Preview(widthDp = 394, heightDp = 892)
@Composable
private fun OptionalBodyInfoScreenPreview() {
    FitMateTheme {
        OptionalBodyInfoScreen(
            muscleMass = "",
            bodyFatPercent = "",
            bodyFatMass = "",
            onMuscleMassChange = {},
            onBodyFatPercentChange = {},
            onBodyFatMassChange = {},
            onBack = {},
            onNext = {},
            onSkip = {}
        )
    }
}
