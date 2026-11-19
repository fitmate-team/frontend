package com.fitmate.domain.model

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
    val weightRequired: Boolean = false
)

data class ExerciseCatalogItem(
    val exerciseCode: String,
    val nameKo: String,
    val exerciseType: String,
    val baselineRecordType: String,
    val primaryMuscle: String
)

data class WorkoutEnvironments(
    val scheduledEnvironmentId: Long,
    val environments: List<WorkoutEnvironment>
)

data class WorkoutEnvironment(
    val id: Long,
    val gymName: String?,
    val locationType: String
)

data class WorkoutLocationValidation(
    val workoutEnvironmentId: Long,
    val currentRoutineLocationName: String,
    val requiresRerecommendation: Boolean
)

data class FinishWorkout(
    val dailyRoutineId: Long,
    val workoutRecordId: Long,
    val endedAt: String,
    val actualExerciseSeconds: Long,
    val actualRestSeconds: Long,
    val minHeartRate: Int?,
    val maxHeartRate: Int?,
    val exercises: List<WorkoutExercise>,
    val perceivedDifficulty: String
)

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


data class ExerciseGuide(
    val exerciseId: Long,
    val exerciseName: String,
    val videoUrl: String,
    val guide: String,
    val primaryMuscle: String
)

data class AvailableWorkoutExercise(
    val exerciseId: Long,
    val exerciseCode: String,
    val nameKo: String,
    val primaryMuscle: String,
    val equipmentCategories: List<String>,
    val includedInDailyRoutine: Boolean
)
