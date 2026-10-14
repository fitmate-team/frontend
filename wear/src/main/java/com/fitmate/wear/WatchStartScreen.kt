package com.fitmate.wear

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
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
import com.fitmate.wear.component.WatchProgressDots
import com.fitmate.wear.component.WatchScreen
import com.fitmate.wear.component.watchTextStyle

// 워치 - 갤럭시 워치 연동 눌렀을 때 (오늘 운동)
@Composable
fun WatchStartScreen(
    workout: WatchWorkout,
    completedExercises: Int = 0,
    onStart: () -> Unit = {}
) {
    WatchScreen {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "오늘 운동",
                style = watchTextStyle(12.sp, 15.sp, letterSpacing = 1.sp),
                color = White,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = workout.title,
                style = watchTextStyle(20.sp, 30.sp, FontWeight.Medium),
                color = White,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                WatchOutlinedChip(text = "${workout.exercises.size}종목")
                WatchOutlinedChip(text = "${workout.totalSets}세트")
            }
            WatchProgressDots(
                total = workout.exercises.size,
                completed = completedExercises,
                modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)
            )
            WatchCircleButton(text = "시작", onClick = onStart, size = 72.dp)
            Text(
                text = "$completedExercises / ${workout.exercises.size} 완료",
                style = watchTextStyle(12.sp, 15.sp),
                color = White,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Preview(widthDp = 304, heightDp = 304)
@Composable
private fun WatchStartScreenPreview() {
    WatchStartScreen(workout = SampleWatchWorkout)
}
