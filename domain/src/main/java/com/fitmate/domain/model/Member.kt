
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

// 회원 프로필에서 등록한 운동 환경
data class MemberWorkoutEnvironment(
    val id: Long,
    val location: ExerciseLocation,
    val gymName: String?,
    val equipmentCodes: List<String>
)

// 회원의 기본 운동 장소 및 등록 환경 목록
data class MemberWorkoutEnvironments(
    val primaryLocation: ExerciseLocation,
    val environments: List<MemberWorkoutEnvironment>
)
