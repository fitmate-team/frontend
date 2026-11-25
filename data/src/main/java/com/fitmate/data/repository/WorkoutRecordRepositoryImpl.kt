
package com.fitmate.data.repository

import com.fitmate.data.remote.api.WorkoutRecordApi
import com.fitmate.data.remote.apiCall
import com.fitmate.data.remote.dto.*
import com.fitmate.domain.model.*
import com.fitmate.domain.repository.WorkoutRecordRepository
import java.time.LocalDate
import javax.inject.Inject

class WorkoutRecordRepositoryImpl @Inject constructor(
    private val workoutRecordApi: WorkoutRecordApi
) : WorkoutRecordRepository {

    // 운동한 날짜 조회
    override suspend fun getWorkoutDates(
        year: Int,
        month: Int
    ): Result<List<LocalDate>> = apiCall {
        workoutRecordApi
            .getMonthlyRecords(year, month)
            .workoutDates
            .map(LocalDate::parse)
    }

    // 월별 운동 기록 및 집계 정보 조회
    override suspend fun getMonthlyWorkoutRecords(
        year: Int,
        month: Int
    ): Result<MonthlyWorkoutRecord> = apiCall {
        workoutRecordApi
            .getMonthlyRecords(year, month)
            .toDomain()
    }

    // 운동 기록 상세 및 분석 결과 조회
    override suspend fun getWorkoutRecordDetail(
        workoutRecordId: Long
    ): Result<WorkoutRecordDetail> = apiCall {
        workoutRecordApi
            .getWorkoutRecordDetail(workoutRecordId)
            .toDomain()
    }

    // 성장 분석 초기 데이터 조회
    override suspend fun getGrowthAnalysis():
            Result<GrowthAnalysis> = apiCall {
        workoutRecordApi
            .getGrowthAnalysis()
            .toDomain()
    }

    // 선택한 운동의 월별 중량 변화 조회
    override suspend fun getExerciseGrowth(
        exerciseId: Long
    ): Result<ExerciseGrowth> = apiCall {
        workoutRecordApi
            .getExerciseGrowth(exerciseId)
            .toDomain()
    }
}

// ==================== 월별 운동 기록 ====================

// 월별 운동 기록 DTO → Domain
private fun MonthlyWorkoutRecordResponseDto.toDomain():
        MonthlyWorkoutRecord {

    return MonthlyWorkoutRecord(
        year = year,
        month = month,
        workoutCount = workoutCount,
        totalExerciseSeconds = totalExerciseSeconds,
        workoutDates = workoutDates.map(LocalDate::parse),
        records = records.map { it.toDomain() }
    )
}

// 개별 운동 기록 DTO → Domain
private fun MonthlyWorkoutRecordItemDto.toDomain():
        MonthlyWorkoutRecordItem {

    return MonthlyWorkoutRecordItem(
        workoutRecordId = workoutRecordId,
        date = LocalDate.parse(date),
        mainTarget = mainTarget,
        actualExerciseSeconds = actualExerciseSeconds,
        completionRate = completionRate,
        maxHeartRate = maxHeartRate,
        exerciseNames = exerciseNames
    )
}

// ==================== 운동 기록 상세 ====================

// 운동 기록 상세 DTO → Domain
private fun WorkoutRecordDetailResponseDto.toDomain():
        WorkoutRecordDetail {

    return WorkoutRecordDetail(
        workoutRecordId = workoutRecordId,
        dailyRoutineId = dailyRoutineId,
        startedAt = startedAt,
        endedAt = endedAt,
        completionRate = completionRate,
        actualExerciseSeconds = actualExerciseSeconds,
        actualRestSeconds = actualRestSeconds,
        minHeartRate = minHeartRate,
        maxHeartRate = maxHeartRate,
        perceivedDifficulty = perceivedDifficulty,
        hasNextRoutine = hasNextRoutine,
        exercises = exercises
            .sortedBy { it.exerciseOrder }
            .map { it.toDomain() }
    )
}

// 개별 운동 분석 DTO → Domain
private fun WorkoutRecordExerciseDto.toDomain():
        WorkoutRecordExercise {

    return WorkoutRecordExercise(
        routineExerciseId = routineExerciseId,
        exerciseId = exerciseId,
        exerciseOrder = exerciseOrder,
        exerciseName = exerciseName,
        baselineRecordType = baselineRecordType,
        weightKg = weight,
        reps = reps,
        setCount = setCount,
        durationSeconds = durationSeconds,
        completedSetCount = completedSetCount,
        completedDurationSeconds = completedDurationSeconds,
        calculatedDifficulty = calculatedDifficulty
    )
}

// ==================== 성장 분석 ====================

// 성장 분석 초기 데이터 DTO → Domain
private fun GrowthAnalysisResponseDto.toDomain():
        GrowthAnalysis {

    return GrowthAnalysis(
        bodyComposition = bodyComposition.toDomain(),
        workoutGrowth = workoutGrowth.toDomain(),
        attendance = attendance.toDomain()
    )
}

// ==================== 신체 변화 ====================

private fun BodyCompositionGrowthDto.toDomain():
        BodyCompositionGrowth {

    return BodyCompositionGrowth(
        latest = latest?.toDomain(),
        monthlyRecords = monthlyRecords
            .sortedWith(
                compareBy<MonthlyBodyCompositionDto> { it.year }
                    .thenBy { it.month }
            )
            .map { it.toDomain() }
    )
}

private fun LatestBodyCompositionDto.toDomain():
        LatestBodyComposition {

    return LatestBodyComposition(
        bodyWeight = bodyWeight?.toDomain(),
        skeletalMuscleMass = skeletalMuscleMass?.toDomain(),
        bodyFatPercentage = bodyFatPercentage?.toDomain(),
        bodyFatMass = bodyFatMass?.toDomain()
    )
}

private fun BodyCompositionMeasurementDto.toDomain():
        BodyCompositionMeasurement {

    return BodyCompositionMeasurement(
        value = value,
        measuredDate = measuredDate
    )
}

private fun MonthlyBodyCompositionDto.toDomain():
        MonthlyBodyComposition {

    return MonthlyBodyComposition(
        year = year,
        month = month,
        bodyWeight = bodyWeight?.toDomain(),
        skeletalMuscleMass = skeletalMuscleMass?.toDomain(),
        bodyFatPercentage = bodyFatPercentage?.toDomain(),
        bodyFatMass = bodyFatMass?.toDomain()
    )
}

private fun BodyCompositionRangeDto.toDomain():
        BodyCompositionRange {

    return BodyCompositionRange(
        max = max,
        min = min
    )
}

// ==================== 운동 성장 ====================

private fun WorkoutGrowthDto.toDomain(): WorkoutGrowth {

    return WorkoutGrowth(
        exercises = exercises.map { it.toDomain() },
        selectedExercise = selectedExercise?.toDomain()
    )
}

private fun GrowthExerciseDto.toDomain(): GrowthExercise {

    return GrowthExercise(
        exerciseId = exerciseId,
        exerciseName = exerciseName
    )
}

private fun ExerciseGrowthResponseDto.toDomain():
        ExerciseGrowth {

    return ExerciseGrowth(
        exerciseId = exerciseId,
        exerciseName = exerciseName,
        monthlyRecords = monthlyRecords
            .sortedWith(
                compareBy<MonthlyExerciseWeightDto> { it.year }
                    .thenBy { it.month }
            )
            .map { it.toDomain() }
    )
}

private fun MonthlyExerciseWeightDto.toDomain():
        MonthlyExerciseWeight {

    return MonthlyExerciseWeight(
        year = year,
        month = month,
        maxWeight = maxWeight,
        minWeight = minWeight
    )
}

// ==================== 출석률 ====================

private fun AttendanceGrowthDto.toDomain(): AttendanceGrowth {

    return AttendanceGrowth(
        monthlyRecords = monthlyRecords
            .sortedWith(
                compareBy<MonthlyAttendanceDto> { it.year }
                    .thenBy { it.month }
            )
            .map { it.toDomain() }
    )
}

private fun MonthlyAttendanceDto.toDomain(): MonthlyAttendance {

    return MonthlyAttendance(
        year = year,
        month = month,
        attendanceRate = attendanceRate
    )
}
