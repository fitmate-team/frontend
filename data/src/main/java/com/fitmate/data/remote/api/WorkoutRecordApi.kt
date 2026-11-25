package com.fitmate.data.remote.api

import com.fitmate.data.remote.dto.MonthlyWorkoutRecordResponseDto
import com.fitmate.data.remote.dto.WorkoutRecordDetailResponseDto
import com.fitmate.data.remote.dto.GrowthAnalysisResponseDto
import com.fitmate.data.remote.dto.ExerciseGrowthResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WorkoutRecordApi {

    // 월별 운동 기록 조회
    @GET("api/workouts/records/monthly")
    suspend fun getMonthlyRecords(
        @Query("year") year: Int,
        @Query("month") month: Int
    ): MonthlyWorkoutRecordResponseDto

    // 운동 기록 상세 및 분석 결과 조회
    @GET("api/workouts/records/{workoutRecordId}")
    suspend fun getWorkoutRecordDetail(
        @Path("workoutRecordId") workoutRecordId: Long
    ): WorkoutRecordDetailResponseDto

    // 성장 분석 초기 데이터 조회
    @GET("api/workouts/growth")
    suspend fun getGrowthAnalysis(): GrowthAnalysisResponseDto

    // 선택한 운동의 월별 중량 변화 조회
    @GET("api/workouts/growth/exercises/{exerciseId}")
    suspend fun getExerciseGrowth(
        @Path("exerciseId") exerciseId: Long
    ): ExerciseGrowthResponseDto
}