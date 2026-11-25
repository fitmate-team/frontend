package com.fitmate.domain.repository

import com.fitmate.domain.model.WorkoutRecordDetail
import java.time.LocalDate

interface WorkoutRecordRepository {

    // 그 달에 운동한 날짜 (중도 종료 포함, 오름차순)
    suspend fun getWorkoutDates(
        year: Int,
        month: Int
    ): Result<List<LocalDate>>

    // 운동 기록 상세 및 분석 결과 조회
    suspend fun getWorkoutRecordDetail(
        workoutRecordId: Long
    ): Result<WorkoutRecordDetail>
}