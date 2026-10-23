package com.fitmate.presentation.auth

import com.fitmate.domain.model.BaselineRecordType
import com.fitmate.domain.model.BodyArea
import com.fitmate.domain.model.EquipmentCategory
import com.fitmate.domain.model.ExerciseLocation
import com.fitmate.domain.model.MuscleGroup
import com.fitmate.domain.model.WorkoutDay
import com.fitmate.presentation.designsystem.component.WORKOUT_PLACE_GYM
import com.fitmate.presentation.designsystem.component.WORKOUT_PLACE_HOME
import com.fitmate.presentation.designsystem.component.WORKOUT_PLACE_OUTDOOR
import com.fitmate.presentation.designsystem.component.WeekDayLabels

// 공통 선택 컴포넌트(designsystem)는 화면 문구로 선택값을 주고받는다. 서버 enum과의 변환을 모아둔다

internal val WorkoutDayLabels: Map<String, WorkoutDay> = WeekDayLabels.zip(WorkoutDay.entries).toMap()

internal val PlaceLabels: Map<String, ExerciseLocation> = mapOf(
    WORKOUT_PLACE_GYM to ExerciseLocation.GYM,
    WORKOUT_PLACE_HOME to ExerciseLocation.HOME,
    WORKOUT_PLACE_OUTDOOR to ExerciseLocation.OUTDOOR
)

internal fun ExerciseLocation.label(): String = PlaceLabels.entries.first { it.value == this }.key

// WorkoutDurationSelector 문구 -> 분. "90분 이상"은 90분으로 보낸다
internal fun durationLabelToMinutes(label: String): Int? = label.takeWhile { it.isDigit() }.toIntOrNull()

internal val BodyAreaLabels: List<Pair<BodyArea, String>> = listOf(
    BodyArea.LOWER_BACK to "허리",
    BodyArea.KNEE to "무릎",
    BodyArea.SHOULDER to "어깨",
    BodyArea.WRIST to "손목",
    BodyArea.ANKLE to "발목",
    BodyArea.NECK to "목"
)

// 기구 선택 화면 섹션 순서와 제목
internal val EquipmentCategorySections: List<Pair<EquipmentCategory, String>> = listOf(
    EquipmentCategory.FREE_WEIGHT to "프리 웨이트",
    EquipmentCategory.BAND_RESISTANCE to "밴드/저항",
    EquipmentCategory.BENCH_RACK to "벤치/랙",
    EquipmentCategory.ACCESSORY to "보조 기구",
    EquipmentCategory.MACHINE_CABLE to "머신/케이블",
    EquipmentCategory.CARDIO to "유산소"
)

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

internal fun EquipmentCategory.label(): String = when (this) {
    EquipmentCategory.FREE_WEIGHT -> "프리웨이트"
    EquipmentCategory.BAND_RESISTANCE -> "밴드/저항"
    EquipmentCategory.BENCH_RACK -> "벤치/랙"
    EquipmentCategory.ACCESSORY -> "보조 기구"
    EquipmentCategory.CARDIO -> "유산소 기구"
    EquipmentCategory.MACHINE_CABLE -> "머신/케이블"
}

// 기록 입력 칸 구성: 중량 운동(kg / 회 / 세트), 맨몸 운동(회 / 세트), 유산소(분)
enum class RecordType { Weight, Bodyweight, Cardio }

internal fun BaselineRecordType.toRecordType(): RecordType = when (this) {
    BaselineRecordType.WEIGHT_REPS_SETS -> RecordType.Weight
    BaselineRecordType.REPS_SETS -> RecordType.Bodyweight
    BaselineRecordType.DURATION -> RecordType.Cardio
}
