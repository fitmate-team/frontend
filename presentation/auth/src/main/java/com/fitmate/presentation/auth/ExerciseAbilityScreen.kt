package com.fitmate.presentation.auth

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
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

// 기록을 입력할 운동 한 개. recordType에 따라 입력칸이 달라진다
data class SignupRecordItem(
    val name: String,
    val recordType: RecordType,
    val input: RecordInput
)

// 1.11 회원가입 (최근 운동 기록). records는 운동 검색 화면에서 고른 운동
@Composable
fun ExerciseAbilityScreen(
    records: List<SignupRecordItem>,
    isSubmitting: Boolean,
    onRecordChange: (name: String, input: RecordInput) -> Unit,
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
        contentTopSpacing = if (records.isEmpty()) Spacing6 else Spacing4,
        bottomBar = {
            FitMatePrimaryButton(text = if (isSubmitting) "가입 중..." else "완료", onClick = onComplete)
            FitMateOutlinedButton(text = "건너뛰기", onClick = onSkip)
        }
    ) {
        records.forEach { record ->
            key(record.name) {
                ExerciseRecordEntry(
                    record = record,
                    onChange = { onRecordChange(record.name, it) }
                )
            }
            Spacer(modifier = Modifier.height(Spacing3))
        }
        ExerciseChip(text = "+ 운동 기록 추가", selected = false, onClick = onAddRecord)
    }
}

@Composable
private fun ExerciseRecordEntry(record: SignupRecordItem, onChange: (RecordInput) -> Unit) {
    val input = record.input
    val weightField: @Composable () -> Unit = {
        RecordValueField(value = input.weight, onValueChange = { onChange(input.copy(weight = it)) }, unit = "kg")
    }
    val repsField: @Composable () -> Unit = {
        RecordValueField(value = input.reps, onValueChange = { onChange(input.copy(reps = it)) }, unit = "회")
    }
    val setsField: @Composable () -> Unit = {
        RecordValueField(value = input.sets, onValueChange = { onChange(input.copy(sets = it)) }, unit = "세트")
    }

    when (record.recordType) {
        RecordType.Weight -> ExerciseRecordCard(title = record.name, fields = listOf(weightField, repsField, setsField))
        RecordType.Bodyweight -> ExerciseRecordCard(title = record.name, fields = listOf(repsField, setsField))
        RecordType.Cardio -> ExerciseRecordCard(
            title = record.name,
            trailing = {
                RecordValueField(value = input.minutes, onValueChange = { onChange(input.copy(minutes = it)) }, unit = "분")
            }
        )
    }
}

@Preview(widthDp = 394, heightDp = 892)
@Composable
private fun ExerciseAbilityScreenPreview() {
    FitMateTheme {
        ExerciseAbilityScreen(
            records = listOf(
                SignupRecordItem("벤치 프레스", RecordType.Weight, RecordInput(weight = "60", reps = "8", sets = "4")),
                SignupRecordItem("줄넘기", RecordType.Cardio, RecordInput()),
                SignupRecordItem("스쿼트", RecordType.Bodyweight, RecordInput())
            ),
            isSubmitting = false,
            onRecordChange = { _, _ -> },
            onBack = {},
            onAddRecord = {},
            onComplete = {},
            onSkip = {}
        )
    }
}
