package com.fitmate.presentation.auth

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.DirectionsRun
import androidx.compose.material.icons.rounded.Balance
import androidx.compose.material.icons.rounded.FitnessCenter
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fitmate.presentation.designsystem.component.FitMatePrimaryButton
import com.fitmate.presentation.designsystem.component.SelectOptionCard
import com.fitmate.presentation.designsystem.theme.FitMateTheme
import com.fitmate.presentation.designsystem.theme.Spacing3

private data class MethodOption(val title: String, val description: String, val icon: ImageVector)

private val MethodOptions = listOf(
    MethodOption("근력 운동 중심", "근육량을 유지하면서 웨이트 비중을 높여요", Icons.Rounded.FitnessCenter),
    MethodOption("유산소 중심", "러닝, 사이클 등 유산소 비중을 높여요", Icons.AutoMirrored.Rounded.DirectionsRun),
    MethodOption("균형형", "근력 운동과 유산소를 균형 있게 진행해요", Icons.Rounded.Balance)
)

// 1.6 회원가입 (운동 방식)
@Composable
fun WorkoutMethodScreen(
    onBack: () -> Unit,
    onNext: () -> Unit
) {
    var selectedMethod by remember { mutableStateOf<String?>(null) }

    SignupStepScaffold(
        step = 5,
        onBack = onBack,
        title = "어떤 방식으로 운동할까요?",
        subtitle = "운동 비중을 선택하면 AI가 루틴 구성에 반영해요.",
        bottomBar = { FitMatePrimaryButton(text = "다음", onClick = onNext) }
    ) {
        MethodOptions.forEachIndexed { index, option ->
            if (index > 0) Spacer(modifier = Modifier.height(Spacing3))
            SelectOptionCard(
                icon = option.icon,
                title = option.title,
                description = option.description,
                selected = selectedMethod == option.title,
                onClick = { selectedMethod = option.title },
                iconSize = 28.dp,
                height = 88.dp
            )
        }
    }
}

@Preview(widthDp = 394, heightDp = 892)
@Composable
private fun WorkoutMethodScreenPreview() {
    FitMateTheme {
        WorkoutMethodScreen(onBack = {}, onNext = {})
    }
}
