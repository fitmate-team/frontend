package com.fitmate.data.remote.api

import com.fitmate.data.remote.dto.MonthlyWorkoutRecordResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface WorkoutRecordApi {

    @GET("api/workouts/records/monthly")
    suspend fun getMonthlyRecords(
        @Query("year") year: Int,
        @Query("month") month: Int
    ): MonthlyWorkoutRecordResponseDto
}
