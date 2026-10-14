package com.fitmate.presentation.auth

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.fitmate.presentation.designsystem.component.ExerciseChip
import com.fitmate.presentation.designsystem.component.ExerciseRecordCard
import com.fitmate.presentation.designsystem.component.FitMateOutlinedButton
import com.fitmate.presentation.designsystem.component.FitMatePrimaryButton
import com.fitmate.presentation.designsystem.component.RecordValueField
import com.fitmate.presentation.designsystem.theme.FitMateTheme
import com.fitmate.presentation.designsystem.theme.Spacing3
import com.fitmate.presentation.designsystem.theme.Spacing4
import com.fitmate.presentation.designsystem.theme.Spacing6

// 1.11 회원가입 (최근 운동 기록). recordExercises는 운동 검색 화면에서 고른 운동
@Composable
fun ExerciseAbilityScreen(
    recordExercises: List<String>,
    onBack: () -> Unit,
    onAddRecord: () -> Unit,
    onComplete: () -> Unit,
    onSkip: () -> Unit
) {
    SignupStepScaffold(
        step = 10,
        onBack = onBack,
        title = "최근 운동 기록을\n알고 있나요?",
        subtitle = "AI가 운동을 추천하는 데 참고할 수 있어요.",
        // 기록 카드가 있으면 설명과 카드 사이가 16dp, 없으면 24dp
        contentTopSpacing = if (recordExercises.isEmpty()) Spacing6 else Spacing4,
        bottomBar = {
            FitMatePrimaryButton(text = "완료", onClick = onComplete)
            FitMateOutlinedButton(text = "건너뛰기", onClick = onSkip)
        }
    ) {
        recordExercises.forEach { name ->
            key(name) {
                ExerciseRecordEntry(
                    name = name,
                    recordType = findSignupExercise(name)?.recordType ?: RecordType.Weight
                )
            }
            Spacer(modifier = Modifier.height(Spacing3))
        }
        ExerciseChip(text = "+ 운동 기록 추가", selected = false, onClick = onAddRecord)
    }
}

@Composable
private fun ExerciseRecordEntry(name: String, recordType: RecordType) {
    var weight by rememberSaveable { mutableStateOf("") }
    var reps by rememberSaveable { mutableStateOf("") }
    var sets by rememberSaveable { mutableStateOf("") }
    var minutes by rememberSaveable { mutableStateOf("") }

    val weightField: @Composable () -> Unit = { RecordValueField(value = weight, onValueChange = { weight = it }, unit = "kg") }
    val repsField: @Composable () -> Unit = { RecordValueField(value = reps, onValueChange = { reps = it }, unit = "회") }
    val setsField: @Composable () -> Unit = { RecordValueField(value = sets, onValueChange = { sets = it }, unit = "세트") }

    when (recordType) {
        RecordType.Weight -> ExerciseRecordCard(title = name, fields = listOf(weightField, repsField, setsField))
        RecordType.Bodyweight -> ExerciseRecordCard(title = name, fields = listOf(repsField, setsField))
        RecordType.Cardio -> ExerciseRecordCard(
            title = name,
            trailing = { RecordValueField(value = minutes, onValueChange = { minutes = it }, unit = "분") }
        )
    }
}

@Preview(widthDp = 394, heightDp = 892)
@Composable
private fun ExerciseAbilityScreenPreview() {
    FitMateTheme {
        ExerciseAbilityScreen(
            recordExercises = listOf("벤치 프레스", "숄더 프레스", "줄넘기", "스쿼트"),
            onBack = {},
            onAddRecord = {},
            onComplete = {},
            onSkip = {}
        )
    }
}
