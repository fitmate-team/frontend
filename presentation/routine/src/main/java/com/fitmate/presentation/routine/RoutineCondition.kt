package com.fitmate.presentation.routine

import com.fitmate.presentation.designsystem.component.WORKOUT_PLACE_GYM
import com.fitmate.presentation.designsystem.component.WeekDayLabels

// AI 루틴 생성 조건. 조건 수정 / 장소 변경 / 시간 변경 화면이 같이 쓴다
data class RoutineCondition(
    val goal: String,
    val days: Set<String>,
    val duration: String,
    val place: String,
    val gymName: String
) {
    val daysLabel: String get() = WeekDayLabels.filter { it in days }.joinToString(" · ")

    // 헬스장이면 헬스장 이름, 아니면 장소 이름
    val placeLabel: String get() = if (place == WORKOUT_PLACE_GYM) gymName else place
}

// 서버 연동 전까지 사용하는 예시 데이터
val SampleRoutineCondition = RoutineCondition(
    goal = "체지방 감량 · 균형형",
    days = setOf("월", "목", "금", "일"),
    duration = "60분",
    place = WORKOUT_PLACE_GYM,
    gymName = "Fit Gym"
)

// 사용자가 저장한 헬스장 목록 (예시)
val SampleSavedGyms = listOf("Fit Gym", "별내 피트니스 센터")
