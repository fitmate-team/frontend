package com.fitmate.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class MemberResponseDto(
    val height: Double,
    val weight: Double? = null,
    val exerciseLevel: String,
    val primaryGoal: String,
    val goalStrategy: String,
    val sessionMinutes: Int,
    val availableDays: List<String> = emptyList(),
    val exerciseLocation: String
)

@Serializable
data class WorkoutEnvironmentResponseDto(
    val id: Long,
    val gymName: String? = null,
    val locationType: String,
    val equipmentCodes: List<String> = emptyList()
)

@Serializable
data class WorkoutEnvironmentListResponseDto(
    val primaryLocation: String,
    val environments: List<WorkoutEnvironmentResponseDto> = emptyList()
)
