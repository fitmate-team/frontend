
package com.fitmate.presentation.workout

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccessTime
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material.icons.rounded.EmojiEvents
import androidx.compose.material.icons.rounded.FitnessCenter
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.fitmate.presentation.designsystem.R
import com.fitmate.presentation.designsystem.theme.*

private data class WorkoutTimeOption(
    val minutes: Int,
    val title: String,
    val description: String
)

private val workoutTimeOptions = listOf(
    WorkoutTimeOption(20, "20분", "짧고 집중적인 운동"),
    WorkoutTimeOption(30, "30분", "간결하게 핵심만"),
    WorkoutTimeOption(45, "45분", "표준적인 운동 시간"),
    WorkoutTimeOption(60, "60분", "충분한 볼륨의 운동"),
    WorkoutTimeOption(90, "90분 이상", "고강도 집중 훈련")
)

@Composable
fun WorkoutTimeChangeScreen(
    initialMinutes: Int = 20,
    onBack: () -> Unit = {},
    onNext: (Int) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var selectedMinutes by remember(initialMinutes) {
        mutableIntStateOf(
            workoutTimeOptions.firstOrNull {
                it.minutes == initialMinutes
            }?.minutes ?: 20
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(68.dp)
                .padding(horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_workout_back),
                contentDescription = "뒤로가기",
                modifier = Modifier
                    .size(24.dp)
                    .clickable(onClick = onBack),
                tint = Color.Unspecified
            )

            Text(
                text = "운동 시간 변경",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        }

        HorizontalDivider(color = BorderDefault)

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 24.dp)
        ) {
            Text(
                text = "오늘 얼마나 운동할 수 있나요?",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = "한 번에 운동 가능한 시간을 알려주세요.",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )

            Spacer(Modifier.height(24.dp))

            workoutTimeOptions.forEachIndexed { index, option ->
                val selected = selectedMinutes == option.minutes

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .background(
                            color = if (selected) Blue100 else White,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .border(
                            width = 1.2.dp,
                            color = if (selected) Blue500 else BorderDefault,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .clickable {
                            selectedMinutes = option.minutes
                        }
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(
                                color = if (selected) Blue500 else Gray50,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .border(
                                width = if (selected) 0.dp else 1.dp,
                                color = if (selected) Color.Transparent else BorderDefault,
                                shape = RoundedCornerShape(10.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        val icon = when (option.minutes) {
                            20 -> Icons.Rounded.Bolt
                            30 -> Icons.Rounded.Timer
                            45 -> Icons.Rounded.AccessTime
                            60 -> Icons.Rounded.FitnessCenter
                            else -> Icons.Rounded.EmojiEvents
                        }

                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            modifier = Modifier.size(22.dp),
                            tint = if (selected) White else Gray300
                        )
                    }

                    Column(
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = option.title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )

                        Text(
                            text = option.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }
                }

                if (index != workoutTimeOptions.lastIndex) {
                    Spacer(Modifier.height(12.dp))
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            PrimaryWorkoutButton(
                label = "다음",
                onClick = { onNext(selectedMinutes) }
            )
        }
    }
}
