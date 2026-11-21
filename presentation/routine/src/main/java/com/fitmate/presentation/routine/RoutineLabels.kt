package com.fitmate.presentation.routine

import com.fitmate.domain.model.ExerciseLevel
import com.fitmate.domain.model.ExerciseLocation
import com.fitmate.domain.model.GoalStrategy
import com.fitmate.domain.model.PrimaryGoal
import com.fitmate.domain.model.RoutineTarget
import com.fitmate.domain.model.WorkoutDay
import com.fitmate.presentation.designsystem.component.WORKOUT_PLACE_GYM
import com.fitmate.presentation.designsystem.component.WORKOUT_PLACE_HOME
import com.fitmate.presentation.designsystem.component.WORKOUT_PLACE_OUTDOOR
import com.fitmate.presentation.designsystem.component.WeekDayLabels
import java.time.LocalDate

// 공통 선택 컴포넌트(designsystem)는 화면 문구로 선택값을 주고받는다. 서버 enum과의 변환을 모아둔다

internal fun WorkoutDay.label(): String = WeekDayLabels[ordinal]

internal fun workoutDayOf(label: String): WorkoutDay? =
    WeekDayLabels.indexOf(label).takeIf { it >= 0 }?.let { WorkoutDay.entries[it] }

internal fun ExerciseLocation.label(): String = when (this) {
    ExerciseLocation.GYM -> WORKOUT_PLACE_GYM
    ExerciseLocation.HOME -> WORKOUT_PLACE_HOME
    ExerciseLocation.OUTDOOR -> WORKOUT_PLACE_OUTDOOR
}

internal fun exerciseLocationOf(label: String): ExerciseLocation? =
    ExerciseLocation.entries.firstOrNull { it.label() == label }

// WorkoutDurationSelector 문구 <-> 분. "90분 이상"은 90분으로 본다
internal fun durationLabelToMinutes(label: String): Int? = label.takeWhile { it.isDigit() }.toIntOrNull()

internal fun minutesToDurationLabel(minutes: Int): String = if (minutes >= 90) "90분 이상" else "${minutes}분"

internal fun RoutineTarget.label(): String = when (this) {
    RoutineTarget.CHEST -> "가슴"
    RoutineTarget.BACK -> "등"
    RoutineTarget.SHOULDERS -> "어깨"
    RoutineTarget.LEGS_GLUTES -> "하체"
    RoutineTarget.ARMS -> "팔"
    RoutineTarget.UPPER_BODY -> "상체"
    RoutineTarget.FULL_BODY -> "전신"
    RoutineTarget.CORE -> "코어"
    RoutineTarget.CARDIO -> "유산소"
}

internal fun RoutineTarget.planIcon(): PlanIcon = when (this) {
    RoutineTarget.LEGS_GLUTES -> PlanIcon.Lower
    RoutineTarget.CARDIO -> PlanIcon.Cardio
    else -> PlanIcon.Upper
}

internal fun ExerciseLevel.label(): String = when (this) {
    ExerciseLevel.FIRST_TIME -> "입문"
    ExerciseLevel.BEGINNER -> "초급"
    ExerciseLevel.INTERMEDIATE -> "중급"
    ExerciseLevel.ADVANCED -> "상급"
}

// 회원가입 목표 / 운동 방식 화면과 같은 문구
internal fun PrimaryGoal.label(): String = when (this) {
    PrimaryGoal.FAT_LOSS -> "체지방 감량"
    PrimaryGoal.MUSCLE_GAIN -> "근육 성장"
    PrimaryGoal.STRENGTH_GAIN -> "근력 향상"
    PrimaryGoal.BODY_RECOMPOSITION -> "신체 재구성"
    PrimaryGoal.FITNESS_IMPROVEMENT -> "체력 향상"
    PrimaryGoal.HABIT_FORMATION -> "운동 습관 형성"
}

internal fun GoalStrategy.label(): String = when (this) {
    GoalStrategy.FAT_LOSS_STRENGTH_FOCUS -> "근력 운동 중심"
    GoalStrategy.FAT_LOSS_CARDIO_FOCUS -> "유산소 중심"
    GoalStrategy.FAT_LOSS_BALANCED -> "균형형"
    GoalStrategy.MUSCLE_GAIN_HYPERTROPHY_FOCUS -> "근비대 중심"
    GoalStrategy.MUSCLE_GAIN_BODY_PART_FOCUS -> "부위 집중"
    GoalStrategy.MUSCLE_GAIN_FULL_BODY_BALANCED -> "전신 균형"
    GoalStrategy.STRENGTH_BIG_THREE_FOCUS -> "3대 운동 중심"
    GoalStrategy.STRENGTH_UPPER_BODY_FOCUS -> "상체 근력 중심"
    GoalStrategy.STRENGTH_LOWER_BODY_FOCUS -> "하체 근력 중심"
    GoalStrategy.STRENGTH_OVERALL -> "전반적 근력"
    GoalStrategy.RECOMPOSITION_FAT_LOSS_PRIORITY -> "감량 우선"
    GoalStrategy.RECOMPOSITION_MUSCLE_GAIN_PRIORITY -> "근육 증가 우선"
    GoalStrategy.RECOMPOSITION_BALANCED -> "균형형"
    GoalStrategy.FITNESS_CARDIORESPIRATORY_FOCUS -> "심폐지구력 중심"
    GoalStrategy.FITNESS_MUSCULAR_ENDURANCE_FOCUS -> "근지구력 중심"
    GoalStrategy.FITNESS_OVERALL -> "전반적 체력"
    GoalStrategy.HABIT_SHORT_AND_FREQUENT -> "짧고 자주"
    GoalStrategy.HABIT_THREE_TIMES_A_WEEK -> "주 3회 꾸준히"
    GoalStrategy.HABIT_GRADUAL_INTENSITY -> "점진적 강도"
}

// "9월 21일 (월)"
internal fun LocalDate.dateLabel(): String = "${monthValue}월 ${dayOfMonth}일 (${WeekDayLabels[dayOfWeek.ordinal]})"

// 이번 주(월요일 시작)에서 그 요일의 날짜
internal fun WorkoutDay.dateInThisWeek(today: LocalDate = LocalDate.now()): LocalDate =
    today.minusDays(today.dayOfWeek.ordinal.toLong()).plusDays(ordinal.toLong())

// 휴식 시간 버튼(RestTimeOptions)과 같은 표기: "60초" / "90초" / "2분"
internal fun restLabel(seconds: Int): String =
    if (seconds >= 120 && seconds % 60 == 0) "${seconds / 60}분" else "${seconds}초"

internal fun restLabelToSeconds(label: String): Int? {
    val number = label.takeWhile { it.isDigit() }.toIntOrNull() ?: return null
    return if (label.endsWith("분")) number * 60 else number
}
