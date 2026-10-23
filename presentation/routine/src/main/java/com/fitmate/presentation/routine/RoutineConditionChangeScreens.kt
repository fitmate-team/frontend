package com.fitmate.presentation.routine

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fitmate.presentation.designsystem.component.DaySelectorRow
import com.fitmate.presentation.designsystem.component.FitMateDropdownField
import com.fitmate.presentation.designsystem.component.FitMateFormScaffold
import com.fitmate.presentation.designsystem.component.FitMatePrimaryButton
import com.fitmate.presentation.designsystem.component.FitMateTitleTopBar
import com.fitmate.presentation.designsystem.component.FormLabeledField
import com.fitmate.presentation.designsystem.component.WorkoutDurationSelector
import com.fitmate.presentation.designsystem.component.WorkoutPlaceSelector
import com.fitmate.presentation.designsystem.theme.FitMateTheme
import com.fitmate.presentation.designsystem.theme.Spacing8

// 3.4 루틴 조건 수정 - 운동 장소 변경. 헬스장은 저장된 헬스장 중에서 고른다
@Composable
fun RoutinePlaceChangeScreen(
    condition: RoutineCondition = SampleRoutineCondition,
    savedGyms: List<String> = SampleSavedGyms,
    onBack: () -> Unit = {},
    onDone: (RoutineCondition) -> Unit = {}
) {
    var place by remember { mutableStateOf(condition.place) }
    var gymName by remember { mutableStateOf(condition.gymName) }
    var dropdownExpanded by remember { mutableStateOf(false) }

    FitMateFormScaffold(
        topBar = { FitMateTitleTopBar(title = "운동 장소 변경", onBack = onBack) },
        title = "이번주 어디에서 운동하나요?",
        subtitle = "운동 장소에 맞는 운동을 추천할게요.",
        bottomBar = {
            FitMatePrimaryButton(text = "완료", onClick = { onDone(condition.copy(place = place, gymName = gymName)) })
        }
    ) {
        WorkoutPlaceSelector(
            selected = place,
            onSelect = { place = it },
            gymContentTopPadding = 2.8.dp
        ) {
            FitMateDropdownField(
                selected = gymName,
                options = savedGyms,
                expanded = dropdownExpanded,
                onExpandedChange = { dropdownExpanded = it },
                onSelect = { gymName = it }
            )
        }
    }
}

// 3.4 루틴 조건 수정 - 운동 시간 변경 (요일 + 한 번에 운동할 수 있는 시간)
@Composable
fun RoutineScheduleChangeScreen(
    condition: RoutineCondition = SampleRoutineCondition,
    onBack: () -> Unit = {},
    onDone: (RoutineCondition) -> Unit = {}
) {
    var days by remember { mutableStateOf(condition.days) }
    var duration by remember { mutableStateOf<String?>(condition.duration) }

    FitMateFormScaffold(
        topBar = { FitMateTitleTopBar(title = "운동 시간 변경", onBack = onBack) },
        title = "언제, 얼마나 운동할 수 있나요?",
        subtitle = "운동할 수 있는 요일과 한 번에 가능한 시간을 알려주세요.",
        contentTopSpacing = 28.dp,
        bottomBar = {
            FitMatePrimaryButton(
                text = "완료",
                onClick = { onDone(condition.copy(days = days, duration = duration ?: condition.duration)) }
            )
        }
    ) {
        FormLabeledField(label = "이번주 운동 가능한 요일") {
            DaySelectorRow(
                selectedDays = days,
                onToggle = { day -> days = if (day in days) days - day else days + day },
                boxHeight = 44.dp
            )
        }
        Spacer(modifier = Modifier.height(Spacing8))
        FormLabeledField(label = "한 번에 운동할 수 있는 시간") {
            WorkoutDurationSelector(selected = duration, onSelect = { duration = it })
        }
    }
}

@Preview(name = "운동 장소 변경", widthDp = 394, heightDp = 892)
@Composable
private fun RoutinePlaceChangeScreenPreview() {
    FitMateTheme { RoutinePlaceChangeScreen() }
}

@Preview(name = "운동 시간 변경", widthDp = 394, heightDp = 892)
@Composable
private fun RoutineScheduleChangeScreenPreview() {
    FitMateTheme { RoutineScheduleChangeScreen(condition = SampleRoutineCondition.copy(duration = "20분")) }
}
