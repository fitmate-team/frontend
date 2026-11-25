package com.fitmate.data.repository

import com.fitmate.data.remote.api.WorkoutRecordApi
import com.fitmate.data.remote.apiCall
import com.fitmate.data.remote.dto.WorkoutRecordDetailResponseDto
import com.fitmate.data.remote.dto.WorkoutRecordExerciseDto
import com.fitmate.domain.model.WorkoutRecordDetail
import com.fitmate.domain.model.WorkoutRecordExercise
import com.fitmate.domain.repository.WorkoutRecordRepository
import java.time.LocalDate
import javax.inject.Inject

class WorkoutRecordRepositoryImpl @Inject constructor(
    private val workoutRecordApi: WorkoutRecordApi
) : WorkoutRecordRepository {

    // 월별 운동 기록 조회
    override suspend fun getWorkoutDates(
        year: Int,
        month: Int
    ): Result<List<LocalDate>> = apiCall {
        workoutRecordApi
            .getMonthlyRecords(year, month)
            .workoutDates
            .map(LocalDate::parse)
    }

    // 운동 기록 상세 및 분석 결과 조회
    override suspend fun getWorkoutRecordDetail(
        workoutRecordId: Long
    ): Result<WorkoutRecordDetail> = apiCall {
        workoutRecordApi
            .getWorkoutRecordDetail(workoutRecordId)
            .toDomain()
    }
}

// 운동 기록 상세 DTO → Domain
private fun WorkoutRecordDetailResponseDto.toDomain(): WorkoutRecordDetail {
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
private fun WorkoutRecordExerciseDto.toDomain(): WorkoutRecordExercise {
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