package com.fitmate.domain.model

import java.time.LocalDate

// 하루 루틴의 대표 운동 부위
enum class RoutineTarget { CHEST, BACK, SHOULDERS, LEGS_GLUTES, ARMS, UPPER_BODY, FULL_BODY, CORE, CARDIO }

// 루틴에 들어간 운동 하나의 처방값. 기록 방식에 따라 비는 값이 있다 (유산소는 reps / weightKg 없이 sets = 1)
// name은 서버가 내려준 운동 이름. 서버로 보낼 때는 쓰지 않는다
data class ExercisePrescription(
    val exerciseCode: String,
    val name: String,
    val sets: Int,
    val reps: Int?,
    val weightKg: Int?,
    val durationSeconds: Int?,
    val restSeconds: Int
)

// 요일 하나의 루틴. focus / reason은 AI 추천 결과에만, date는 저장된 루틴에만 있다
data class DailyRoutine(
    val day: WorkoutDay,
    val mainTarget: RoutineTarget,
    val estimatedMinutes: Int,
    val exercises: List<ExercisePrescription>,
    val focus: String? = null,
    val reason: String? = null,
    val date: LocalDate? = null
)

data class RecommendationReason(
    val title: String,
    val reason: String
)

// AI가 만든 주간 루틴 (저장 전). reasons는 주 목표 / 세부 목표 / 일정 순
data class WeeklyRoutineRecommendation(
    val reasons: List<RecommendationReason>,
    val days: List<DailyRoutine>
)

// 이번 주에 저장된 루틴
data class WeeklyRoutine(
    val weekStartDate: LocalDate,
    val sessionMinutes: Int,
    val environmentId: Long,
    val days: List<DailyRoutine>
)

// 직접 만들기에서 AI가 추천한 처방값. 운동 기록이 없으면 추천이 없을 수 있다
data class PrescriptionRecommendation(
    val sets: Int?,
    val reps: Int?,
    val weightKg: Int?,
    val durationSeconds: Int?,
    val restSeconds: Int?
)

// 직접 만든 하루 루틴을 AI가 요약한 결과
data class DailyRoutineSummary(
    val mainTarget: RoutineTarget,
    val estimatedMinutes: Int
)

// 오늘의 운동 (홈 카드)
data class TodayRoutine(
    val dailyRoutineId: Long,
    val date: LocalDate,
    val environmentId: Long,
    val location: ExerciseLocation,
    val locationName: String,
    val title: String,
    val estimatedMinutes: Int,
    val averageRestSeconds: Int,
    val hasWorkedOut: Boolean,
    val exerciseNames: List<String>
)
