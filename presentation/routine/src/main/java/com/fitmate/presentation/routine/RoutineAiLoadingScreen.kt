package com.fitmate.presentation.routine

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.fitmate.presentation.designsystem.component.AiAnalyzingScreen
import com.fitmate.presentation.designsystem.theme.FitMateTheme

// 3.5 / 3.6 추천 로딩. AI 요청이 끝나면 ViewModel 이벤트로 다음 화면으로 간다
@Composable
fun RoutineAiLoadingScreen() {
    AiAnalyzingScreen()
}

@Preview(widthDp = 394, heightDp = 892)
@Composable
private fun RoutineAiLoadingScreenPreview() {
    FitMateTheme { AiAnalyzingScreen() }
}
