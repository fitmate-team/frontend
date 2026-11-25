package com.fitmate.domain.repository

import com.fitmate.domain.model.DailyRoutine
import com.fitmate.domain.model.DailyRoutineSummary
import com.fitmate.domain.model.ExercisePrescription
import com.fitmate.domain.model.HomeSummary
import com.fitmate.domain.model.PrescriptionRecommendation
import com.fitmate.domain.model.TodayRoutine
import com.fitmate.domain.model.WeeklyRoutine
import com.fitmate.domain.model.WeeklyRoutineRecommendation
import com.fitmate.domain.model.DailyRoutineRecommendation
import com.fitmate.domain.model.WorkoutDay

interface RoutineRepository {

    // 이번 주 루틴이 없으면 null
    suspend fun getWeeklyRoutine(): Result<WeeklyRoutine?>

    // 오늘 운동이 없으면 (루틴이 없거나 쉬는 날) null
    suspend fun getTodayRoutine(): Result<TodayRoutine?>

    // 홈 화면 요약. 루틴이 없어도 정상 응답한다
    suspend fun getHomeSummary(): Result<HomeSummary>

    // AI에게 맡기기. 이번 주 남은 요일만 만들어진다
    // regenerate = true면 이미 저장된 이번 주 루틴을 다시 만드는 경우 (주간 루틴은 주에 하나만 저장된다)
    suspend fun recommendWeeklyRoutine(
        days: Set<WorkoutDay>,
        sessionMinutes: Int,
        environmentId: Long,
        regenerate: Boolean = false
    ): Result<WeeklyRoutineRecommendation>

    // regenerate = true면 기존 루틴에 덮어쓴다. 지난 날짜와 운동 기록이 있는 날짜는 서버가 그대로 둔다
    suspend fun saveWeeklyRoutine(
        environmentId: Long,
        sessionMinutes: Int,
        days: List<DailyRoutine>,
        regenerate: Boolean = false
    ): Result<Unit>

    // AI와 함께 만들기 - 운동 환경에 맞는 운동 코드 3~4개
    suspend fun recommendExercises(environmentId: Long): Result<List<String>>

    // 운동 코드별 처방값 추천. 추천이 없는 운동은 결과에서 빠진다
    suspend fun recommendPrescriptions(exerciseCodes: List<String>): Result<Map<String, PrescriptionRecommendation>>

    suspend fun summarizeDailyRoutine(exercises: List<ExercisePrescription>): Result<DailyRoutineSummary>

    // 직접 만든 주간 루틴 저장. firstSelectedDay는 사용자가 가장 먼저 고른 요일
    suspend fun saveCustomRoutine(
        firstSelectedDay: WorkoutDay,
        environmentId: Long,
        days: List<DailyRoutine>,
        regenerate: Boolean = false
    ): Result<Unit>


    suspend fun recommendDailyRoutine(
        environmentId: Long,
        sessionMinutes: Int
    ): Result<DailyRoutineRecommendation>

    suspend fun saveDailyRoutine(
        environmentId: Long,
        sessionMinutes: Int,
        recommendation: DailyRoutineRecommendation
    ): Result<Unit>
}
