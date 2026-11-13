package com.fitmate.presentation.routine

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.fitmate.presentation.designsystem.component.ExerciseSearchLayout
import com.fitmate.presentation.designsystem.component.FitMatePrimaryButton
import com.fitmate.presentation.designsystem.component.SearchableExercise
import com.fitmate.presentation.designsystem.theme.FitMateTheme

// 미리보기용 예시 데이터
private val SampleSearchableExercises = listOf(
    SearchableExercise("벤치 프레스", "가슴 · 프리웨이트", "가슴"),
    SearchableExercise("숄더 프레스", "어깨 · 프리웨이트", "어깨"),
    SearchableExercise("체스트 프레스", "가슴 · 머신/케이블", "가슴"),
    SearchableExercise("레그 프레스", "하체 · 머신/케이블", "하체/둔근"),
    SearchableExercise("러닝머신", "유산소 · 유산소 기구", "전신")
)

// 3.5 운동 검색 / 3.6 운동 선택 및 설정. exercises는 선택한 운동 환경에서 할 수 있는 운동
// 아무것도 고르지 않았으면 AI에게 추천받고, 고른 운동이 있으면 세부 설정으로 넘어간다
@Composable
fun ManualExerciseSelectionScreen(
    exercises: List<SearchableExercise> = SampleSearchableExercises,
    initialSelected: List<String> = emptyList(),
    onBack: () -> Unit = {},
    onAiRecommend: () -> Unit = {},
    onNext: (List<String>) -> Unit = {}
) {
    var selected by rememberSaveable { mutableStateOf(initialSelected) }

    ExerciseSearchLayout(
        exercises = exercises,
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
    FitMateTheme { ManualExerciseSelectionScreen(initialSelected = listOf("벤치 프레스", "숄더 프레스", "체스트 프레스")) }
}
