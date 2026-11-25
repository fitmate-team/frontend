package com.fitmate.domain.model


data class WorkoutGuide(
    val exerciseId: Long,
    val exerciseName: String,
    val videoUrl: String?,
    val guide: String?,
    val primaryMuscle: String?
)
