package com.fitmate.domain.repository

import java.time.LocalDate

interface WorkoutRecordRepository {

    // 그 달에 운동한 날짜 (중도 종료 포함, 오름차순)
    suspend fun getWorkoutDates(year: Int, month: Int): Result<List<LocalDate>>
}
