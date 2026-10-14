package com.fitmate.presentation.auth

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.fitmate.presentation.designsystem.theme.FitMateTheme

private val OutdoorEquipmentSections = listOf(
    EquipmentSection("유산소", listOf("줄넘기", "자전거"))
)

// 1.9 회원가입 (운동기구 - 야외)
@Composable
fun OutdoorEquipmentScreen(
    onBack: () -> Unit,
    onNext: () -> Unit
) {
    EquipmentSelectionScreen(
        subtitle = "없는 기구가 필요한 운동은 추천에서 제외할게요.",
        sections = OutdoorEquipmentSections,
        onBack = onBack,
        onNext = onNext
    )
}

@Preview(widthDp = 394, heightDp = 892)
@Composable
private fun OutdoorEquipmentScreenPreview() {
    FitMateTheme {
        OutdoorEquipmentScreen(onBack = {}, onNext = {})
    }
}
