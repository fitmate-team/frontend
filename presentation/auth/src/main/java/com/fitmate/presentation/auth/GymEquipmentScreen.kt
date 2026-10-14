package com.fitmate.presentation.auth

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.fitmate.presentation.designsystem.theme.FitMateTheme

private val GymEquipmentSections = listOf(
    EquipmentSection("프리 웨이트", listOf("덤벨", "바벨", "EZ바", "케틀벨")),
    EquipmentSection("밴드/저항", listOf("저항 밴드", "미니 밴드")),
    EquipmentSection("벤치/랙", listOf("파워랙/스쿼트랙", "풀업바/철봉")),
    EquipmentSection("보조 기구", listOf("AB 롤러", "스텝박스")),
    EquipmentSection(
        "머신/케이블",
        listOf(
            "케이블", "체스트 프레스", "펙덱", "랫풀다운",
            "숄더 프레스", "어시스트 풀업", "백 익스텐션",
            "레그 프레스", "레그 익스텐션", "레그 컬",
            "힙 어덕션", "힙 어브덕션", "암 컬"
        )
    ),
    EquipmentSection("유산소", listOf("줄넘기", "러닝머신", "스테어마스터", "로잉 머신", "실내 자전거", "엘립티컬"))
)

// 1.9 회원가입 (운동기구 - 헬스장)
@Composable
fun GymEquipmentScreen(
    onBack: () -> Unit,
    onNext: () -> Unit
) {
    EquipmentSelectionScreen(
        subtitle = "선택한 기구에 맞춰 운동 루틴을 추천해드려요.",
        sections = GymEquipmentSections,
        onBack = onBack,
        onNext = onNext
    )
}

@Preview(widthDp = 394, heightDp = 996)
@Composable
private fun GymEquipmentScreenPreview() {
    FitMateTheme {
        GymEquipmentScreen(onBack = {}, onNext = {})
    }
}
