package com.fitmate.data.remote.dto

import kotlinx.serialization.Serializable

// 루틴 저장 / AI 요약 요청에 들어가는 운동 하나의 처방값
@Serializable
data class ExercisePrescriptionDto(
    val exerciseCode: String,
    val weightKg: Int? = null,
    val reps: Int? = null,
    val sets: Int,
    val durationSeconds: Int? = null,
    val restSeconds: Int
)

// 직접 만들기 저장은 요일마다 environmentId가 필요하다 (Swagger 명세에는 빠져 있음)
@Serializable
data class DailyRoutineRequestDto(
    val environmentId: Long? = null,
    val day: String,
    val mainTarget: String,
    val estimatedMinutes: Int,
    val exercises: List<ExercisePrescriptionDto>
)

@Serializable
data class WeeklyRoutineSaveRequestDto(
    val environmentId: Long,
    val sessionMinutes: Int,
    val weeklyPlan: List<DailyRoutineRequestDto>
)

@Serializable
data class CustomRoutineSaveRequestDto(
    val firstSelectedDay: String,
    val weeklyPlan: List<DailyRoutineRequestDto>
)

// AI 추천 결과와 저장된 루틴이 같이 쓰는 운동 응답
@Serializable
data class RoutineExerciseResponseDto(
    val exerciseCode: String,
    val nameKo: String = "",
    val sets: Int = 1,
    val reps: Int? = null,
    val weightKg: Int? = null,
    val durationSeconds: Int? = null,
    val restSeconds: Int = 0
)

@Serializable
data class WeeklyAiRoutineRequestDto(
    val selectedDays: List<String>,
    val sessionMinutes: Int,
    val environmentId: Long
)

@Serializable
data class RecommendationReasonDto(
    val title: String = "",
    val reason: String = ""
)

@Serializable
data class RecommendationReasonsDto(
    val primaryGoalReason: RecommendationReasonDto? = null,
    val secondaryGoalReason: RecommendationReasonDto? = null,
    val scheduleReason: RecommendationReasonDto? = null
)

@Serializable
data class WeeklyRoutineDayOutputDto(
    val day: String,
    val mainTarget: String,
    val focus: String? = null,
    val estimatedMinutes: Int = 0,
    val dailyReason: String? = null,
    val exercises: List<RoutineExerciseResponseDto> = emptyList()
)

@Serializable
data class WeeklyRoutineOutputDto(
    val recommendationReason: RecommendationReasonsDto? = null,
    val weeklyPlan: List<WeeklyRoutineDayOutputDto> = emptyList()
)

@Serializable
data class SavedDailyRoutineDto(
    val routineDate: String? = null,
    val day: String,
    val mainTarget: String,
    val estimatedMinutes: Int = 0,
    val exercises: List<RoutineExerciseResponseDto> = emptyList()
)

@Serializable
data class WeeklyRoutineResponseDto(
    val weekStartDate: String,
    val sessionMinutes: Int,
    val workoutEnvironmentId: Long,
    val weeklyPlan: List<SavedDailyRoutineDto> = emptyList()
)

@Serializable
data class TodayExerciseResponseDto(
    val exerciseName: String
)

@Serializable
data class RoutineTodayWorkoutResponseDto(
    val dailyRoutineId: Long,
    val workoutDate: String,
    val workoutEnvironmentId: Long,
    val locationType: String,
    val locationName: String? = null,
    val title: String = "",
    val estimatedDurationMinutes: Int = 0,
    val averageRestSeconds: Int = 0,
    val hasWorkedOut: Boolean = false,
    val exercises: List<TodayExerciseResponseDto> = emptyList()
)

// GET /api/home. 실제 응답은 Swagger와 다르다 (todayWorkout에 exerciseNames / status가 오고 장소 정보는 없다)
// 오늘 운동이 없으면 todayWorkout의 dailyRoutineId 등이 null로 온다
@Serializable
data class HomeResponseDto(
    val weeklyWorkout: HomeWeeklyWorkoutDto? = null,
    val todayWorkout: HomeTodayWorkoutDto? = null
)

@Serializable
data class HomeWeeklyWorkoutDto(
    val hasWeeklyRoutine: Boolean = false,
    val completedWorkoutCount: Int = 0,
    val targetWorkoutCount: Int = 0,
    val consecutiveWorkoutDays: Int = 0
)

@Serializable
data class HomeTodayWorkoutDto(
    val dailyRoutineId: Long? = null,
    val workoutDate: String? = null,
    val title: String? = null,
    val estimatedDurationMinutes: Int? = null,
    val averageRestSeconds: Int? = null,
    val exerciseCount: Int = 0,
    val exerciseNames: List<String> = emptyList(),
    val status: String? = null,
    val workoutRecordId: Long? = null
)

@Serializable
data class CustomAiExerciseRequestDto(
    val environmentId: Long
)

@Serializable
data class CustomExerciseOutputDto(
    val exerciseCodes: List<String> = emptyList()
)

@Serializable
data class CustomAiPrescriptionRequestDto(
    val exerciseCodes: List<String>
)

@Serializable
data class PrescriptionRecommendationDto(
    val weightKg: Int? = null,
    val reps: Int? = null,
    val sets: Int? = null,
    val durationSeconds: Int? = null,
    val restSeconds: Int? = null
)

@Serializable
data class ExerciseRecommendationDto(
    val exerciseCode: String,
    val recommendation: PrescriptionRecommendationDto? = null
)

@Serializable
data class CustomPrescriptionResponseDto(
    val exercises: List<ExerciseRecommendationDto> = emptyList()
)

@Serializable
data class CustomAiSummaryRequestDto(
    val exercises: List<ExercisePrescriptionDto>
)

@Serializable
data class CustomSummaryOutputDto(
    val mainTarget: String,
    val estimatedMinutes: Int
)


@Serializable
data class DailyAiRoutineRequestDto(
    val environmentId: Long,
    val sessionMinutes: Int
)

@Serializable
data class DailyRoutineOutputDto(
    val mainTarget: String,
    val estimatedMinutes: Int,
    val replacements: List<DailyExerciseReplacementDto> = emptyList()
)

@Serializable
data class DailyExerciseReplacementDto(
    val originalExerciseCode: String,
    val replacementExercise: DailyReplacementExerciseDto
)

@Serializable
data class DailyReplacementExerciseDto(
    val exerciseCode: String,
    val nameKo: String,
    val weightKg: Int? = null,
    val reps: Int? = null,
    val sets: Int? = null,
    val durationSeconds: Int? = null,
    val restSeconds: Int? = null,
    val appliedAdjustmentId: Long? = null
)
@Serializable
data class DailyRoutineSaveRequestDto(
    val environmentId: Long,
    val sessionMinutes: Int,
    val mainTarget: String,
    val estimatedMinutes: Int,
    val replacements: List<DailyExerciseReplacementDto>
)
