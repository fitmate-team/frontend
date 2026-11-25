
package com.fitmate.presentation.workout.recommendatio

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

import com.fitmate.presentation.designsystem.R
import com.fitmate.presentation.designsystem.theme.*
import com.fitmate.presentation.workout.today.PrimaryWorkoutButton

data class WorkoutChange(
    val previousExercise: String,
    val newExercise: String
)

@Composable
fun WorkoutRecommendationScreen(
    workoutChanges: List<WorkoutChange> = emptyList(),
    onBack: () -> Unit = {},
    onPreviewClick: () -> Unit = {},
    onStartWorkout: () -> Unit = {},
    onEditClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .border(0.5.dp, BorderDefault)
                .padding(horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                contentDescription = "뒤로가기",
                modifier = Modifier
                    .size(24.dp)
                    .clickable(onClick = onBack),
                tint = TextPrimary
            )

            Text(
                text = "운동 루틴 다시 추천",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        }

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(
                start = 24.dp,
                end = 24.dp,
                top = 24.dp,
                bottom = 24.dp
            ),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Text(
                    text = "루틴을 바꾸었어요.",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = "사용 가능한 운동기구에 맞춰 운동 구성을 조정했어요.",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )

                Spacer(Modifier.height(10.dp))
            }

            if (workoutChanges.isEmpty()) {
                item {
                    Text(
                        text = "변경된 운동 목록이 없어요.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                }
            } else {
                items(workoutChanges) { change ->
                    WorkoutChangeCard(change)
                }
            }

            item {
                Spacer(Modifier.height(4.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .border(
                            1.dp,
                            BorderDefault,
                            RoundedCornerShape(12.dp)
                        )
                        .clickable(onClick = onPreviewClick)
                        .padding(horizontal = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(
                            R.drawable.ic_workout_preview
                        ),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier.size(18.dp)
                    )

                    Spacer(Modifier.width(8.dp))

                    Text(
                        text = "전체 루틴 확인하기",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextPrimary
                    )

                    Spacer(Modifier.weight(1f))

                    Icon(
                        painter = painterResource(
                            R.drawable.ic_workout_chevron_right
                        ),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 24.dp,
                    vertical = 16.dp
                ),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            PrimaryWorkoutButton(
                label = "변경된 루틴으로 운동하기",
                onClick = onStartWorkout
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .border(
                        1.dp,
                        Blue600,
                        RoundedCornerShape(12.dp)
                    )
                    .clickable(onClick = onEditClick),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "직접 수정하기",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Blue600
                )
            }
        }
    }
}

@Composable
private fun WorkoutChangeCard(
    change: WorkoutChange
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                1.dp,
                BorderDefault,
                RoundedCornerShape(14.dp)
            )
            .padding(
                horizontal = 20.dp,
                vertical = 14.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = change.previousExercise,
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary
        )

        Spacer(Modifier.height(4.dp))

        Icon(
            imageVector = Icons.Rounded.KeyboardArrowDown,
            contentDescription = "변경",
            tint = Blue600,
            modifier = Modifier.size(24.dp)
        )

        Spacer(Modifier.height(4.dp))

        Text(
            text = change.newExercise,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = Blue600
        )
    }
}
