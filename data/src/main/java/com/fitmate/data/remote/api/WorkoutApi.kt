package com.fitmate.data.remote.api

import com.fitmate.data.remote.dto.TodayWorkoutResponseDto
import com.fitmate.data.remote.dto.WorkoutEnvironmentsResponseDto
import com.fitmate.data.remote.dto.WorkoutLocationRequestDto
import com.fitmate.data.remote.dto.WorkoutLocationValidationResponseDto
import com.fitmate.data.remote.dto.StartWorkoutRequestDto
import com.fitmate.data.remote.dto.StartWorkoutResponseDto
import com.fitmate.data.remote.dto.FinishWorkoutRequestDto
import com.fitmate.data.remote.dto.WorkoutAdjustmentResponseDto
import com.fitmate.data.remote.dto.ApplyWorkoutAdjustmentsRequestDto
import com.fitmate.data.remote.dto.AvailableExercisesResponseDto
import com.fitmate.data.remote.dto.ExerciseGuideResponseDto
import com.fitmate.data.remote.dto.UpdateDailyRoutineExercisesRequestDto
import com.fitmate.data.remote.dto.EnvironmentAvailableExerciseDto
import retrofit2.http.Query
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface WorkoutApi {

    @GET("api/workouts/today")
    suspend fun getTodayWorkout(): TodayWorkoutResponseDto

    @GET("api/workouts/today/environments")
    suspend fun getWorkoutEnvironments(): WorkoutEnvironmentsResponseDto

    @POST("api/workouts/today/location/validate")
    suspend fun validateWorkoutLocation(
        @Body request: WorkoutLocationRequestDto
    ): WorkoutLocationValidationResponseDto

    @PATCH("api/workouts/today/location")
    suspend fun updateWorkoutLocation(
        @Body request: WorkoutLocationRequestDto
    ): Unit

    @POST("api/workouts/today/start")
    suspend fun startWorkout(
        @Body request: StartWorkoutRequestDto
    ): StartWorkoutResponseDto

    @POST("api/workouts/today/finish")
    suspend fun finishWorkout(
        @Body request: FinishWorkoutRequestDto
    ): Unit

    @POST("api/routine/adjustments/recommendations/{workoutRecordId}")
    suspend fun recommendAdjustments(
        @Path("workoutRecordId") workoutRecordId: Long
    ): WorkoutAdjustmentResponseDto

    @PUT("api/routine/adjustments/{workoutRecordId}/exercises")
    suspend fun applyAdjustments(
        @Path("workoutRecordId") workoutRecordId: Long,
        @Body request: ApplyWorkoutAdjustmentsRequestDto
    ): Unit


    @GET("api/daily-routines/{dailyRoutineId}/available-exercises")
    suspend fun getAvailableExercises(
        @Path("dailyRoutineId") dailyRoutineId: Long
    ): AvailableExercisesResponseDto

    @GET("api/exercises/guides")
    suspend fun getExerciseGuides(
        @Query("exerciseIds") exerciseIds: String
    ): List<ExerciseGuideResponseDto>

    // 오늘의 루틴 운동 목록 전체 수정
    @PUT("api/daily-routines/{dailyRoutineId}/exercises")
    suspend fun updateDailyRoutineExercises(
        @Path("dailyRoutineId") dailyRoutineId: Long,
        @Body request: UpdateDailyRoutineExercisesRequestDto
    ): Unit

    // 선택한 운동 환경에서 수행 가능한 운동 목록
    @GET("api/exercises/available")
    suspend fun getExercisesAvailableByEnvironment(
        @Query("environmentId") environmentId: Long
    ): List<EnvironmentAvailableExerciseDto>


}
