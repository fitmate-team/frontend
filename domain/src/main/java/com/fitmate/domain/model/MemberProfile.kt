package com.fitmate.domain.model

// 서버 enum 이름과 1:1로 맞춘다 (요청 / 응답 직렬화 시 name 그대로 사용)

enum class Gender { MALE, FEMALE }

enum class ExerciseLevel { FIRST_TIME, BEGINNER, INTERMEDIATE, ADVANCED }

enum class CurrentExerciseStatus { STARTING, REGULARLY_EXERCISING, RESTARTING }

enum class PrimaryGoal { FAT_LOSS, MUSCLE_GAIN, STRENGTH_GAIN, BODY_RECOMPOSITION, FITNESS_IMPROVEMENT, HABIT_FORMATION }

// 목표별 세부 운동 방식. goal로 어떤 목표에 속하는지 알 수 있다
enum class GoalStrategy(val goal: PrimaryGoal) {
    FAT_LOSS_STRENGTH_FOCUS(PrimaryGoal.FAT_LOSS),
    FAT_LOSS_CARDIO_FOCUS(PrimaryGoal.FAT_LOSS),
    FAT_LOSS_BALANCED(PrimaryGoal.FAT_LOSS),
    MUSCLE_GAIN_HYPERTROPHY_FOCUS(PrimaryGoal.MUSCLE_GAIN),
    MUSCLE_GAIN_BODY_PART_FOCUS(PrimaryGoal.MUSCLE_GAIN),
    MUSCLE_GAIN_FULL_BODY_BALANCED(PrimaryGoal.MUSCLE_GAIN),
    STRENGTH_BIG_THREE_FOCUS(PrimaryGoal.STRENGTH_GAIN),
    STRENGTH_UPPER_BODY_FOCUS(PrimaryGoal.STRENGTH_GAIN),
    STRENGTH_LOWER_BODY_FOCUS(PrimaryGoal.STRENGTH_GAIN),
    STRENGTH_OVERALL(PrimaryGoal.STRENGTH_GAIN),
    RECOMPOSITION_FAT_LOSS_PRIORITY(PrimaryGoal.BODY_RECOMPOSITION),
    RECOMPOSITION_MUSCLE_GAIN_PRIORITY(PrimaryGoal.BODY_RECOMPOSITION),
    RECOMPOSITION_BALANCED(PrimaryGoal.BODY_RECOMPOSITION),
    FITNESS_CARDIORESPIRATORY_FOCUS(PrimaryGoal.FITNESS_IMPROVEMENT),
    FITNESS_MUSCULAR_ENDURANCE_FOCUS(PrimaryGoal.FITNESS_IMPROVEMENT),
    FITNESS_OVERALL(PrimaryGoal.FITNESS_IMPROVEMENT),
    HABIT_SHORT_AND_FREQUENT(PrimaryGoal.HABIT_FORMATION),
    HABIT_THREE_TIMES_A_WEEK(PrimaryGoal.HABIT_FORMATION),
    HABIT_GRADUAL_INTENSITY(PrimaryGoal.HABIT_FORMATION)
}

enum class ExerciseLocation { GYM, HOME, OUTDOOR }

enum class WorkoutDay { MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY }

enum class BodyArea { LOWER_BACK, KNEE, SHOULDER, WRIST, ANKLE, NECK }

// 회원가입 시 입력한 최근 운동 기록. 기록 방식에 따라 채워지는 값이 다르다
data class RecentExerciseRecord(
    val exerciseCode: String,
    val weight: Int? = null,
    val reps: Int? = null,
    val sets: Int? = null,
    val durationSeconds: Int? = null
)

data class SignUpForm(
    val loginId: String,
    val password: String,
    val gender: Gender,
    val age: Int,
    val height: Double,
    val weight: Double,
    val skeletalMuscleMass: Double?,
    val bodyFatPercentage: Double?,
    val bodyFatMass: Double?,
    val exerciseLevel: ExerciseLevel,
    val currentExerciseStatus: CurrentExerciseStatus,
    val primaryGoal: PrimaryGoal,
    val goalStrategy: GoalStrategy,
    val sessionMinutes: Int,
    val availableDays: Set<WorkoutDay>,
    val exerciseLocation: ExerciseLocation,
    val gymName: String?,
    val equipmentCodes: Set<String>,
    val avoidBodyAreas: Set<BodyArea>,
    val excludedExerciseCodes: Set<String>,
    val recentExerciseRecords: List<RecentExerciseRecord>
)
