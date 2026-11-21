package com.fitmate.data.remote.api

import com.fitmate.data.remote.dto.CustomAiExerciseRequestDto
import com.fitmate.data.remote.dto.CustomAiPrescriptionRequestDto
import com.fitmate.data.remote.dto.CustomAiSummaryRequestDto
import com.fitmate.data.remote.dto.CustomExerciseOutputDto
import com.fitmate.data.remote.dto.CustomPrescriptionResponseDto
import com.fitmate.data.remote.dto.CustomRoutineSaveRequestDto
import com.fitmate.data.remote.dto.CustomSummaryOutputDto
import com.fitmate.data.remote.dto.HomeResponseDto
import com.fitmate.data.remote.dto.TodayWorkoutResponseDto
import com.fitmate.data.remote.dto.WeeklyAiRoutineRequestDto
import com.fitmate.data.remote.dto.WeeklyRoutineOutputDto
import com.fitmate.data.remote.dto.WeeklyRoutineResponseDto
import com.fitmate.data.remote.dto.WeeklyRoutineSaveRequestDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface RoutineApi {

    @GET("api/routine/weekly")
    suspend fun getWeeklyRoutine(): WeeklyRoutineResponseDto

    @POST("api/routine/weekly")
    suspend fun saveWeeklyRoutine(@Body request: WeeklyRoutineSaveRequestDto)

    @POST("api/routine/custom")
    suspend fun saveCustomRoutine(@Body request: CustomRoutineSaveRequestDto)

    // 이번 주 루틴이 이미 있을 때 덮어쓰기 (POST는 409 DUPLICATE_WEEKLY_ROUTINE)
    @PUT("api/routine/weekly/regenerate")
    suspend fun regenerateWeeklyRoutine(@Body request: WeeklyRoutineSaveRequestDto)

    @PUT("api/routine/custom/regenerate")
    suspend fun regenerateCustomRoutine(@Body request: CustomRoutineSaveRequestDto)

    @GET("api/workouts/today")
    suspend fun getTodayWorkout(): TodayWorkoutResponseDto

    @GET("api/home")
    suspend fun getHome(): HomeResponseDto

    @POST("api/ai/weekly-routines")
    suspend fun recommendWeeklyRoutine(@Body request: WeeklyAiRoutineRequestDto): WeeklyRoutineOutputDto

    // 기존 루틴의 남은 요일을 다시 만든다. 저장은 하지 않는다
    @POST("api/ai/weekly-routines/regenerate")
    suspend fun regenerateWeeklyRoutineRecommendation(@Body request: WeeklyAiRoutineRequestDto): WeeklyRoutineOutputDto

    @POST("api/ai/custom-routines/exercises")
    suspend fun recommendExercises(@Body request: CustomAiExerciseRequestDto): CustomExerciseOutputDto

    @POST("api/ai/custom-routines/prescription")
    suspend fun recommendPrescriptions(@Body request: CustomAiPrescriptionRequestDto): CustomPrescriptionResponseDto

    @POST("api/ai/custom-routines/summary")
    suspend fun summarizeDailyRoutine(@Body request: CustomAiSummaryRequestDto): CustomSummaryOutputDto
}
