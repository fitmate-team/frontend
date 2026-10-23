package com.fitmate.domain.model

enum class EquipmentCategory { FREE_WEIGHT, BAND_RESISTANCE, BENCH_RACK, ACCESSORY, CARDIO, MACHINE_CABLE }

data class Equipment(
    val code: String,
    val name: String,
    val category: EquipmentCategory
)

enum class MuscleGroup { CHEST, TRICEPS, BACK, SHOULDERS, LEGS_GLUTES, BICEPS, CORE, FULL_BODY, CARDIO }

enum class ExerciseType { STRENGTH, CORE, HIIT, CARDIO }

// 최근 기록 입력 방식: 중량 x 횟수 x 세트 / 횟수 x 세트 / 시간
enum class BaselineRecordType { WEIGHT_REPS_SETS, REPS_SETS, DURATION }

data class Exercise(
    val code: String,
    val name: String,
    val type: ExerciseType,
    val recordType: BaselineRecordType,
    val primaryMuscle: MuscleGroup,
    val equipmentCategories: Set<EquipmentCategory>
)
