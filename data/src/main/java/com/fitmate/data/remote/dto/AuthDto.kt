package com.fitmate.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequestDto(
    val loginId: String,
    val password: String
)

@Serializable
data class LoginResponseDto(
    val id: Long,
    val loginId: String,
    val accessToken: String,
    val refreshToken: String
)

@Serializable
data class TokenReissueRequestDto(
    val refreshToken: String
)

@Serializable
data class TokenReissueResponseDto(
    val accessToken: String
)

@Serializable
data class LoginIdCheckResponseDto(
    val available: Boolean
)

// enum 값은 서버 enum 이름 문자열로 보낸다
@Serializable
data class SignUpRequestDto(
    val loginId: String,
    val password: String,
    val gender: String,
    val age: Int,
    val height: Double,
    val weight: Double,
    val skeletalMuscleMass: Double? = null,
    val bodyFatPercentage: Double? = null,
    val bodyFatMass: Double? = null,
    val exerciseLevel: String,
    val currentExerciseStatus: String,
    val primaryGoal: String,
    val goalStrategy: String,
    val sessionMinutes: Int,
    val availableDays: List<String>,
    val exerciseLocation: String,
    val gymName: String? = null,
    val equipmentCodes: List<String>,
    val avoidBodyAreas: List<String>,
    val excludedExerciseCodes: List<String>,
    val recentExerciseRecords: List<RecentExerciseRecordRequestDto>
)

@Serializable
data class RecentExerciseRecordRequestDto(
    val exerciseCode: String,
    val weight: Int? = null,
    val reps: Int? = null,
    val sets: Int? = null,
    val durationSeconds: Int? = null
)

@Serializable
data class SignUpResponseDto(
    val id: Long,
    val loginId: String
)

// 공통 에러 응답
@Serializable
data class ErrorResponseDto(
    val code: String? = null,
    val message: String? = null
)
