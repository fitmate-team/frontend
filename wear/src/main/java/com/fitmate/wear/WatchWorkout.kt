package com.fitmate.wear

data class WatchSet(val weightKg: Int, val reps: Int)

data class WatchExercise(
    val name: String,
    val sets: List<WatchSet>,
    val previous: WatchSet?
)

data class WatchWorkout(
    val title: String,
    val exercises: List<WatchExercise>,
    val restSeconds: Int
) {
    val totalSets: Int get() = exercises.sumOf { it.sets.size }
}

// 휴대폰 동기화 전까지 사용하는 예시 데이터
val SampleWatchWorkout = WatchWorkout(
    title = "상체 PUSH",
    exercises = listOf(
        WatchExercise("Bench Press", List(4) { WatchSet(40, 8) }, previous = WatchSet(40, 8)),
        WatchExercise("Shoulder Press", List(3) { WatchSet(12, 10) }, previous = WatchSet(12, 10)),
        WatchExercise("Chest Press", List(3) { WatchSet(30, 10) }, previous = WatchSet(25, 10)),
        WatchExercise("Lateral Raise", List(3) { WatchSet(5, 12) }, previous = null),
        WatchExercise("Triceps Pushdown", List(3) { WatchSet(15, 12) }, previous = WatchSet(15, 10))
    ),
    restSeconds = 90
)

// 운동 진행 위치 (몇 번째 종목의 몇 번째 세트)
data class WatchProgress(val exerciseIndex: Int, val setIndex: Int)

sealed interface WatchStep {
    data object Ready : WatchStep
    data class Exercising(val progress: WatchProgress) : WatchStep

    // 완료한 세트 다음 세트(next)를 기다리는 휴식
    data class Resting(val next: WatchProgress, val totalSeconds: Int) : WatchStep

    // 심박수가 너무 높을 때. 휴식하러 가면 next 세트 전 휴식으로 넘어간다
    data class HeartRateWarning(val next: WatchProgress) : WatchStep
    data class Finished(val elapsedMinutes: Int) : WatchStep
}

// 방금 끝낸 세트 다음 위치. 마지막 세트였으면 null
fun WatchWorkout.nextAfter(progress: WatchProgress): WatchProgress? {
    val exercise = exercises[progress.exerciseIndex]
    return when {
        progress.setIndex + 1 < exercise.sets.size -> progress.copy(setIndex = progress.setIndex + 1)
        progress.exerciseIndex + 1 < exercises.size -> WatchProgress(progress.exerciseIndex + 1, 0)
        else -> null
    }
}
