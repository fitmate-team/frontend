package com.fitmate.domain.repository

import com.fitmate.domain.model.TodayWorkout
import com.fitmate.domain.model.WorkoutEnvironments
import com.fitmate.domain.model.WorkoutLocationValidation
import com.fitmate.domain.model.FinishWorkout
import com.fitmate.domain.model.WorkoutAdjustmentRecommendation
import com.fitmate.domain.model.ApplyWorkoutAdjustments
import com.fitmate.domain.model.AvailableWorkoutExercise
import com.fitmate.domain.model.ExerciseCatalogItem
import com.fitmate.domain.model.ExerciseGuide
import com.fitmate.domain.model.WorkoutExercise

interface WorkoutRepository {

    suspend fun getTodayWorkout(): Result<TodayWorkout>

    suspend fun getWorkoutEnvironments(): Result<WorkoutEnvironments>

    suspend fun getExerciseCatalog(): Result<List<ExerciseCatalogItem>>

    suspend fun validateWorkoutLocation(
        workoutEnvironmentId: Long
    ): Result<WorkoutLocationValidation>

    suspend fun updateWorkoutLocation(
        workoutEnvironmentId: Long
    ): Result<Unit>

    suspend fun startWorkout(
        dailyRoutineId: Long
    ): Result<Long>

    suspend fun finishWorkout(
        request: FinishWorkout
    ): Result<Unit>

    suspend fun recommendAdjustments(
        workoutRecordId: Long
    ): Result<WorkoutAdjustmentRecommendation>

    suspend fun applyAdjustments(
        workoutRecordId: Long,
        request: ApplyWorkoutAdjustments
    ): Result<Unit>

    suspend fun getAvailableExercises(
        dailyRoutineId: Long
    ): Result<List<AvailableWorkoutExercise>>

    suspend fun getExerciseGuides(
        exerciseIds: List<Long>
    ): Result<List<ExerciseGuide>>

    suspend fun updateDailyRoutineExercises(
        dailyRoutineId: Long,
        exercises: List<WorkoutExercise>
    ): Result<Unit>


}

