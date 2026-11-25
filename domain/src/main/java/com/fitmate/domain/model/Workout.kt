package com.fitmate.domain.model

// 오늘의 운동
data class TodayWorkout(
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
    val exercises: List<WorkoutExercise>
)

// 운동 루틴 정보
data class WorkoutExercise(
    val exerciseCode: String,
    val exerciseId: Long? = null,
    val weightKg: Int?,
    val reps: Int?,
    val sets: Int?,
    val durationSeconds: Int?,
    val restSeconds: Int?,
    val appliedAdjustmentId: Long?,
    val exerciseName: String? = null,
    val weightRequired: Boolean = false,
    val routineExerciseId: Long? = null,
    val baselineRecordType: String? = null
)

// 운동 카탈로그
data class ExerciseCatalogItem(
    val exerciseCode: String,
    val nameKo: String,
    val exerciseType: String,
    val baselineRecordType: String,
    val primaryMuscle: String
)

// 운동 환경 목록
data class WorkoutEnvironments(
    val scheduledEnvironmentId: Long,
    val environments: List<WorkoutEnvironment>
)

data class WorkoutEnvironment(
    val id: Long,
    val gymName: String?,
    val locationType: String
)

// 운동 환경 변경 가능 여부
data class WorkoutLocationValidation(
    val workoutEnvironmentId: Long,
    val currentRoutineLocationName: String,
    val requiresRerecommendation: Boolean
)

// 운동 종료 요청
data class FinishWorkout(
    val dailyRoutineId: Long,
    val workoutRecordId: Long,
    val endedAt: String,
    val actualExerciseSeconds: Long,
    val actualRestSeconds: Long,
    val minHeartRate: Int?,
    val maxHeartRate: Int?,
    val exercises: List<FinishWorkoutExercise>,
    val perceivedDifficulty: String
)

// 운동 종료 시 운동별 실제 수행 기록
data class FinishWorkoutExercise(
    val routineExerciseId: Long,
    val completedSetCount: Int? = null,
    val completedDurationSeconds: Int? = null,
    val minHeartRate: Int? = null,
    val maxHeartRate: Int? = null
)

// AI 운동 강도 조정 추천
data class WorkoutAdjustmentRecommendation(
    val workoutRecordId: Long,
    val exercises: List<WorkoutAdjustmentExercise>
)

data class WorkoutAdjustmentExercise(
    val exerciseId: Long,
    val exerciseName: String,
    val baselineRecordType: String,
    val current: WorkoutAdjustmentValues,
    val recommended: WorkoutAdjustmentValues,
    val changes: List<WorkoutAdjustmentChange>,
    val reason: String
)

data class WorkoutAdjustmentValues(
    val weightKg: Int?,
    val reps: Int?,
    val setCount: Int?,
    val durationSeconds: Int?
)

data class WorkoutAdjustmentChange(
    val field: String,
    val direction: String
)

// AI 운동 강도 조정 적용
data class ApplyWorkoutAdjustments(
    val exercises: List<ApplyWorkoutAdjustmentExercise>
)

data class ApplyWorkoutAdjustmentExercise(
    val weightKg: Int?,
    val reps: Int?,
    val setCount: Int?,
    val durationSeconds: Int?,
    val exerciseId: Long,
    val apply: Boolean
)

// 운동 가이드
data class ExerciseGuide(
    val exerciseId: Long,
    val exerciseName: String,
    val videoUrl: String,
    val guide: String,
    val primaryMuscle: String
)

// 선택 가능한 운동 목록
data class AvailableWorkoutExercise(
    val exerciseId: Long,
    val exerciseCode: String,
    val nameKo: String,
    val primaryMuscle: String,
    val equipmentCategories: List<String>,
    val includedInDailyRoutine: Boolean
)

// 운동 기록 상세 및 분석 결과
data class WorkoutRecordDetail(
    val workoutRecordId: Long,
    val dailyRoutineId: Long,
    val startedAt: String,
    val endedAt: String,
    val completionRate: Double,
    val actualExerciseSeconds: Long,
    val actualRestSeconds: Long,
    val minHeartRate: Int?,
    val maxHeartRate: Int?,
    val perceivedDifficulty: String?,
    val hasNextRoutine: Boolean,
    val exercises: List<WorkoutRecordExercise>
)

// 개별 운동 수행 및 분석 결과
data class WorkoutRecordExercise(
    val routineExerciseId: Long,
    val exerciseId: Long,
    val exerciseOrder: Int,
    val exerciseName: String,
    val baselineRecordType: String,
    val weightKg: Double?,
    val reps: Int?,
    val setCount: Int?,
    val durationSeconds: Int?,
    val completedSetCount: Int?,
    val completedDurationSeconds: Int?,
    val calculatedDifficulty: String?
)