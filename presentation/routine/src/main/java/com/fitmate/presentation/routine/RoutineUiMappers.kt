package com.fitmate.presentation.routine

import com.fitmate.domain.model.BaselineRecordType
import com.fitmate.domain.model.DailyRoutine
import com.fitmate.domain.model.ExercisePrescription
import java.time.LocalDate
import kotlin.math.roundToInt

// 처방값 -> 운동 목록 한 줄. recordType은 운동 목록에서 찾은 기록 방식으로, 중량 설정 필요 배지를 정할 때 쓴다
internal fun ExercisePrescription.toPlannedExercise(recordType: BaselineRecordType?): PlannedExercise {
    val isCardio = recordType == BaselineRecordType.DURATION || (durationSeconds != null && reps == null)
    val repsAndSets = "${reps ?: "-"}회 × ${sets}세트"
    val detail = when {
        isCardio -> "${(durationSeconds ?: 0) / 60}분"
        weightKg != null -> "${weightKg}kg / $repsAndSets"
        else -> repsAndSets
    }
    return PlannedExercise(
        name = name,
        detail = detail,
        restTime = if (isCardio) null else "휴식 ${restLabel(restSeconds)}",
        needWeight = recordType == BaselineRecordType.WEIGHT_REPS_SETS && weightKg == null,
        isCardio = isCardio
    )
}

// 유산소는 휴식이 없어서 평균에서 뺀다
private fun List<ExercisePrescription>.averageRestSeconds(): Int =
    filter { it.reps != null }.map { it.restSeconds }.takeIf { it.isNotEmpty() }?.average()?.roundToInt() ?: 0

internal fun DailyRoutine.toDetailUi(recordTypes: Map<String, BaselineRecordType>): RoutineDayDetailUi = RoutineDayDetailUi(
    title = mainTarget.label(),
    dateLabel = (date ?: day.dateInThisWeek()).dateLabel(),
    estimatedMinutes = estimatedMinutes,
    averageRestSeconds = exercises.averageRestSeconds(),
    exercises = exercises.map { it.toPlannedExercise(recordTypes[it.exerciseCode]) }
)

internal fun manualDayDetailOf(dayLabel: String, exercises: List<ExerciseSetting>): RoutineDayDetailUi {
    val prescriptions = exercises.map { it.toPrescription() }
    return RoutineDayDetailUi(
        title = routineTitleOf(exercises),
        dateLabel = (workoutDayOf(dayLabel)?.dateInThisWeek() ?: LocalDate.now()).dateLabel(),
        estimatedMinutes = null,
        averageRestSeconds = prescriptions.averageRestSeconds(),
        exercises = exercises.zip(prescriptions) { setting, prescription -> prescription.toPlannedExercise(setting.recordType) }
    )
}

// 루틴 카드에 보여줄 운동 이름 (앞에서 3개)
internal fun List<ExercisePrescription>.namesPreview(): String = take(3).joinToString(" · ") { it.name }
