package com.fitmate.presentation.auth

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.fitmate.domain.model.Equipment
import com.fitmate.presentation.designsystem.theme.FitMateTheme

// 1.9 회원가입 (운동기구 - 헬스장). 기구 목록은 서버에서 장소별로 받아온다
@Composable
fun GymEquipmentScreen(
    equipments: List<Equipment>,
    selectedCodes: Set<String>,
    isLoading: Boolean,
    onToggle: (String) -> Unit,
    onRetry: () -> Unit,
    onBack: () -> Unit,
    onNext: () -> Unit
) {
    EquipmentSelectionScreen(
        subtitle = "선택한 기구에 맞춰 운동 루틴을 추천해드려요.",
        equipments = equipments,
        selectedCodes = selectedCodes,
        isLoading = isLoading,
        onToggle = onToggle,
        onRetry = onRetry,
        onBack = onBack,
        onNext = onNext
    )
}

@Preview(widthDp = 394, heightDp = 996)
@Composable
private fun GymEquipmentScreenPreview() {
    FitMateTheme {
        GymEquipmentScreen(
            equipments = PreviewEquipments,
            selectedCodes = setOf("EQ_DUMBBELL"),
            isLoading = false,
            onToggle = {},
            onRetry = {},
            onBack = {},
            onNext = {}
        )
    }
}
