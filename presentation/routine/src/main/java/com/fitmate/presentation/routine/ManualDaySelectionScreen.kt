package com.fitmate.presentation.routine

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fitmate.presentation.designsystem.component.DaySelectorRow
import com.fitmate.presentation.designsystem.component.FitMateFormScaffold
import com.fitmate.presentation.designsystem.component.FitMatePrimaryButton
import com.fitmate.presentation.designsystem.component.FitMateTitleTopBar
import com.fitmate.presentation.designsystem.component.FormLabeledField
import com.fitmate.presentation.designsystem.component.WeeklyPlanDayCard
import com.fitmate.presentation.designsystem.component.WeeklyPlanFooter
import com.fitmate.presentation.designsystem.theme.FitMateTheme
import com.fitmate.presentation.designsystem.theme.Spacing2
import com.fitmate.presentation.designsystem.theme.Spacing8

// 3.4 루틴 만들기 - 요일 선택. 요일을 누르면 그 요일의 운동을 고르러 가고,
// 운동 설정을 마친 요일은 선택 상태가 되어 아래 "등록된 루틴"에 카드로 보인다
@Composable
fun ManualDaySelectionScreen(
    draft: ManualRoutineDraft = ManualRoutineDraft(),
    onBack: () -> Unit = {},
    onDayClick: (String) -> Unit = {},
    onOpenDay: (String) -> Unit = {},
    onSave: () -> Unit = {}
) {
    FitMateFormScaffold(
        topBar = { FitMateTitleTopBar(title = "루틴 만들기", onBack = onBack) },
        title = "운동 루틴을 만들어볼까요?",
        subtitle = "운동할 수 있는 요일을 입력하고 운동을 선택해보세요.",
        contentTopSpacing = 28.dp,
        bottomBar = { FitMatePrimaryButton(text = "저장하기", onClick = onSave) }
    ) {
        FormLabeledField(label = "운동 가능한 요일") {
            DaySelectorRow(
                selectedDays = draft.days.keys,
                onToggle = onDayClick,
                boxHeight = 44.dp
            )
        }
        if (draft.days.isNotEmpty()) {
            Spacer(modifier = Modifier.height(Spacing8))
            FormLabeledField(label = "등록된 루틴") {
                Column(verticalArrangement = Arrangement.spacedBy(Spacing2)) {
                    draft.orderedDays.forEach { day ->
                        val settings = draft.days.getValue(day)
                        val names = settings.map { it.name }
                        val (icon, iconSize) = when (routineTitleOf(names)) {
                            "하체" -> PlanIcon.Lower
                            "유산소" -> PlanIcon.Cardio
                            else -> PlanIcon.Upper
                        }.toIcon()
                        WeeklyPlanDayCard(
                            dayLabel = day,
                            icon = icon,
                            iconSize = iconSize,
                            title = routineTitleOf(names),
                            exercises = names.take(3).joinToString(" · ") { shortNameOf(it) },
                            footer = WeeklyPlanFooter.Rest("휴식 ${settings.firstNotNullOfOrNull { it.rest } ?: "90초"}"),
                            onClick = { onOpenDay(day) }
                        )
                    }
                }
            }
        }
    }
}

@Preview(name = "요일 선택", widthDp = 394, heightDp = 892)
@Composable
private fun ManualDaySelectionScreenPreview() {
    FitMateTheme { ManualDaySelectionScreen() }
}

@Preview(name = "데이터 입력된 상태", widthDp = 394, heightDp = 892)
@Composable
private fun ManualDaySelectionScreenFilledPreview() {
    val upper = listOf(
        ExerciseSetting("벤치 프레스", rest = "90초"),
        ExerciseSetting("숄더 프레스"),
        ExerciseSetting("체스트 프레스")
    )
    FitMateTheme {
        ManualDaySelectionScreen(
            draft = ManualRoutineDraft(days = mapOf("월" to upper, "목" to upper, "금" to upper, "일" to upper))
        )
    }
}
