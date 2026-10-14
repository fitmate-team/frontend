package com.fitmate.presentation.auth

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.fitmate.presentation.designsystem.component.ChipFlowLayout
import com.fitmate.presentation.designsystem.component.ExerciseChip
import com.fitmate.presentation.designsystem.component.FitMatePrimaryButton
import com.fitmate.presentation.designsystem.theme.Spacing3
import com.fitmate.presentation.designsystem.theme.Spacing6

internal data class EquipmentSection(val title: String, val items: List<String>)

// 1.9 회원가입 (운동기구) 공통 화면. 장소별로 섹션 구성만 다르다
@Composable
internal fun EquipmentSelectionScreen(
    subtitle: String,
    sections: List<EquipmentSection>,
    onBack: () -> Unit,
    onNext: () -> Unit
) {
    var selected by rememberSaveableSelection()

    SignupStepScaffold(
        step = 8,
        onBack = onBack,
        title = "사용할 수 있는\n운동기구를 알려주세요",
        subtitle = subtitle,
        bottomBar = { FitMatePrimaryButton(text = "다음", onClick = onNext) }
    ) {
        sections.forEachIndexed { index, section ->
            if (index > 0) Spacer(modifier = Modifier.height(Spacing6))
            SignupFieldLabel(text = section.title)
            Spacer(modifier = Modifier.height(Spacing3))
            ChipFlowLayout {
                section.items.forEach { item ->
                    ExerciseChip(
                        text = item,
                        selected = item in selected,
                        onClick = { selected = selected.toggle(item) }
                    )
                }
            }
        }
    }
}
