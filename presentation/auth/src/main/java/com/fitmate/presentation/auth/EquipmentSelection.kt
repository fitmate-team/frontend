package com.fitmate.presentation.auth

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.fitmate.domain.model.Equipment
import com.fitmate.domain.model.EquipmentCategory
import com.fitmate.presentation.designsystem.component.ChipFlowLayout
import com.fitmate.presentation.designsystem.component.ExerciseChip
import com.fitmate.presentation.designsystem.component.FitMateOutlinedButton
import com.fitmate.presentation.designsystem.component.FitMatePrimaryButton
import com.fitmate.presentation.designsystem.theme.Spacing3
import com.fitmate.presentation.designsystem.theme.Spacing6

internal data class EquipmentSection(val title: String, val items: List<Equipment>)

// 서버 기구 목록을 카테고리 섹션으로 묶는다. 기구가 없는 섹션은 보여주지 않는다
internal fun List<Equipment>.toSections(): List<EquipmentSection> =
    EquipmentCategorySections.mapNotNull { (category, title) ->
        filter { it.category == category }
            .takeIf { it.isNotEmpty() }
            ?.let { EquipmentSection(title, it) }
    }

// 1.9 회원가입 (운동기구) 공통 화면. 장소별로 설명 문구와 기구 목록만 다르다
// 목록을 불러오지 못했으면 다시 시도 버튼을 보여준다
@Composable
internal fun EquipmentSelectionScreen(
    subtitle: String,
    equipments: List<Equipment>,
    selectedCodes: Set<String>,
    isLoading: Boolean,
    onToggle: (String) -> Unit,
    onRetry: () -> Unit,
    onBack: () -> Unit,
    onNext: () -> Unit
) {
    SignupStepScaffold(
        step = 8,
        onBack = onBack,
        title = "사용할 수 있는\n운동기구를 알려주세요",
        subtitle = subtitle,
        bottomBar = { FitMatePrimaryButton(text = "다음", onClick = onNext) }
    ) {
        if (equipments.isEmpty()) {
            if (isLoading) {
                SignupHelperText(text = "운동기구 목록을 불러오는 중이에요.")
            } else {
                FitMateOutlinedButton(text = "운동기구 목록 다시 불러오기", onClick = onRetry)
            }
            return@SignupStepScaffold
        }
        equipments.toSections().forEachIndexed { index, section ->
            if (index > 0) Spacer(modifier = Modifier.height(Spacing6))
            SignupFieldLabel(text = section.title)
            Spacer(modifier = Modifier.height(Spacing3))
            ChipFlowLayout {
                section.items.forEach { item ->
                    ExerciseChip(
                        text = item.name,
                        selected = item.code in selectedCodes,
                        onClick = { onToggle(item.code) }
                    )
                }
            }
        }
    }
}

// 미리보기용 기구 목록
internal val PreviewEquipments = listOf(
    Equipment("EQ_DUMBBELL", "덤벨", EquipmentCategory.FREE_WEIGHT),
    Equipment("EQ_BARBELL", "바벨", EquipmentCategory.FREE_WEIGHT),
    Equipment("EQ_RESISTANCE_BAND", "저항 밴드", EquipmentCategory.BAND_RESISTANCE),
    Equipment("EQ_BENCH", "벤치", EquipmentCategory.BENCH_RACK),
    Equipment("EQ_CABLE_MACHINE", "케이블 머신", EquipmentCategory.MACHINE_CABLE),
    Equipment("EQ_JUMP_ROPE", "줄넘기", EquipmentCategory.CARDIO),
    Equipment("EQ_TREADMILL", "러닝머신", EquipmentCategory.CARDIO)
)
