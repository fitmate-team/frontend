package com.fitmate.data.remote.dto

import kotlinx.serialization.Serializable

// 월별 운동 기록 중 홈에서 쓰는 운동 날짜만 받는다
@Serializable
data class MonthlyWorkoutRecordResponseDto(
    val workoutDates: List<String> = emptyList()
)
