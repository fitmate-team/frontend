package com.fitmate.presentation.workout

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.fitmate.presentation.designsystem.theme.*

data class ExerciseSetting(
    val exercise: SelectableExercise,
    val sets: String = "",
    val reps: String = "",
    val weight: String = "",
    val restSeconds: Int? = null,
    val isSaved: Boolean = false
)

@Composable
fun WorkoutExerciseSettingScreen(
    modifier: Modifier = Modifier,
    selectedExercises: List<SelectableExercise> = listOf(
        SelectableExercise(1, "벤치프레스", "가슴", "프리웨이트"),
        SelectableExercise(2, "인클라인 덤벨프레스", "가슴", "프리웨이트"),
        SelectableExercise(3, "체스트프레스", "가슴", "머신/케이블")
    ),
    onBack: () -> Unit = {},
    onComplete: (List<ExerciseSetting>) -> Unit = {}
) {
    val settings = remember(selectedExercises) {
        mutableStateListOf<ExerciseSetting>().apply {
            addAll(
                selectedExercises.map { exercise ->
                    ExerciseSetting(exercise = exercise)
                }
            )
        }
    }

    var selectedIndex by remember { mutableIntStateOf(0) }

    if (settings.isEmpty()) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("선택한 운동이 없습니다.")
        }
        return
    }

    val current = settings[selectedIndex]

    fun updateCurrent(
        transform: (ExerciseSetting) -> ExerciseSetting
    ) {
        settings[selectedIndex] = transform(settings[selectedIndex])
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(White)
    ) {

        // 상단 타이틀
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(76.dp)
                .padding(horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                contentDescription = "뒤로가기",
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onBack() },
                tint = TextPrimary
            )

            Text(
                text = "운동 설정",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        }

        // 선택한 운동 칩
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            itemsIndexed(settings) { index, setting ->

                val selected = selectedIndex == index

                Box(
                    modifier = Modifier
                        .border(
                            1.dp,
                            if (selected) Blue500 else BorderDefault,
                            RoundedCornerShape(50)
                        )
                        .background(
                            if (selected) Blue100 else White,
                            RoundedCornerShape(50)
                        )
                        .clickable {
                            selectedIndex = index
                        }
                        .padding(
                            horizontal = 14.dp,
                            vertical = 10.dp
                        )
                ) {
                    Text(
                        text = setting.exercise.name,
                        color = if (selected) Blue600 else TextPrimary,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }

        HorizontalDivider(
            modifier = Modifier.padding(top = 16.dp),
            color = BorderDefault
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(
                    horizontal = 24.dp,
                    vertical = 24.dp
                )
        ) {

            Text(
                text = "운동 세부 강도 설정",
                color = TextPrimary,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(Modifier.height(16.dp))

            // 세트 / 횟수 / 중량 입력
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(92.dp)
                    .border(
                        1.dp,
                        BorderDefault,
                        RoundedCornerShape(20.dp)
                    )
                    .padding(vertical = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ExerciseNumberInput(
                    label = "세트",
                    value = current.sets,
                    onValueChange = { value ->
                        updateCurrent {
                            it.copy(
                                sets = value,
                                isSaved = false
                            )
                        }
                    },
                    modifier = Modifier.weight(1f)
                )

                VerticalDivider(
                    modifier = Modifier.height(46.dp),
                    color = BorderDefault
                )

                ExerciseNumberInput(
                    label = "횟수",
                    value = current.reps,
                    onValueChange = { value ->
                        updateCurrent {
                            it.copy(
                                reps = value,
                                isSaved = false
                            )
                        }
                    },
                    modifier = Modifier.weight(1f)
                )

                VerticalDivider(
                    modifier = Modifier.height(46.dp),
                    color = BorderDefault
                )

                ExerciseNumberInput(
                    label = "중량(kg)",
                    value = current.weight,
                    onValueChange = { value ->
                        updateCurrent {
                            it.copy(
                                weight = value,
                                isSaved = false
                            )
                        }
                    },
                    modifier = Modifier.weight(1f),
                    allowDecimal = true
                )
            }

            Spacer(Modifier.height(32.dp))

            Text(
                text = "휴식 시간",
                color = TextPrimary,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(Modifier.height(16.dp))

            val restOptions = listOf(
                30 to "30초",
                60 to "60초",
                90 to "90초",
                120 to "2분",
                180 to "3분"
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                restOptions.forEach { (seconds, label) ->

                    val selected = current.restSeconds == seconds

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(36.dp)
                            .border(
                                width = 1.dp,
                                color = if (selected) Blue500 else BorderDefault,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .background(
                                color = if (selected) Blue100 else White,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .clickable {
                                updateCurrent {
                                    it.copy(
                                        restSeconds = seconds,
                                        isSaved = false
                                    )
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = label,
                            style = MaterialTheme.typography.bodySmall,
                            color = if (selected) Blue600 else TextSecondary
                        )
                    }
                }
            }

            Spacer(Modifier.height(28.dp))

            // AI 추천 정보
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        1.dp,
                        Blue500,
                        RoundedCornerShape(10.dp)
                    )
                    .background(
                        Blue100,
                        RoundedCornerShape(10.dp)
                    )
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "AI 추천 – ${current.exercise.name}",
                    color = Blue600,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = "4세트 · 8회 · 42.5kg · 휴식 90초",
                    color = Blue600,
                    style = MaterialTheme.typography.bodySmall
                )

                Text(
                    text = "최근 4회 평균 성공률 94%를 기준으로 추천해요.",
                    color = TextSecondary,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        // 하단 버튼
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 24.dp,
                    end = 24.dp,
                    top = 14.dp,
                    bottom = 24.dp
                )
        ) {
            PrimaryWorkoutButton(
                label = when {
                    !current.isSaved -> "저장하기"
                    selectedIndex == settings.lastIndex -> "완료"
                    else -> "다음"
                },
                onClick = {
                    if (!current.isSaved) {
                        updateCurrent {
                            it.copy(isSaved = true)
                        }
                    } else if (selectedIndex < settings.lastIndex) {
                        selectedIndex++
                    } else {
                        onComplete(settings.toList())
                    }
                }
            )
        }
    }
}

@Composable
private fun ExerciseNumberInput(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    allowDecimal: Boolean = false
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary
        )

        BasicTextField(
            value = value,
            onValueChange = { input ->
                val filtered = if (allowDecimal) {
                    input.filter { it.isDigit() || it == '.' }
                } else {
                    input.filter { it.isDigit() }
                }

                onValueChange(filtered)
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = if (allowDecimal) {
                    KeyboardType.Decimal
                } else {
                    KeyboardType.Number
                }
            ),
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                color = TextPrimary,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.SemiBold
            ),
            decorationBox = { innerTextField ->
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    if (value.isEmpty()) {
                        Text(
                            text = "-",
                            color = TextPrimary,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    innerTextField()
                }
            }
        )
    }
}