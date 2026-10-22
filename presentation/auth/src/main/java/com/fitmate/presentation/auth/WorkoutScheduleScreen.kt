package com.fitmate.presentation.auth

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fitmate.presentation.designsystem.component.DaySelectorRow
import com.fitmate.presentation.designsystem.component.FitMatePrimaryButton
import com.fitmate.presentation.designsystem.component.WorkoutDurationSelector
import com.fitmate.presentation.designsystem.theme.FitMateTheme
import com.fitmate.presentation.designsystem.theme.Spacing8

// 1.7 회원가입 (운동 시간) - 운동 가능한 요일 + 한 번에 운동할 수 있는 시간
@Composable
fun WorkoutScheduleScreen(
    selectedDays: Set<String>,
    selectedDuration: String?,
    onDayToggle: (String) -> Unit,
    onDurationSelect: (String) -> Unit,
    onBack: () -> Unit,
    onNext: () -> Unit
) {
    SignupStepScaffold(
        step = 6,
        onBack = onBack,
        title = "언제, 얼마나 운동할 수 있나요?",
        subtitle = "운동할 수 있는 요일과 한 번에 가능한 시간을 알려주세요.",
        contentTopSpacing = 28.dp,
        bottomBar = { FitMatePrimaryButton(text = "다음", onClick = onNext) }
    ) {
        SignupLabeledField(label = "운동 가능한 요일") {
            DaySelectorRow(selectedDays = selectedDays, onToggle = onDayToggle)
        }
        Spacer(modifier = Modifier.height(Spacing8))
        SignupLabeledField(label = "한 번에 운동할 수 있는 시간") {
            WorkoutDurationSelector(selected = selectedDuration, onSelect = onDurationSelect)
        }
    }
}

@Preview(widthDp = 394, heightDp = 892)
@Composable
private fun WorkoutScheduleScreenPreview() {
    FitMateTheme {
        WorkoutScheduleScreen(
            selectedDays = setOf("월", "수"),
            selectedDuration = "60분",
            onDayToggle = {},
            onDurationSelect = {},
            onBack = {},
            onNext = {}
        )
    }
}
