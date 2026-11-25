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


// ==================== 월별 운동 기록 ====================

// 월별 운동 기록 및 집계 정보
data class MonthlyWorkoutRecord(
    val year: Int,
    val month: Int,
    val workoutCount: Int,
    val totalExerciseSeconds: Long,
    val workoutDates: List<java.time.LocalDate>,
    val records: List<MonthlyWorkoutRecordItem>
)

// 개별 운동 기록 요약
data class MonthlyWorkoutRecordItem(
    val workoutRecordId: Long,
    val date: java.time.LocalDate,
    val mainTarget: String,
    val actualExerciseSeconds: Long,
    val completionRate: Double,
    val maxHeartRate: Int?,
    val exerciseNames: List<String>
)

// ==================== 성장 분석 ====================

// 성장 분석 초기 데이터
data class GrowthAnalysis(
    val bodyComposition: BodyCompositionGrowth,
    val workoutGrowth: WorkoutGrowth,
    val attendance: AttendanceGrowth
)

// ==================== 신체 변화 ====================

// 최근 신체 측정값 및 월별 기록
data class BodyCompositionGrowth(
    val latest: LatestBodyComposition?,
    val monthlyRecords: List<MonthlyBodyComposition>
)

// 신체 지표별 최근 측정값
data class LatestBodyComposition(
    val bodyWeight: BodyCompositionMeasurement?,
    val skeletalMuscleMass: BodyCompositionMeasurement?,
    val bodyFatPercentage: BodyCompositionMeasurement?,
    val bodyFatMass: BodyCompositionMeasurement?
)

// 개별 신체 지표 측정값
data class BodyCompositionMeasurement(
    val value: Double?,
    val measuredDate: String?
)

// 월별 신체 기록
data class MonthlyBodyComposition(
    val year: Int,
    val month: Int,
    val bodyWeight: BodyCompositionRange?,
    val skeletalMuscleMass: BodyCompositionRange?,
    val bodyFatPercentage: BodyCompositionRange?,
    val bodyFatMass: BodyCompositionRange?
)

// 월별 신체 지표 최대·최소
data class BodyCompositionRange(
    val max: Double?,
    val min: Double?
)

// ==================== 운동 성장 ====================

// 수행한 중량 운동 목록 및 선택 운동
data class WorkoutGrowth(
    val exercises: List<GrowthExercise>,
    val selectedExercise: ExerciseGrowth?
)

// 성장 분석에서 선택 가능한 운동
data class GrowthExercise(
    val exerciseId: Long,
    val exerciseName: String
)

// 선택 운동의 월별 중량 변화
data class ExerciseGrowth(
    val exerciseId: Long,
    val exerciseName: String,
    val monthlyRecords: List<MonthlyExerciseWeight>
)

// 월별 최대·최소 중량
data class MonthlyExerciseWeight(
    val year: Int,
    val month: Int,
    val maxWeight: Double?,
    val minWeight: Double?
)

// ==================== 출석률 ====================

// 월별 출석률 목록
data class AttendanceGrowth(
    val monthlyRecords: List<MonthlyAttendance>
)

// 개별 월의 출석률
data class MonthlyAttendance(
    val year: Int,
    val month: Int,
    val attendanceRate: Double?
)
