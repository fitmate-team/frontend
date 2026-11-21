package com.fitmate.data.repository

import com.fitmate.data.remote.api.WorkoutRecordApi
import com.fitmate.data.remote.apiCall
import com.fitmate.domain.repository.WorkoutRecordRepository
import java.time.LocalDate
import javax.inject.Inject

class WorkoutRecordRepositoryImpl @Inject constructor(
    private val workoutRecordApi: WorkoutRecordApi
) : WorkoutRecordRepository {

    override suspend fun getWorkoutDates(year: Int, month: Int): Result<List<LocalDate>> = apiCall {
        workoutRecordApi.getMonthlyRecords(year, month).workoutDates.map(LocalDate::parse)
    }
}
