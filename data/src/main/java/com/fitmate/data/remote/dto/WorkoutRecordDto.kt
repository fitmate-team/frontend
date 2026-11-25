package com.fitmate.data.remote.dto

import kotlinx.serialization.Serializable

// ==================== 월별 운동 기록 ====================

// 월별 운동 기록 조회 응답
@Serializable
data class MonthlyWorkoutRecordResponseDto(
    val workoutDates: List<String> = emptyList(),
    val year: Int = 0,
    val month: Int = 0,
    val workoutCount: Int = 0,
    val totalExerciseSeconds: Long = 0L
)

// ==================== 운동 기록 상세 ====================

// 운동 기록 상세 및 분석 결과
@Serializable
data class WorkoutRecordDetailResponseDto(
    val workoutRecordId: Long,
    val dailyRoutineId: Long,
    val startedAt: String,
    val endedAt: String,
    val completionRate: Double,
    val actualExerciseSeconds: Long,
    val actualRestSeconds: Long,
    val minHeartRate: Int? = null,
    val maxHeartRate: Int? = null,
    val perceivedDifficulty: String? = null,
    val hasNextRoutine: Boolean,
    val exercises: List<WorkoutRecordExerciseDto> = emptyList()
)

// 개별 운동 수행 및 분석 결과
@Serializable
data class WorkoutRecordExerciseDto(
    val routineExerciseId: Long,
    val exerciseId: Long,
    val exerciseOrder: Int,
    val exerciseName: String,
    val baselineRecordType: String,
    val weight: Double? = null,
    val reps: Int? = null,
    val setCount: Int? = null,
    val durationSeconds: Int? = null,
    val completedSetCount: Int? = null,
    val completedDurationSeconds: Int? = null,
    val calculatedDifficulty: String? = null
)

// ==================== 성장 분석 ====================

// 성장 분석 초기 데이터 조회 응답
@Serializable
data class GrowthAnalysisResponseDto(
    val bodyComposition: BodyCompositionGrowthDto,
    val workoutGrowth: WorkoutGrowthDto,
    val attendance: AttendanceGrowthDto
)

// ==================== 신체 변화 ====================

// 신체 지표별 최근 측정값 및 월별 기록
@Serializable
data class BodyCompositionGrowthDto(
    val latest: LatestBodyCompositionDto? = null,
    val monthlyRecords: List<MonthlyBodyCompositionDto> = emptyList()
)

// 신체 지표별 최근 측정값
@Serializable
data class LatestBodyCompositionDto(
    val bodyWeight: BodyCompositionMeasurementDto? = null,
    val skeletalMuscleMass: BodyCompositionMeasurementDto? = null,
    val bodyFatPercentage: BodyCompositionMeasurementDto? = null,
    val bodyFatMass: BodyCompositionMeasurementDto? = null
)

// 개별 신체 지표 측정값
@Serializable
data class BodyCompositionMeasurementDto(
    val value: Double? = null,
    val measuredDate: String? = null
)

// 월별 신체 기록
@Serializable
data class MonthlyBodyCompositionDto(
    val year: Int,
    val month: Int,
    val bodyWeight: BodyCompositionRangeDto? = null,
    val skeletalMuscleMass: BodyCompositionRangeDto? = null,
    val bodyFatPercentage: BodyCompositionRangeDto? = null,
    val bodyFatMass: BodyCompositionRangeDto? = null
)

// 월별 신체 지표 최대·최소
@Serializable
data class BodyCompositionRangeDto(
    val max: Double? = null,
    val min: Double? = null
)

// ==================== 운동 성장 ====================

// 수행한 중량 운동 목록 및 기본 선택 운동
@Serializable
data class WorkoutGrowthDto(
    val exercises: List<GrowthExerciseDto> = emptyList(),
    val selectedExercise: ExerciseGrowthResponseDto? = null
)

// 성장 분석에서 선택 가능한 운동
@Serializable
data class GrowthExerciseDto(
    val exerciseId: Long,
    val exerciseName: String
)

// 선택한 운동의 중량 변화 조회 응답
@Serializable
data class ExerciseGrowthResponseDto(
    val exerciseId: Long,
    val exerciseName: String,
    val monthlyRecords: List<MonthlyExerciseWeightDto> = emptyList()
)

// 월별 최대·최소 중량
@Serializable
data class MonthlyExerciseWeightDto(
    val year: Int,
    val month: Int,
    val maxWeight: Double? = null,
    val minWeight: Double? = null
)

// ==================== 출석률 ====================

// 월별 출석률 기록
@Serializable
data class AttendanceGrowthDto(
    val monthlyRecords: List<MonthlyAttendanceDto> = emptyList()
)

// 개별 월의 출석률
@Serializable
data class MonthlyAttendanceDto(
    val year: Int,
    val month: Int,
    val attendanceRate: Double? = null
)