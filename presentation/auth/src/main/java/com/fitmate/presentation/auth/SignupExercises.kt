package com.fitmate.presentation.auth

import com.fitmate.presentation.designsystem.component.SearchableExercise

// 기록 입력 칸 구성: 중량 운동(kg / 회 / 세트), 맨몸 운동(회 / 세트), 유산소(분)
internal enum class RecordType { Weight, Bodyweight, Cardio }

internal data class SignupExercise(
    val name: String,
    val bodyPart: String,
    val equipment: String,
    val filter: String,
    val recordType: RecordType
) {
    val description: String get() = "$bodyPart · $equipment"
}

// 서버 연동 전까지 사용하는 운동 목록
internal val SignupExerciseCatalog = listOf(
    SignupExercise("벤치 프레스", "가슴", "프리웨이트", "가슴", RecordType.Weight),
    SignupExercise("인클라인 덤벨프레스", "가슴", "프리웨이트", "가슴", RecordType.Weight),
    SignupExercise("숄더 프레스", "어깨", "프리웨이트", "어깨", RecordType.Weight),
    SignupExercise("체스트 프레스", "가슴", "머신/케이블", "가슴", RecordType.Weight),
    SignupExercise("레그 프레스", "하체", "머신/케이블", "하체/둔근", RecordType.Weight),
    SignupExercise("랫풀다운", "등", "머신/케이블", "등", RecordType.Weight),
    SignupExercise("줄넘기", "유산소", "전신", "전신", RecordType.Cardio),
    SignupExercise("러닝머신", "유산소", "전신", "전신", RecordType.Cardio),
    SignupExercise("스쿼트", "하체", "맨몸", "하체/둔근", RecordType.Bodyweight)
)

internal val SignupSearchableExercises = SignupExerciseCatalog.map {
    SearchableExercise(name = it.name, description = it.description, filter = it.filter)
}

internal fun findSignupExercise(name: String): SignupExercise? =
    SignupExerciseCatalog.firstOrNull { it.name == name }
