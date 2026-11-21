package com.fitmate.domain.model

// 내 정보 중 루틴 화면에서 쓰는 값
data class MemberInfo(
    val height: Double,
    val weight: Double?,
    val exerciseLevel: ExerciseLevel,
    val primaryGoal: PrimaryGoal,
    val goalStrategy: GoalStrategy,
    val sessionMinutes: Int,
    val availableDays: Set<WorkoutDay>,
    val exerciseLocation: ExerciseLocation
)

// 등록된 운동 환경. 집 / 야외는 하나씩, 헬스장은 여러 개일 수 있다
data class WorkoutEnvironment(
    val id: Long,
    val location: ExerciseLocation,
    val gymName: String?,
    val equipmentCodes: List<String>
)

data class WorkoutEnvironments(
    val primaryLocation: ExerciseLocation,
    val environments: List<WorkoutEnvironment>
)
