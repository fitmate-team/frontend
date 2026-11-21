package com.fitmate.data.remote.api

import com.fitmate.data.remote.dto.MemberResponseDto
import com.fitmate.data.remote.dto.WorkoutEnvironmentListResponseDto
import retrofit2.http.GET

interface MemberApi {

    @GET("api/members/me")
    suspend fun getMe(): MemberResponseDto

    @GET("api/members/workout-environments")
    suspend fun getWorkoutEnvironments(): WorkoutEnvironmentListResponseDto
}
