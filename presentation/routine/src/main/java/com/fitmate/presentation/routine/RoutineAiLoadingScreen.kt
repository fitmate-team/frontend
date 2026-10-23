package com.fitmate.presentation.routine

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.tooling.preview.Preview
import com.fitmate.presentation.designsystem.component.AiAnalyzingScreen
import com.fitmate.presentation.designsystem.theme.FitMateTheme
import kotlinx.coroutines.delay

// 3.5 / 3.6 추천 로딩. 분석이 끝나면 onComplete
@Composable
fun RoutineAiLoadingScreen(onComplete: () -> Unit = {}) {
    LaunchedEffect(Unit) {
        delay(1600)
        onComplete()
    }
    AiAnalyzingScreen()
}

@Preview(widthDp = 394, heightDp = 892)
@Composable
private fun RoutineAiLoadingScreenPreview() {
    FitMateTheme { AiAnalyzingScreen() }
}
