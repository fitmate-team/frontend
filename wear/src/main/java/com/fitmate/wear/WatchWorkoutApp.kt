package com.fitmate.wear

import android.os.SystemClock
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay

private const val AddRestSeconds = 30

// 워치 운동 흐름: 시작 → 세트 완료 → 휴식 → ... → 운동 끝
// 심박수가 너무 높으면 HeartRateWarning으로 넘어간다 (심박 센서 연동 전이라 아직 진입 경로 없음)
@Composable
fun WatchWorkoutApp(
    workout: WatchWorkout = SampleWatchWorkout,
    onFinish: () -> Unit = {}
) {
    var step by remember { mutableStateOf<WatchStep>(WatchStep.Ready) }
    var startedAt by remember { mutableLongStateOf(0L) }

    fun startRest(next: WatchProgress) {
        step = WatchStep.Resting(next = next, totalSeconds = workout.restSeconds)
    }

    BackHandler(enabled = step is WatchStep.Resting || step is WatchStep.HeartRateWarning) {
        // 휴식 / 경고 화면에서 뒤로가면 휴식을 건너뛴다
        when (val current = step) {
            is WatchStep.Resting -> step = WatchStep.Exercising(current.next)
            is WatchStep.HeartRateWarning -> startRest(current.next)
            else -> Unit
        }
    }

    when (val current = step) {
        WatchStep.Ready -> WatchStartScreen(
            workout = workout,
            onStart = {
                startedAt = SystemClock.elapsedRealtime()
                step = WatchStep.Exercising(WatchProgress(0, 0))
            }
        )

        is WatchStep.Exercising -> WatchExerciseScreen(
            exercise = workout.exercises[current.progress.exerciseIndex],
            setIndex = current.progress.setIndex,
            onComplete = {
                val next = workout.nextAfter(current.progress)
                if (next == null) {
                    val elapsedMillis = SystemClock.elapsedRealtime() - startedAt
                    step = WatchStep.Finished(elapsedMinutes = (elapsedMillis / 60_000).toInt())
                } else {
                    startRest(next)
                }
            }
        )

        is WatchStep.HeartRateWarning -> WatchHeartRateWarningScreen(
            onRest = { startRest(current.next) }
        )

        is WatchStep.Resting -> RestCountdown(
            workout = workout,
            resting = current,
            onAddTime = { step = current.copy(totalSeconds = current.totalSeconds + AddRestSeconds) },
            onDone = { step = WatchStep.Exercising(current.next) }
        )

        is WatchStep.Finished -> WatchFinishScreen(
            elapsedMinutes = current.elapsedMinutes,
            totalSets = workout.totalSets,
            onConfirm = onFinish
        )
    }
}

// 1초마다 줄어드는 휴식 타이머. +30은 남은 시간과 전체 시간을 함께 늘린다
@Composable
private fun RestCountdown(
    workout: WatchWorkout,
    resting: WatchStep.Resting,
    onAddTime: () -> Unit,
    onDone: () -> Unit
) {
    var remaining by remember(resting.next) { mutableIntStateOf(resting.totalSeconds) }
    val currentOnDone by rememberUpdatedState(onDone)

    LaunchedEffect(resting.next) {
        while (remaining > 0) {
            delay(1_000)
            remaining--
        }
        currentOnDone()
    }

    val exercise = workout.exercises[resting.next.exerciseIndex]
    WatchRestScreen(
        remainingSeconds = remaining,
        totalSeconds = resting.totalSeconds,
        nextSet = exercise.sets[resting.next.setIndex],
        nextSetNumber = resting.next.setIndex + 1,
        setCount = exercise.sets.size,
        onAddTime = {
            remaining += AddRestSeconds
            onAddTime()
        },
        onSkip = onDone
    )
}
