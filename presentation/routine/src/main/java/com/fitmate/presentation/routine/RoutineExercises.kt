package com.fitmate.presentation.routine

import com.fitmate.presentation.designsystem.component.SearchableExercise
import com.fitmate.presentation.designsystem.component.WeekDayLabels

// 루틴에 넣을 수 있는 운동. name은 검색 목록 표기, shortName은 루틴 카드 / 운동 설정 탭 표기
internal data class RoutineExercise(
    val name: String,
    val shortName: String,
    val bodyPart: String,
    val equipment: String,
    val filter: String
) {
    val description: String get() = "$bodyPart · $equipment"
}

// 서버 연동 전까지 사용하는 운동 목록
internal val RoutineExerciseCatalog = listOf(
    RoutineExercise("벤치 프레스", "벤치프레스", "가슴", "프리웨이트", "가슴"),
    RoutineExercise("인클라인 덤벨프레스", "인클라인 덤벨프레스", "가슴", "프리웨이트", "가슴"),
    RoutineExercise("숄더 프레스", "숄더프레스", "어깨", "프리웨이트", "어깨"),
    RoutineExercise("체스트 프레스", "체스트프레스", "가슴", "머신/케이블", "가슴"),
    RoutineExercise("레그 프레스", "레그프레스", "하체", "머신/케이블", "하체/둔근"),
    RoutineExercise("랫풀다운", "랫풀다운", "등", "머신/케이블", "등"),
    RoutineExercise("줄넘기", "줄넘기", "유산소", "전신", "전신"),
    RoutineExercise("러닝머신", "러닝머신", "유산소", "전신", "전신")
)

internal val RoutineSearchableExercises = RoutineExerciseCatalog.map {
    SearchableExercise(name = it.name, description = it.description, filter = it.filter)
}

// "AI에게 추천받기"를 눌렀을 때 미리 골라주는 운동 (예시)
val AiRecommendedExerciseNames = listOf("벤치 프레스", "숄더 프레스", "체스트 프레스")

internal fun shortNameOf(name: String): String =
    RoutineExerciseCatalog.firstOrNull { it.name == name }?.shortName ?: name

private val UpperBodyParts = setOf("가슴", "등", "어깨", "이두", "삼두")

// 운동 부위로 루틴 이름을 정한다 (상체 / 하체 / 유산소 / 전신)
internal fun routineTitleOf(exerciseNames: List<String>): String {
    val parts = exerciseNames.mapNotNull { name -> RoutineExerciseCatalog.firstOrNull { it.name == name }?.bodyPart }.toSet()
    return when {
        parts.isEmpty() -> "전신"
        parts.all { it in UpperBodyParts } -> "상체"
        parts.all { it == "하체" } -> "하체"
        parts.all { it == "유산소" } -> "유산소"
        else -> "전신"
    }
}

// 운동 하나의 세부 설정 (운동 설정 화면에서 입력)
data class ExerciseSetting(
    val name: String,
    val sets: String = "",
    val reps: String = "",
    val weight: String = "",
    val rest: String? = null
)

// 직접 만든 루틴 초안. 요일별로 설정한 운동 목록
data class ManualRoutineDraft(
    val days: Map<String, List<ExerciseSetting>> = emptyMap()
) {
    val orderedDays: List<String> get() = WeekDayLabels.filter { it in days }
}

internal val RestTimeOptions = listOf("30초", "60초", "90초", "2분", "3분")
