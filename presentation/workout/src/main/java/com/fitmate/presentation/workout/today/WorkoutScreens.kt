package com.fitmate.presentation.workout.today

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Place
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.fitmate.domain.model.TodayWorkout

import com.fitmate.presentation.designsystem.R
import com.fitmate.presentation.designsystem.component.ExerciseListItem
import com.fitmate.presentation.designsystem.theme.*

data class TodayExercise(
    val name: String,
    val detail: String,
    val rest: String,
    val needsWeight: Boolean = false
)

data class WorkoutTodayUiState(
    val category: String = "",
    val date: String = "",
    val durationMinutes: Int = 0,
    val averageRestSeconds: Int = 0,
    val gymName: String = "",
    val exercises: List<TodayExercise> = emptyList()
)



fun TodayWorkout.toUiState(): WorkoutTodayUiState {
    return WorkoutTodayUiState(
        category = title,
        date = workoutDate,
        durationMinutes = estimatedDurationMinutes,
        averageRestSeconds = averageRestSeconds,
        gymName = locationName,
        exercises = exercises.map { exercise ->

            val detail = when {
                exercise.durationSeconds != null ->
                    "${exercise.durationSeconds}초"

                exercise.weightKg != null &&
                        exercise.reps != null &&
                        exercise.sets != null ->
                    "${exercise.weightKg}kg / ${exercise.reps}회 × ${exercise.sets}세트"

                exercise.reps != null &&
                        exercise.sets != null ->
                    "${exercise.reps}회 × ${exercise.sets}세트"

                else -> "운동 정보 없음"
            }

            TodayExercise(
                name = exercise.exerciseName ?: "운동 이름 없음",
                detail = detail,
                rest = exercise.restSeconds?.let {
                    "휴식 ${it}초"
                } ?: "",
                needsWeight = exercise.weightRequired && exercise.weightKg == null
            )
        }
    )
}


@Composable
fun TodayWorkoutScreen(
    modifier: Modifier = Modifier,
    state: WorkoutTodayUiState = WorkoutTodayUiState(),
    isPreview: Boolean = false,
    onBack: () -> Unit = {},
    onWeightSettingClick: (Int) -> Unit = {},
    onChangeLocation: () -> Unit = {},
    onStartWorkout: () -> Unit = {}
) {
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
                .padding(horizontal = Spacing6),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Spacing3)
            ) {
                if (isPreview) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = "뒤로가기",
                        modifier = Modifier
                            .size(24.dp)
                            .clickable(onClick = onBack),
                        tint = TextPrimary
                    )
                }

                Text(
                    text = if (isPreview) "미리보기" else "오늘의 운동",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }

            // 운동 환경 변경 버튼
            if (!isPreview) {
                Row(
                    modifier = Modifier
                        .border(
                            width = 1.dp,
                            color = BorderDefault,
                            shape = ShapeFull
                        )
                        .clickable(onClick = onChangeLocation)
                        .padding(
                            horizontal = Spacing3,
                            vertical = Spacing2
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(Spacing1)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Place,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = TextSecondary
                    )

                    Text(
                        text = "운동 환경 변경하기",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }
            }
        }

        // 운동 정보 및 리스트
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = Spacing6,
                end = Spacing6,
                top = Spacing4,
                bottom = Spacing3
            ),
            verticalArrangement = Arrangement.spacedBy(Spacing2)
        ) {
            item {
                // 운동 카테고리 및 날짜
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = Spacing4),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(
                                color = Blue100,
                                shape = ShapeMedium
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(
                                R.drawable.ic_workout_fitness_center
                            ),
                            contentDescription = "운동 카테고리",
                            tint = Color.Unspecified
                        )
                    }

                    Spacer(Modifier.width(Spacing4))

                    Text(
                        text = state.category,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )

                    Spacer(Modifier.weight(1f))

                    Text(
                        text = state.date,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }

                HorizontalDivider(color = BorderDefault)

                // 운동 통계
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = Spacing2),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    WorkoutStat(
                        value = "${state.durationMinutes}분",
                        label = "예상 시간",
                        modifier = Modifier.weight(1f)
                    )

                    VerticalDivider(
                        modifier = Modifier.height(46.dp),
                        color = BorderDefault
                    )

                    WorkoutStat(
                        value = "${state.exercises.size}종목",
                        label = "운동 종목",
                        modifier = Modifier.weight(1f)
                    )

                    VerticalDivider(
                        modifier = Modifier.height(46.dp),
                        color = BorderDefault
                    )

                    WorkoutStat(
                        value = state.gymName,
                        label = "운동 장소",
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(Modifier.height(Spacing4))
            }

            // 운동 아이템은 클릭해도 이동하지 않음
            itemsIndexed(state.exercises) { index, exercise ->
                ExerciseListItem(
                    order = (index + 1)
                        .toString()
                        .padStart(2, '0'),
                    name = exercise.name,
                    detail = exercise.detail,
                    restTime = exercise.rest,
                    needWeight = exercise.needsWeight
                )
            }

            if (!isPreview) {
                item {
                    Spacer(Modifier.height(Spacing1))

                    PrimaryWorkoutButton(
                        label = "시작하기",
                        onClick = {
                            val firstWeightRequiredIndex =
                                state.exercises.indexOfFirst { it.needsWeight }

                            if (firstWeightRequiredIndex >= 0) {
                                // 중량 설정이 필요한 운동이 하나라도 있는 경우
                                onWeightSettingClick(firstWeightRequiredIndex)
                            } else {
                                // 모든 운동의 중량이 설정된 경우
                                onStartWorkout()
                            }
                        }
                    )
                }
            }

        }
    }
}

// 운동 통계
@Composable
private fun WorkoutStat(
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )

        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary
        )
    }
}

// 공통 버튼
@Composable
fun PrimaryWorkoutButton(
    label: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .background(
                color = Blue600,
                shape = ShapeMedium
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = White
        )
    }
}
