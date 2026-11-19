
package com.fitmate.presentation.workout.setup

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fitmate.presentation.designsystem.R
import com.fitmate.presentation.designsystem.theme.*
import com.fitmate.presentation.workout.components.WorkoutGuideContent

data class WorkoutWeightExercise(
    val id: Int,
    val name: String,
    val description: String,
    val guide: String,
    val bodyParts: List<String>,
    val videoUrl: String = ""
)

@Composable
fun WorkoutWeightSettingScreen(
    modifier: Modifier = Modifier,
    initialExerciseId: Int,
    exercises: List<WorkoutWeightExercise>,
    isGuideLoading: Boolean = false,
    onBack: () -> Unit = {},
    onComplete: (Map<Int, Int>) -> Unit = {}
) {
    // API 응답으로 exercises가 바뀌어도 현재 운동 위치 유지
    var currentExerciseId by remember(initialExerciseId) {
        mutableIntStateOf(initialExerciseId)
    }

    // 가이드가 갱신되어도 입력한 중량 유지
    val weights = remember {
        mutableStateMapOf<Int, Int>()
    }

    if (exercises.isEmpty()) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(White),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                color = Blue600
            )
        }
        return
    }

    val currentIndex = exercises
        .indexOfFirst { it.id == currentExerciseId }
        .coerceAtLeast(0)

    val exercise = exercises[currentIndex]
    val currentWeight = weights[exercise.id] ?: 0
    val isLastExercise = currentIndex == exercises.lastIndex

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(White)
    ) {
        // 상단 헤더
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(68.dp)
                .padding(horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_workout_back),
                contentDescription = "뒤로가기",
                modifier = Modifier
                    .size(24.dp)
                    .clickable {
                        if (currentIndex > 0) {
                            currentExerciseId = exercises[currentIndex - 1].id
                        } else {
                            onBack()
                        }
                    },
                tint = Color.Unspecified
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = "운동 중량 설정",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        }

        HorizontalDivider(
            color = BorderDefault,
            thickness = 1.dp
        )

        // 본문
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
                .padding(top = 24.dp, bottom = 20.dp)
        ) {
            // 운동 이름
            Text(
                text = exercise.name,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = exercise.description,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(36.dp))

            WorkoutGuideContent(
                guide = exercise.guide,
                bodyParts = exercise.bodyParts,
                videoUrl = exercise.videoUrl,
                isLoading = isGuideLoading
            )

            Spacer(modifier = Modifier.height(32.dp))

            // 사용할 중량
            Text(
                text = "사용할 중량 (kg)",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // - 버튼
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .border(
                            width = 1.dp,
                            color = BorderDefault,
                            shape = RoundedCornerShape(15.dp)
                        )
                        .clickable {
                            weights[exercise.id] =
                                (currentWeight - 1).coerceAtLeast(0)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "−",
                        fontSize = 24.sp,
                        color = TextPrimary
                    )
                }

                // 중량 표시
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .border(
                            width = 1.dp,
                            color = BorderDefault,
                            shape = RoundedCornerShape(15.dp)
                        )
                        .background(
                            color = Color(0xFFF8F9FA),
                            shape = RoundedCornerShape(15.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (currentWeight == 0) {
                            "-"
                        } else {
                            currentWeight.toString()
                        },
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextPrimary,
                        textAlign = TextAlign.Center
                    )
                }

                // + 버튼
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .border(
                            width = 1.dp,
                            color = BorderDefault,
                            shape = RoundedCornerShape(15.dp)
                        )
                        .clickable {
                            weights[exercise.id] = currentWeight + 1
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "+",
                        fontSize = 24.sp,
                        color = TextPrimary
                    )
                }
            }
        }

        // 하단 버튼
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 24.dp,
                    vertical = 16.dp
                )
                .height(50.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(
                    if (currentWeight > 0) Blue600
                    else Blue600.copy(alpha = 0.5f)
                )
                .clickable(enabled = currentWeight > 0) {
                    if (isLastExercise) {
                        onComplete(weights.toMap())
                    } else {
                        currentExerciseId = exercises[currentIndex + 1].id
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (isLastExercise) {
                    "시작하기"
                } else {
                    "다음"
                },
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = White
            )
        }
    }
}
