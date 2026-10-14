package com.fitmate.presentation.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.fitmate.presentation.designsystem.component.ExerciseSearchLayout
import com.fitmate.presentation.designsystem.component.FitMatePrimaryButton
import com.fitmate.presentation.designsystem.theme.FitMateTheme

// 운동 검색 (고려해야할 부분 / 최근 운동 기록에서 운동을 고를 때 공통으로 사용)
// initialSelected로 이미 고른 운동을 체크한 상태로 열고, 다음을 누르면 고른 순서대로 돌려준다
@Composable
fun ExerciseSearchScreen(
    initialSelected: List<String>,
    onBack: () -> Unit,
    onConfirm: (List<String>) -> Unit
) {
    var selected by rememberSaveable { mutableStateOf(initialSelected) }

    ExerciseSearchLayout(
        exercises = SignupSearchableExercises,
        selected = selected,
        onToggle = { name -> selected = if (name in selected) selected - name else selected + name },
        onBack = onBack
    ) {
        FitMatePrimaryButton(text = "다음", onClick = { onConfirm(selected) })
    }
}

@Preview(widthDp = 394, heightDp = 892)
@Composable
private fun ExerciseSearchScreenPreview() {
    FitMateTheme {
        ExerciseSearchScreen(initialSelected = listOf("랫풀다운", "줄넘기"), onBack = {}, onConfirm = {})
    }
}
