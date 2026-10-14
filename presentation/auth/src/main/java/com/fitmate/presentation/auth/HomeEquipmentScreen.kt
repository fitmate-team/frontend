package com.fitmate.presentation.auth

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.fitmate.presentation.designsystem.theme.FitMateTheme

private val HomeEquipmentSections = listOf(
    EquipmentSection("프리 웨이트", listOf("덤벨", "바벨", "EZ바", "케틀벨")),
    EquipmentSection("밴드/저항", listOf("저항 밴드", "미니 밴드")),
    EquipmentSection("벤치/랙", listOf("벤치", "파워랙/스쿼트랙", "풀업바/철봉")),
    EquipmentSection("보조 기구", listOf("AB 롤러", "스텝박스")),
    EquipmentSection("유산소", listOf("줄넘기", "러닝머신", "로잉 머신", "실내 자전거", "엘립티컬"))
)

// 1.9 회원가입 (운동기구 - 집)
@Composable
fun HomeEquipmentScreen(
    onBack: () -> Unit,
    onNext: () -> Unit
) {
    EquipmentSelectionScreen(
        subtitle = "없는 기구가 필요한 운동은 추천에서 제외할게요.",
        sections = HomeEquipmentSections,
        onBack = onBack,
        onNext = onNext
    )
}

@Preview(widthDp = 394, heightDp = 892)
@Composable
private fun HomeEquipmentScreenPreview() {
    FitMateTheme {
        HomeEquipmentScreen(onBack = {}, onNext = {})
    }
}
