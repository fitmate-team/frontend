package com.fitmate.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class EquipmentResponseDto(
    val equipmentCode: String,
    val nameKo: String,
    val category: String
)

@Serializable
data class ExerciseResponseDto(
    val exerciseCode: String,
    val nameKo: String,
    val exerciseType: String,
    val baselineRecordType: String,
    val primaryMuscle: String,
    val equipmentCategories: List<String> = emptyList()
)
