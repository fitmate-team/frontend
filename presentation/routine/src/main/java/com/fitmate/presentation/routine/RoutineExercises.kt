package com.fitmate.presentation.routine

import com.fitmate.domain.model.BaselineRecordType
import com.fitmate.domain.model.ExercisePrescription
import com.fitmate.domain.model.MuscleGroup
import com.fitmate.presentation.designsystem.component.WeekDayLabels
import kotlin.math.roundToInt

// 운동 하나의 세부 설정 (운동 설정 화면에서 입력). 입력 칸 문구 그대로 들고 있다가 저장할 때 숫자로 바꾼다
// 유산소(DURATION)는 시간(분)만, 그 외는 세트 / 횟수 / 중량 / 휴식을 쓴다
data class ExerciseSetting(
    val code: String,
    val name: String,
    val recordType: BaselineRecordType,
    val muscle: MuscleGroup,
    val sets: String = "",
    val reps: String = "",
    val weight: String = "",
    val minutes: String = "",
    val rest: String? = null
) {
    val isCardio: Boolean get() = recordType == BaselineRecordType.DURATION

    // 중량은 비워두면 운동 시작 전에 설정한다
    val isComplete: Boolean
        get() = if (isCardio) {
            (minutes.toIntOrNull() ?: 0) > 0
        } else {
            (sets.toIntOrNull() ?: 0) > 0 && (reps.toIntOrNull() ?: 0) > 0
        }

    fun toPrescription(): ExercisePrescription = if (isCardio) {
        ExercisePrescription(
            exerciseCode = code,
            name = name,
            sets = 1,
            reps = null,
            weightKg = null,
            durationSeconds = (minutes.toIntOrNull() ?: 0) * 60,
            restSeconds = 0
        )
    } else {
        ExercisePrescription(
            exerciseCode = code,
            name = name,
            sets = sets.toIntOrNull() ?: 0,
            reps = reps.toIntOrNull(),
            weightKg = weight.toDoubleOrNull()?.roundToInt()
                ?.takeIf { recordType == BaselineRecordType.WEIGHT_REPS_SETS && it > 0 },
            durationSeconds = null,
            restSeconds = rest?.let(::restLabelToSeconds) ?: DEFAULT_REST_SECONDS
        )
    }
}

// 직접 만든 루틴 초안. 요일 문구("월")별로 설정한 운동 목록. 요일은 처음 고른 순서대로 들어 있다
data class ManualRoutineDraft(
    val days: Map<String, List<ExerciseSetting>> = emptyMap()
) {
    val orderedDays: List<String> get() = WeekDayLabels.filter { it in days }
}

internal val RestTimeOptions = listOf("30초", "60초", "90초", "2분", "3분")

internal const val DEFAULT_REST_SECONDS = 60

private val UpperBodyMuscles = setOf(
    MuscleGroup.CHEST, MuscleGroup.BACK, MuscleGroup.SHOULDERS, MuscleGroup.BICEPS, MuscleGroup.TRICEPS
)

// 운동 부위로 루틴 이름을 정한다 (상체 / 하체 / 유산소 / 전신). 저장할 때는 AI가 부위를 다시 정한다
internal fun routineTitleOf(exercises: List<ExerciseSetting>): String {
    val muscles = exercises.map { it.muscle }.toSet()
    return when {
        muscles.isEmpty() -> "전신"
        muscles.all { it in UpperBodyMuscles } -> "상체"
        muscles.all { it == MuscleGroup.LEGS_GLUTES } -> "하체"
        muscles.all { it == MuscleGroup.CARDIO } -> "유산소"
        else -> "전신"
    }
}

// 운동 검색 부위 칩(ExerciseSearchFilters)과 같은 문구. 유산소는 별도 칩이 없어 전신으로 묶는다
internal fun MuscleGroup.filterLabel(): String = when (this) {
    MuscleGroup.CHEST -> "가슴"
    MuscleGroup.BACK -> "등"
    MuscleGroup.SHOULDERS -> "어깨"
    MuscleGroup.BICEPS -> "이두"
    MuscleGroup.TRICEPS -> "삼두"
    MuscleGroup.LEGS_GLUTES -> "하체/둔근"
    MuscleGroup.CORE -> "코어"
    MuscleGroup.FULL_BODY, MuscleGroup.CARDIO -> "전신"
}

internal fun MuscleGroup.bodyPartLabel(): String = when (this) {
    MuscleGroup.LEGS_GLUTES -> "하체"
    MuscleGroup.CARDIO -> "유산소"
    else -> filterLabel()
}
