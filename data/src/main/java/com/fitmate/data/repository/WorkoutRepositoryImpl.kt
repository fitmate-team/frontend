package com.fitmate.data.repository

import com.fitmate.data.remote.api.WorkoutApi
import com.fitmate.data.remote.api.CatalogApi
import com.fitmate.data.remote.apiCall
import com.fitmate.data.remote.dto.*
import com.fitmate.domain.model.*
import com.fitmate.domain.repository.WorkoutRepository
import javax.inject.Inject

class WorkoutRepositoryImpl @Inject constructor(
    private val api: WorkoutApi,
    private val catalogApi: CatalogApi
) : WorkoutRepository {

    override suspend fun getTodayWorkout(): Result<TodayWorkout> =
        apiCall {
            api.getTodayWorkout().toDomain()
        }

    override suspend fun getWorkoutEnvironments(): Result<WorkoutEnvironments> =
        apiCall {
            api.getWorkoutEnvironments().toDomain()
        }

    override suspend fun getExerciseCatalog():
            Result<List<ExerciseCatalogItem>> = apiCall {

        catalogApi.getExercises().map { exercise ->
            ExerciseCatalogItem(
                exerciseCode = exercise.exerciseCode,
                nameKo = exercise.nameKo,
                exerciseType = exercise.exerciseType,
                baselineRecordType = exercise.baselineRecordType,
                primaryMuscle = exercise.primaryMuscle
            )
        }
    }

    override suspend fun validateWorkoutLocation(
        workoutEnvironmentId: Long
    ): Result<WorkoutLocationValidation> = apiCall {
        api.validateWorkoutLocation(
            WorkoutLocationRequestDto(workoutEnvironmentId)
        ).toDomain()
    }

    override suspend fun updateWorkoutLocation(
        workoutEnvironmentId: Long
    ): Result<Unit> = apiCall {
        api.updateWorkoutLocation(
            WorkoutLocationRequestDto(workoutEnvironmentId)
        )
    }

    override suspend fun startWorkout(
        dailyRoutineId: Long
    ): Result<Long> = apiCall {
        api.startWorkout(
            StartWorkoutRequestDto(dailyRoutineId)
        ).workoutRecordId
    }

    // 운동 종료
    override suspend fun finishWorkout(
        request: FinishWorkout
    ): Result<Unit> = apiCall {
        api.finishWorkout(
            FinishWorkoutRequestDto(
                dailyRoutineId = request.dailyRoutineId,
                workoutRecordId = request.workoutRecordId,
                endedAt = request.endedAt,
                actualExerciseSeconds = request.actualExerciseSeconds,
                actualRestSeconds = request.actualRestSeconds,
                minHeartRate = request.minHeartRate,
                maxHeartRate = request.maxHeartRate,
                exercises = request.exercises.map {
                    it.toDto()
                },
                perceivedDifficulty = request.perceivedDifficulty
            )
        )
    }

    override suspend fun recommendAdjustments(
        workoutRecordId: Long
    ): Result<WorkoutAdjustmentRecommendation> = apiCall {
        api.recommendAdjustments(workoutRecordId).toDomain()
    }

    override suspend fun applyAdjustments(
        workoutRecordId: Long,
        request: ApplyWorkoutAdjustments
    ): Result<Unit> = apiCall {
        api.applyAdjustments(
            workoutRecordId = workoutRecordId,
            request = ApplyWorkoutAdjustmentsRequestDto(
                exercises = request.exercises.map {
                    ApplyWorkoutAdjustmentExerciseDto(
                        weightKg = it.weightKg,
                        reps = it.reps,
                        setCount = it.setCount,
                        durationSeconds = it.durationSeconds,
                        exerciseId = it.exerciseId,
                        apply = it.apply
                    )
                }
            )
        )
    }

    override suspend fun getAvailableExercises(
        dailyRoutineId: Long
    ): Result<List<AvailableWorkoutExercise>> = apiCall {
        api.getAvailableExercises(dailyRoutineId)
            .exercises.map { exercise ->
                AvailableWorkoutExercise(
                    exerciseId = exercise.exerciseId,
                    exerciseCode = exercise.exerciseCode,
                    nameKo = exercise.nameKo,
                    primaryMuscle = exercise.primaryMuscle,
                    equipmentCategories = exercise.equipmentCategories,
                    includedInDailyRoutine = exercise.includedInDailyRoutine
                )
            }
    }

    override suspend fun getExerciseGuides(
        exerciseIds: List<Long>
    ): Result<List<ExerciseGuide>> = apiCall {
        if (exerciseIds.isEmpty()) {
            emptyList()
        } else {
            api.getExerciseGuides(
                exerciseIds = exerciseIds.joinToString(",")
            ).map { guide ->
                ExerciseGuide(
                    exerciseId = guide.exerciseId,
                    exerciseName = guide.exerciseName,
                    videoUrl = guide.videoUrl.orEmpty(),
                    guide = guide.guide.orEmpty(),
                    primaryMuscle = guide.primaryMuscle
                )
            }
        }
    }

    override suspend fun updateDailyRoutineExercises(
        dailyRoutineId: Long,
        exercises: List<WorkoutExercise>
    ): Result<Unit> = apiCall {
        api.updateDailyRoutineExercises(
            dailyRoutineId = dailyRoutineId,
            request = UpdateDailyRoutineExercisesRequestDto(
                exercises = exercises.map { exercise ->
                    UpdateDailyRoutineExerciseDto(
                        exerciseCode = exercise.exerciseCode,
                        weightKg = exercise.weightKg,
                        reps = exercise.reps,
                        sets = exercise.sets,
                        durationSeconds = exercise.durationSeconds,
                        restSeconds = exercise.restSeconds
                    )
                }
            )
        )
    }

    override suspend fun getExercisesAvailableByEnvironment(
        environmentId: Long
    ): Result<List<ExerciseCatalogItem>> = apiCall {
        api.getExercisesAvailableByEnvironment(environmentId)
            .map { item ->
                ExerciseCatalogItem(
                    exerciseCode = item.exerciseCode,
                    nameKo = item.nameKo,
                    exerciseType = item.exerciseType,
                    baselineRecordType = item.baselineRecordType,
                    primaryMuscle = item.primaryMuscle
                )
            }
    }
}

// DTO → Domain

private fun TodayWorkoutResponseDto.toDomain() = TodayWorkout(
    dailyRoutineId = dailyRoutineId,
    workoutDate = workoutDate,
    workoutEnvironmentId = workoutEnvironmentId,
    locationType = locationType,
    locationName = locationName,
    title = title,
    estimatedDurationMinutes = estimatedDurationMinutes,
    exerciseCount = exerciseCount,
    averageRestSeconds = averageRestSeconds,
    hasWorkedOut = hasWorkedOut,
    exercises = exercises.map { it.toDomain() }
)

// 오늘의 운동 상세 정보
private fun TodayWorkoutExerciseDto.toDomain() = WorkoutExercise(
    exerciseCode = exerciseCode,
    exerciseName = exerciseName,
    weightKg = weightKg,
    reps = reps,
    sets = sets,
    durationSeconds = durationSeconds,
    restSeconds = restSeconds,
    appliedAdjustmentId = null,
    weightRequired = weightRequired,
    exerciseId = exerciseId,
    routineExerciseId = routineExerciseId,
    baselineRecordType = baselineRecordType
)

private fun WorkoutExerciseDto.toDomain() = WorkoutExercise(
    exerciseCode = exerciseCode,
    weightKg = weightKg,
    reps = reps,
    sets = sets,
    durationSeconds = durationSeconds,
    restSeconds = restSeconds,
    appliedAdjustmentId = appliedAdjustmentId
)

// Domain → DTO

private fun WorkoutExercise.toDto() = WorkoutExerciseDto(
    exerciseCode = exerciseCode,
    weightKg = weightKg,
    reps = reps,
    sets = sets,
    durationSeconds = durationSeconds,
    restSeconds = restSeconds,
    appliedAdjustmentId = appliedAdjustmentId
)

// 운동 종료 전용 수행 기록 변환
private fun FinishWorkoutExercise.toDto() =
    FinishWorkoutExerciseDto(
        routineExerciseId = routineExerciseId,
        completedSetCount = completedSetCount,
        completedDurationSeconds = completedDurationSeconds,
        minHeartRate = minHeartRate,
        maxHeartRate = maxHeartRate
    )

// 운동 환경 목록
private fun WorkoutEnvironmentsResponseDto.toDomain() =
    WorkoutEnvironments(
        scheduledEnvironmentId = scheduledEnvironmentId,
        environments = environments.map {
            WorkoutEnvironment(
                id = it.id,
                gymName = it.gymName,
                locationType = it.locationType
            )
        }
    )

// 운동 환경 변경 가능 여부
private fun WorkoutLocationValidationResponseDto.toDomain() =
    WorkoutLocationValidation(
        workoutEnvironmentId = workoutEnvironmentId,
        currentRoutineLocationName = currentRoutineLocationName,
        requiresRerecommendation = requiresRerecommendation
    )

// AI 운동 강도 조정 추천
private fun WorkoutAdjustmentResponseDto.toDomain() =
    WorkoutAdjustmentRecommendation(
        workoutRecordId = workoutRecordId,
        exercises = exercises.map { exercise ->
            WorkoutAdjustmentExercise(
                exerciseId = exercise.exerciseId,
                exerciseName = exercise.exerciseName,
                baselineRecordType = exercise.baselineRecordType,
                current = exercise.current.toDomain(),
                recommended = exercise.recommended.toDomain(),
                changes = exercise.changes.map {
                    WorkoutAdjustmentChange(
                        field = it.field,
                        direction = it.direction
                    )
                },
                reason = exercise.reason
            )
        }
    )

private fun WorkoutAdjustmentValuesDto.toDomain() =
    WorkoutAdjustmentValues(
        weightKg = weightKg,
        reps = reps,
        setCount = setCount,
        durationSeconds = durationSeconds
    )