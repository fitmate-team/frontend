package com.fitmate.presentation.workout

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
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

data class WorkoutChange(
    val previousExercise: String,
    val newExercise: String
)

@Composable
fun WorkoutRecommendationScreen(
    onBack: () -> Unit = {},
    onPreviewClick: () -> Unit = {},
    onStartWorkout: () -> Unit = {},
    onEditClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {

    val workoutChanges = listOf(
        WorkoutChange(
            previousExercise = "레그프레스",
            newExercise = "덤벨 스쿼트"
        ),
        WorkoutChange(
            previousExercise = "랫풀다운",
            newExercise = "밴드 랫풀다운"
        ),
        WorkoutChange(
            previousExercise = "케이블 로우",
            newExercise = "덤벨 로우"
        )
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(White)
    ) {

        // 상단 헤더
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .border(
                    width = 0.5.dp,
                    color = BorderDefault
                )
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

        // 본문
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

            items(workoutChanges.size) { index ->

                WorkoutChangeCard(
                    change = workoutChanges[index]
                )
            }

            item {

                Spacer(Modifier.height(4.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .border(
                            width = 1.dp,
                            color = BorderDefault,
                            shape = RoundedCornerShape(12.dp)
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

        // 하단 버튼
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
                        width = 1.dp,
                        color = Blue600,
                        shape = RoundedCornerShape(12.dp)
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
            .height(100.dp)
            .border(
                width = 1.dp,
                color = BorderDefault,
                shape = RoundedCornerShape(14.dp)
            )
            .padding(
                horizontal = 20.dp,
                vertical = 8.dp
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
            painter = painterResource(
                R.drawable.ic_workout_change_down
            ),
            contentDescription = "변경",
            modifier = Modifier.size(20.dp),
            tint = Color.Unspecified
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