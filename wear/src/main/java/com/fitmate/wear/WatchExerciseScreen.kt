package com.fitmate.wear

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fitmate.presentation.designsystem.theme.White
import com.fitmate.wear.component.WatchCircleButton
import com.fitmate.wear.component.WatchOutlinedChip
import com.fitmate.wear.component.WatchScreen
import com.fitmate.wear.component.watchTextStyle

// 워치 - 운동 중 (현재 세트)
@Composable
fun WatchExerciseScreen(
    exercise: WatchExercise,
    setIndex: Int,
    onComplete: () -> Unit = {}
) {
    val set = exercise.sets[setIndex]

    WatchScreen {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = exercise.name.uppercase(),
                style = watchTextStyle(12.sp, 13.5.sp, letterSpacing = 1.5.sp),
                color = White,
                modifier = Modifier.padding(bottom = 6.dp)
            )
            Row {
                Text(
                    text = "${set.weightKg}",
                    style = watchTextStyle(56.sp, 56.sp, FontWeight.Bold),
                    color = White,
                    modifier = Modifier.alignByBaseline()
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "kg",
                    style = watchTextStyle(18.sp, 27.sp),
                    color = White,
                    modifier = Modifier.alignByBaseline()
                )
            }
            Text(
                text = "${set.reps} REPS",
                style = watchTextStyle(14.sp, 21.sp),
                color = White,
                modifier = Modifier.padding(top = 4.dp, bottom = 6.dp)
            )
            WatchOutlinedChip(
                text = "SET ${setIndex + 1} / ${exercise.sets.size}",
                height = 22.dp,
                horizontalPadding = 12.dp,
                textStyle = watchTextStyle(13.sp, 16.5.sp),
                modifier = Modifier.padding(bottom = 14.dp)
            )
            WatchCircleButton(text = "완료", onClick = onComplete, size = 68.dp)
            if (exercise.previous != null) {
                Text(
                    text = "이전 ${exercise.previous.weightKg}kg × ${exercise.previous.reps}",
                    style = watchTextStyle(12.sp, 13.5.sp),
                    color = White,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}

@Preview(widthDp = 304, heightDp = 304)
@Composable
private fun WatchExerciseScreenPreview() {
    WatchExerciseScreen(exercise = SampleWatchWorkout.exercises.first(), setIndex = 1)
}
