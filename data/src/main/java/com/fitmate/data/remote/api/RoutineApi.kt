package com.fitmate.data.remote.api

import com.fitmate.data.remote.dto.CustomAiExerciseRequestDto
import com.fitmate.data.remote.dto.CustomAiPrescriptionRequestDto
import com.fitmate.data.remote.dto.CustomAiSummaryRequestDto
import com.fitmate.data.remote.dto.CustomExerciseOutputDto
import com.fitmate.data.remote.dto.CustomPrescriptionResponseDto
import com.fitmate.data.remote.dto.CustomRoutineSaveRequestDto
import com.fitmate.data.remote.dto.CustomSummaryOutputDto
import com.fitmate.data.remote.dto.TodayWorkoutResponseDto
import com.fitmate.data.remote.dto.WeeklyAiRoutineRequestDto
import com.fitmate.data.remote.dto.WeeklyRoutineOutputDto
import com.fitmate.data.remote.dto.WeeklyRoutineResponseDto
import com.fitmate.data.remote.dto.WeeklyRoutineSaveRequestDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface RoutineApi {

    @GET("api/routine/weekly")
    suspend fun getWeeklyRoutine(): WeeklyRoutineResponseDto

    @POST("api/routine/weekly")
    suspend fun saveWeeklyRoutine(@Body request: WeeklyRoutineSaveRequestDto)

    @POST("api/routine/custom")
    suspend fun saveCustomRoutine(@Body request: CustomRoutineSaveRequestDto)

    @GET("api/workouts/today")
    suspend fun getTodayWorkout(): TodayWorkoutResponseDto

    @POST("api/ai/weekly-routines")
    suspend fun recommendWeeklyRoutine(@Body request: WeeklyAiRoutineRequestDto): WeeklyRoutineOutputDto

    @POST("api/ai/custom-routines/exercises")
    suspend fun recommendExercises(@Body request: CustomAiExerciseRequestDto): CustomExerciseOutputDto

    @POST("api/ai/custom-routines/prescription")
    suspend fun recommendPrescriptions(@Body request: CustomAiPrescriptionRequestDto): CustomPrescriptionResponseDto

    @POST("api/ai/custom-routines/summary")
    suspend fun summarizeDailyRoutine(@Body request: CustomAiSummaryRequestDto): CustomSummaryOutputDto
}
