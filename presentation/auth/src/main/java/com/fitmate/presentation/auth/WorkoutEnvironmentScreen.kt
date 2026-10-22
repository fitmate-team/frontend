package com.fitmate.presentation.auth

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.fitmate.presentation.designsystem.component.FitMatePrimaryButton
import com.fitmate.presentation.designsystem.component.FitMateTextField
import com.fitmate.presentation.designsystem.component.WORKOUT_PLACE_GYM
import com.fitmate.presentation.designsystem.component.WORKOUT_PLACE_HOME
import com.fitmate.presentation.designsystem.component.WORKOUT_PLACE_OUTDOOR
import com.fitmate.presentation.designsystem.component.WorkoutPlaceSelector
import com.fitmate.presentation.designsystem.theme.FitMateTheme
import com.fitmate.presentation.designsystem.theme.fitMateTextStyle

const val ENV_GYM = WORKOUT_PLACE_GYM
const val ENV_HOME = WORKOUT_PLACE_HOME
const val ENV_OUTDOOR = WORKOUT_PLACE_OUTDOOR

// 1.8 회원가입 (운동 장소). 헬스장을 고르면 헬스장 이름 입력칸이 펼쳐진다
@Composable
fun WorkoutEnvironmentScreen(
    selectedEnv: String?,
    gymName: String,
    onEnvSelect: (String) -> Unit,
    onGymNameChange: (String) -> Unit,
    onBack: () -> Unit,
    onNext: () -> Unit
) {
    SignupStepScaffold(
        step = 7,
        onBack = onBack,
        title = "주로 어디에서 운동하나요?",
        subtitle = "운동 장소에 맞는 운동을 추천할게요.",
        bottomBar = {
            FitMatePrimaryButton(text = "다음", onClick = onNext)
        }
    ) {
        WorkoutPlaceSelector(selected = selectedEnv, onSelect = onEnvSelect) {
            FitMateTextField(
                value = gymName,
                onValueChange = onGymNameChange,
                placeholder = "헬스장 이름을 입력해주세요.",
                textStyle = fitMateTextStyle(14.sp, 20.sp)
            )
        }
    }
}

@Preview(widthDp = 394, heightDp = 892)
@Composable
private fun WorkoutEnvironmentScreenPreview() {
    FitMateTheme {
        WorkoutEnvironmentScreen(
            selectedEnv = ENV_GYM,
            gymName = "",
            onEnvSelect = {},
            onGymNameChange = {},
            onBack = {},
            onNext = {}
        )
    }
}
