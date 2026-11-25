package com.fitmate.data.remote.dto

import kotlinx.serialization.Serializable

// 오늘의 운동 조회
@Serializable
data class TodayWorkoutExerciseDto(
    val routineExerciseId: Long,
    val exerciseId: Long,
    val exerciseCode: String = "",
    val exerciseName: String,
    val exerciseOrder: Int,
    val exerciseType: String,
    val baselineRecordType: String,
    val weightKg: Int? = null,
    val reps: Int? = null,
    val sets: Int? = null,
    val durationSeconds: Int? = null,
    val restSeconds: Int? = null,
    val weightRequired: Boolean = false
)

@Serializable
data class TodayWorkoutResponseDto(
    val dailyRoutineId: Long,
    val workoutDate: String,
    val workoutEnvironmentId: Long,
    val locationType: String,
    val locationName: String,
    val title: String,
    val estimatedDurationMinutes: Int,
    val exerciseCount: Int,
    val averageRestSeconds: Int,
    val hasWorkedOut: Boolean,
    val exercises: List<TodayWorkoutExerciseDto>
)

@Serializable
data class WorkoutExerciseDto(
    val exerciseCode: String,
    val weightKg: Int? = null,
    val reps: Int? = null,
    val sets: Int? = null,
    val durationSeconds: Int? = null,
    val restSeconds: Int? = null,
    val appliedAdjustmentId: Long? = null
)

// 운동 환경 목록 조회
@Serializable
data class WorkoutEnvironmentsResponseDto(
    val scheduledEnvironmentId: Long,
    val environments: List<WorkoutEnvironmentDto>
)

@Serializable
data class WorkoutEnvironmentDto(
    val id: Long,
    val gymName: String? = null,
    val locationType: String
)

// 운동 환경 변경 요청
@Serializable
data class WorkoutLocationRequestDto(
    val workoutEnvironmentId: Long
)

// 운동 환경 변경 가능 여부 검사
@Serializable
data class WorkoutLocationValidationResponseDto(
    val workoutEnvironmentId: Long,
    val currentRoutineLocationName: String,
    val requiresRerecommendation: Boolean
)

// 운동 시작
@Serializable
data class StartWorkoutRequestDto(
    val dailyRoutineId: Long
)

@Serializable
data class StartWorkoutResponseDto(
    val workoutRecordId: Long
)

// 운동 종료
@Serializable
data class FinishWorkoutRequestDto(
    val dailyRoutineId: Long,
    val workoutRecordId: Long,
    val endedAt: String,
    val actualExerciseSeconds: Long,
    val actualRestSeconds: Long,
    val minHeartRate: Int? = null,
    val maxHeartRate: Int? = null,
    val exercises: List<FinishWorkoutExerciseDto>,
    val perceivedDifficulty: String
)

// 운동 종료 시 운동별 실제 수행 기록
@Serializable
data class FinishWorkoutExerciseDto(
    val routineExerciseId: Long,
    val completedSetCount: Int? = null,
    val completedDurationSeconds: Int? = null,
    val minHeartRate: Int? = null,
    val maxHeartRate: Int? = null
)

// 운동별 AI 조정 추천
@Serializable
data class WorkoutAdjustmentResponseDto(
    val workoutRecordId: Long,
    val exercises: List<WorkoutAdjustmentExerciseDto>
)

@Serializable
data class WorkoutAdjustmentExerciseDto(
    val exerciseId: Long,
    val exerciseName: String,
    val baselineRecordType: String,
    val current: WorkoutAdjustmentValuesDto,
    val recommended: WorkoutAdjustmentValuesDto,
    val changes: List<WorkoutAdjustmentChangeDto>,
    val reason: String
)

@Serializable
data class WorkoutAdjustmentValuesDto(
    val weightKg: Int? = null,
    val reps: Int? = null,
    val setCount: Int? = null,
    val durationSeconds: Int? = null
)

@Serializable
data class WorkoutAdjustmentChangeDto(
    val field: String,
    val direction: String
)

// AI 조정 결과 적용
@Serializable
data class ApplyWorkoutAdjustmentsRequestDto(
    val exercises: List<ApplyWorkoutAdjustmentExerciseDto>
)

@Serializable
data class ApplyWorkoutAdjustmentExerciseDto(
    val weightKg: Int? = null,
    val reps: Int? = null,
    val setCount: Int? = null,
    val durationSeconds: Int? = null,
    val exerciseId: Long,
    val apply: Boolean
)

// 데일리 루틴에서 선택 가능한 운동 목록
@Serializable
data class AvailableExercisesResponseDto(
    val dailyRoutineId: Long,
    val exercises: List<AvailableExerciseDto>
)

@Serializable
data class AvailableExerciseDto(
    val exerciseId: Long,
    val exerciseCode: String,
    val nameKo: String,
    val primaryMuscle: String,
    val equipmentCategories: List<String> = emptyList(),
    val includedInDailyRoutine: Boolean
)

// 운동 가이드 다건 조회
@Serializable
data class ExerciseGuideResponseDto(
    val exerciseId: Long,
    val exerciseName: String,
    val videoUrl: String? = null,
    val guide: String? = null,
    val primaryMuscle: String
)

@Serializable
data class UpdateDailyRoutineExercisesRequestDto(
    val exercises: List<UpdateDailyRoutineExerciseDto>
)

@Serializable
data class UpdateDailyRoutineExerciseDto(
    val exerciseCode: String,
    val weightKg: Int? = null,
    val reps: Int? = null,
    val sets: Int? = null,
    val durationSeconds: Int? = null,
    val restSeconds: Int? = null
)

@Serializable
data class AvailableWorkoutExerciseDto(
    val exerciseId: Long,
    val exerciseCode: String
)

@Serializable
data class WorkoutGuideDto(
    val exerciseId: Long,
    val exerciseName: String,
    val videoUrl: String? = null,
    val guide: String? = null,
    val primaryMuscle: String? = null
)

// 운동 환경별 수행 가능한 운동 조회
@Serializable
data class EnvironmentAvailableExerciseDto(
    val exerciseCode: String,
    val nameKo: String,
    val exerciseType: String,
    val baselineRecordType: String,
    val primaryMuscle: String,
    val equipmentCategories: List<String> = emptyList()
)