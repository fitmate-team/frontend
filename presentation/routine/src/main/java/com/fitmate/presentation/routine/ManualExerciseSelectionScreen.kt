package com.fitmate.presentation.routine

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.fitmate.presentation.designsystem.component.ExerciseSearchLayout
import com.fitmate.presentation.designsystem.component.FitMatePrimaryButton
import com.fitmate.presentation.designsystem.theme.FitMateTheme

// 3.5 운동 검색 / 3.6 운동 선택 및 설정
// 아무것도 고르지 않았으면 AI에게 추천받고, 고른 운동이 있으면 세부 설정으로 넘어간다
@Composable
fun ManualExerciseSelectionScreen(
    initialSelected: List<String> = emptyList(),
    onBack: () -> Unit = {},
    onAiRecommend: () -> Unit = {},
    onNext: (List<String>) -> Unit = {}
) {
    var selected by rememberSaveable { mutableStateOf(initialSelected) }

    ExerciseSearchLayout(
        exercises = RoutineSearchableExercises,
        selected = selected,
        onToggle = { name -> selected = if (name in selected) selected - name else selected + name },
        onBack = onBack
    ) {
        if (selected.isEmpty()) {
            FitMatePrimaryButton(text = "AI에게 추천받기", onClick = onAiRecommend)
        } else {
            FitMatePrimaryButton(text = "선택한 운동 설정하기 (${selected.size})", onClick = { onNext(selected) })
        }
    }
}

@Preview(name = "운동 검색", widthDp = 394, heightDp = 892)
@Composable
private fun ManualExerciseSelectionScreenPreview() {
    FitMateTheme { ManualExerciseSelectionScreen() }
}

@Preview(name = "운동 선택 및 설정", widthDp = 394, heightDp = 892)
@Composable
private fun ManualExerciseSelectionScreenSelectedPreview() {
    FitMateTheme { ManualExerciseSelectionScreen(initialSelected = AiRecommendedExerciseNames) }
}
