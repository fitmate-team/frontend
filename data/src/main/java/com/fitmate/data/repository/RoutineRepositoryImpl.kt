package com.fitmate.data.repository

import com.fitmate.data.remote.api.RoutineApi
import com.fitmate.data.remote.apiCall
import com.fitmate.data.remote.dto.CustomAiExerciseRequestDto
import com.fitmate.data.remote.dto.CustomAiPrescriptionRequestDto
import com.fitmate.data.remote.dto.CustomAiSummaryRequestDto
import com.fitmate.data.remote.dto.CustomRoutineSaveRequestDto
import com.fitmate.data.remote.dto.DailyRoutineRequestDto
import com.fitmate.data.remote.dto.ExercisePrescriptionDto
import com.fitmate.data.remote.dto.PrescriptionRecommendationDto
import com.fitmate.data.remote.dto.RecommendationReasonDto
import com.fitmate.data.remote.dto.RoutineExerciseResponseDto
import com.fitmate.data.remote.dto.SavedDailyRoutineDto
import com.fitmate.data.remote.dto.TodayWorkoutResponseDto
import com.fitmate.data.remote.dto.WeeklyAiRoutineRequestDto
import com.fitmate.data.remote.dto.WeeklyRoutineDayOutputDto
import com.fitmate.data.remote.dto.WeeklyRoutineResponseDto
import com.fitmate.data.remote.dto.WeeklyRoutineSaveRequestDto
import com.fitmate.data.remote.nullIfErrorCode
import com.fitmate.data.remote.toEnumOrNull
import com.fitmate.domain.model.DailyRoutine
import com.fitmate.domain.model.DailyRoutineSummary
import com.fitmate.domain.model.ExerciseLocation
import com.fitmate.domain.model.ExercisePrescription
import com.fitmate.domain.model.PrescriptionRecommendation
import com.fitmate.domain.model.RecommendationReason
import com.fitmate.domain.model.RoutineTarget
import com.fitmate.domain.model.TodayRoutine
import com.fitmate.domain.model.WeeklyRoutine
import com.fitmate.domain.model.WeeklyRoutineRecommendation
import com.fitmate.domain.model.WorkoutDay
import com.fitmate.domain.repository.RoutineRepository
import java.time.LocalDate
import javax.inject.Inject

private const val WEEKLY_ROUTINE_NOT_FOUND = "WEEKLY_ROUTINE_NOT_FOUND"
private const val TODAY_ROUTINE_NOT_FOUND = "TODAY_ROUTINE_NOT_FOUND"

class RoutineRepositoryImpl @Inject constructor(
    private val routineApi: RoutineApi
) : RoutineRepository {

    override suspend fun getWeeklyRoutine(): Result<WeeklyRoutine?> =
        apiCall { routineApi.getWeeklyRoutine().toDomain() }
            .nullIfErrorCode(WEEKLY_ROUTINE_NOT_FOUND)

    // 루틴이 없을 때도 오늘 루틴이 없다는 같은 코드가 온다
    override suspend fun getTodayRoutine(): Result<TodayRoutine?> =
        apiCall { routineApi.getTodayWorkout().toDomain() }
            .nullIfErrorCode(TODAY_ROUTINE_NOT_FOUND, WEEKLY_ROUTINE_NOT_FOUND)

    override suspend fun recommendWeeklyRoutine(
        days: Set<WorkoutDay>,
        sessionMinutes: Int,
        environmentId: Long
    ): Result<WeeklyRoutineRecommendation> = apiCall {
        val response = routineApi.recommendWeeklyRoutine(
            WeeklyAiRoutineRequestDto(
                selectedDays = days.sorted().map { it.name },
                sessionMinutes = sessionMinutes,
                environmentId = environmentId
            )
        )
        val reasons = response.recommendationReason
        WeeklyRoutineRecommendation(
            reasons = listOfNotNull(reasons?.primaryGoalReason, reasons?.secondaryGoalReason, reasons?.scheduleReason)
                .map { it.toDomain() },
            days = response.weeklyPlan.mapNotNull { it.toDomain() }.sortedBy { it.day }
        )
    }

    override suspend fun saveWeeklyRoutine(
        environmentId: Long,
        sessionMinutes: Int,
        days: List<DailyRoutine>
    ): Result<Unit> = apiCall {
        routineApi.saveWeeklyRoutine(
            WeeklyRoutineSaveRequestDto(
                environmentId = environmentId,
                sessionMinutes = sessionMinutes,
                weeklyPlan = days.map { it.toRequestDto(environmentId = null) }
            )
        )
    }

    override suspend fun recommendExercises(environmentId: Long): Result<List<String>> = apiCall {
        routineApi.recommendExercises(CustomAiExerciseRequestDto(environmentId)).exerciseCodes
    }

    override suspend fun recommendPrescriptions(
        exerciseCodes: List<String>
    ): Result<Map<String, PrescriptionRecommendation>> = apiCall {
        routineApi.recommendPrescriptions(CustomAiPrescriptionRequestDto(exerciseCodes))
            .exercises
            .mapNotNull { item -> item.recommendation?.let { item.exerciseCode to it.toDomain() } }
            .toMap()
    }

    override suspend fun summarizeDailyRoutine(exercises: List<ExercisePrescription>): Result<DailyRoutineSummary> = apiCall {
        val response = routineApi.summarizeDailyRoutine(CustomAiSummaryRequestDto(exercises.map { it.toRequestDto() }))
        DailyRoutineSummary(
            mainTarget = response.mainTarget.toEnumOrNull<RoutineTarget>() ?: RoutineTarget.FULL_BODY,
            estimatedMinutes = response.estimatedMinutes
        )
    }

    override suspend fun saveCustomRoutine(
        firstSelectedDay: WorkoutDay,
        environmentId: Long,
        days: List<DailyRoutine>
    ): Result<Unit> = apiCall {
        routineApi.saveCustomRoutine(
            CustomRoutineSaveRequestDto(
                firstSelectedDay = firstSelectedDay.name,
                weeklyPlan = days.map { it.toRequestDto(environmentId) }
            )
        )
    }
}

private fun RoutineExerciseResponseDto.toDomain() = ExercisePrescription(
    exerciseCode = exerciseCode,
    name = nameKo,
    sets = sets,
    reps = reps,
    weightKg = weightKg,
    durationSeconds = durationSeconds,
    restSeconds = restSeconds
)

private fun ExercisePrescription.toRequestDto() = ExercisePrescriptionDto(
    exerciseCode = exerciseCode,
    weightKg = weightKg,
    reps = reps,
    sets = sets,
    durationSeconds = durationSeconds,
    restSeconds = restSeconds
)

private fun DailyRoutine.toRequestDto(environmentId: Long?) = DailyRoutineRequestDto(
    environmentId = environmentId,
    day = day.name,
    mainTarget = mainTarget.name,
    estimatedMinutes = estimatedMinutes,
    exercises = exercises.map { it.toRequestDto() }
)

private fun RecommendationReasonDto.toDomain() = RecommendationReason(title = title, reason = reason)

private fun PrescriptionRecommendationDto.toDomain() = PrescriptionRecommendation(
    sets = sets,
    reps = reps,
    weightKg = weightKg,
    durationSeconds = durationSeconds,
    restSeconds = restSeconds
)

// 앱이 모르는 요일이 오면 그 날만 건너뛰고, 모르는 부위는 전신으로 본다
private fun WeeklyRoutineDayOutputDto.toDomain(): DailyRoutine? {
    return DailyRoutine(
        day = day.toEnumOrNull<WorkoutDay>() ?: return null,
        mainTarget = mainTarget.toEnumOrNull<RoutineTarget>() ?: RoutineTarget.FULL_BODY,
        estimatedMinutes = estimatedMinutes,
        exercises = exercises.map { it.toDomain() },
        focus = focus,
        reason = dailyReason
    )
}

private fun SavedDailyRoutineDto.toDomain(): DailyRoutine? {
    return DailyRoutine(
        day = day.toEnumOrNull<WorkoutDay>() ?: return null,
        mainTarget = mainTarget.toEnumOrNull<RoutineTarget>() ?: RoutineTarget.FULL_BODY,
        estimatedMinutes = estimatedMinutes,
        exercises = exercises.map { it.toDomain() },
        date = routineDate?.let(LocalDate::parse)
    )
}

private fun WeeklyRoutineResponseDto.toDomain() = WeeklyRoutine(
    weekStartDate = LocalDate.parse(weekStartDate),
    sessionMinutes = sessionMinutes,
    environmentId = workoutEnvironmentId,
    days = weeklyPlan.mapNotNull { it.toDomain() }.sortedBy { it.day }
)

private fun TodayWorkoutResponseDto.toDomain() = TodayRoutine(
    dailyRoutineId = dailyRoutineId,
    date = LocalDate.parse(workoutDate),
    environmentId = workoutEnvironmentId,
    location = locationType.toEnumOrNull<ExerciseLocation>() ?: ExerciseLocation.GYM,
    locationName = locationName.orEmpty(),
    title = title,
    estimatedMinutes = estimatedDurationMinutes,
    averageRestSeconds = averageRestSeconds,
    hasWorkedOut = hasWorkedOut,
    exerciseNames = exercises.map { it.exerciseName }
)
