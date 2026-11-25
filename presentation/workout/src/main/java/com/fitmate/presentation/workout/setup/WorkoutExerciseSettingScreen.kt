package com.fitmate.presentation.workout.setup

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

import com.fitmate.presentation.designsystem.theme.*
import com.fitmate.presentation.workout.today.PrimaryWorkoutButton

data class ExerciseSetting(
    val exercise: SelectableExercise,
    val sets: String = "",
    val reps: String = "",
    val weight: String = "",
    val restSeconds: Int? = null,
    val isSaved: Boolean = false,
    val durationSeconds: String = ""
)

@Composable
fun WorkoutExerciseSettingScreen(
    modifier: Modifier = Modifier,
    selectedExercises: List<SelectableExercise> = emptyList(),
    isSaving: Boolean = false,
    onBack: () -> Unit = {},
    onComplete: (List<ExerciseSetting>) -> Unit = {}
) {
    val settings = remember(selectedExercises) {
        mutableStateListOf<ExerciseSetting>().apply {
            addAll(
                selectedExercises.map { exercise ->
                    val isDuration =
                        exercise.baselineRecordType == "DURATION"

                    ExerciseSetting(
                        exercise = exercise,
                        sets = if (isDuration) "1" else "",
                        restSeconds = if (isDuration) 0 else null
                    )
                }
            )
        }
    }

    var selectedIndex by remember(selectedExercises) {
        mutableIntStateOf(0)
    }

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
    val baselineType = current.exercise.baselineRecordType

    fun updateCurrent(
        transform: (ExerciseSetting) -> ExerciseSetting
    ) {
        settings[selectedIndex] = transform(settings[selectedIndex])
    }

    // 운동 유형별 입력 항목
    val requiresReps =
        baselineType == "REPS_SETS" ||
                baselineType == "WEIGHT_REPS_SETS"

    val requiresWeight = baselineType == "WEIGHT_REPS_SETS"
    val requiresDuration = baselineType == "DURATION"

    // 초 단위 저장값을 분 단위로 표시
    val durationMinutes = current.durationSeconds
        .toIntOrNull()
        ?.div(60)
        ?.toString()
        .orEmpty()

    // 운동 유형별 입력값 검증
    val validInput = when (baselineType) {
        "DURATION" -> {
            (current.durationSeconds.toIntOrNull() ?: 0) > 0
        }

        "WEIGHT_REPS_SETS" -> {
            current.restSeconds != null &&
                    (current.sets.toIntOrNull() ?: 0) > 0 &&
                    (current.reps.toIntOrNull() ?: 0) > 0 &&
                    (current.weight.toIntOrNull() ?: -1) >= 0
        }

        "REPS_SETS" -> {
            current.restSeconds != null &&
                    (current.sets.toIntOrNull() ?: 0) > 0 &&
                    (current.reps.toIntOrNull() ?: 0) > 0
        }

        else -> false
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(White)
    ) {
        // 상단 헤더
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
                    .clickable(onClick = onBack),
                tint = TextPrimary
            )

            Text(
                text = "운동 설정",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        }

        // 선택한 운동 목록
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
                            width = 1.dp,
                            color = if (selected) {
                                Blue500
                            } else {
                                BorderDefault
                            },
                            shape = RoundedCornerShape(50)
                        )
                        .background(
                            color = if (selected) Blue100 else White,
                            shape = RoundedCornerShape(50)
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

            // 운동 유형별 세부 강도 입력
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(92.dp)
                    .border(
                        width = 1.dp,
                        color = BorderDefault,
                        shape = RoundedCornerShape(20.dp)
                    )
                    .padding(vertical = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 중량이 필요한 운동에만 표시
                if (requiresWeight) {
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
                        modifier = Modifier.weight(1f)
                    )

                    VerticalDivider(
                        modifier = Modifier.height(46.dp),
                        color = BorderDefault
                    )
                }

                // 유산소 운동은 세트 1 고정
                ExerciseNumberInput(
                    label = "세트",
                    value = if (requiresDuration) {
                        "1"
                    } else {
                        current.sets
                    },
                    onValueChange = { value ->
                        updateCurrent {
                            it.copy(
                                sets = value,
                                isSaved = false
                            )
                        }
                    },
                    modifier = Modifier.weight(1f),
                    enabled = !requiresDuration
                )

                // 횟수 또는 시간 입력 영역 구분선
                if (requiresReps || requiresDuration) {
                    VerticalDivider(
                        modifier = Modifier.height(46.dp),
                        color = BorderDefault
                    )
                }

                // 반복 횟수가 필요한 운동
                if (requiresReps) {
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
                }

                // 유산소 운동 시간
                if (requiresDuration) {
                    ExerciseNumberInput(
                        label = "시간(분)",
                        value = durationMinutes,
                        onValueChange = { value ->
                            val minutes = value.toIntOrNull()

                            if (value.isEmpty()) {
                                updateCurrent {
                                    it.copy(
                                        durationSeconds = "",
                                        isSaved = false
                                    )
                                }
                            } else if (
                                minutes != null &&
                                minutes <= Int.MAX_VALUE / 60
                            ) {
                                updateCurrent {
                                    it.copy(
                                        durationSeconds =
                                            (minutes * 60).toString(),
                                        isSaved = false
                                    )
                                }
                            }
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // 유산소 운동은 휴식 시간 선택 영역 숨김
            if (!requiresDuration) {
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
                                    color = if (selected) {
                                        Blue500
                                    } else {
                                        BorderDefault
                                    },
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .background(
                                    color = if (selected) {
                                        Blue100
                                    } else {
                                        White
                                    },
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
                                color = if (selected) {
                                    Blue600
                                } else {
                                    TextSecondary
                                }
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(28.dp))

            // 운동 설정 요약 카드
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = Blue500,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .background(
                        color = Blue100,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "운동 설정 – ${current.exercise.name}",
                    color = Blue600,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold
                )

                val detailText = when (baselineType) {
                    "WEIGHT_REPS_SETS" ->
                        "${current.weight.ifBlank { "-" }}kg · " +
                                "${current.sets.ifBlank { "-" }}세트 · " +
                                "${current.reps.ifBlank { "-" }}회"

                    "REPS_SETS" ->
                        "${current.sets.ifBlank { "-" }}세트 · " +
                                "${current.reps.ifBlank { "-" }}회"

                    "DURATION" ->
                        "1세트 · ${durationMinutes.ifBlank { "-" }}분"

                    else -> "운동 정보를 입력해 주세요."
                }

                Text(
                    text = detailText,
                    color = Blue600,
                    style = MaterialTheme.typography.bodySmall
                )

                // 유산소 운동은 휴식 시간 안내 숨김
                if (!requiresDuration) {
                    Text(
                        text = if (current.restSeconds != null) {
                            "휴식 ${current.restSeconds}초"
                        } else {
                            "휴식 시간을 선택해 주세요."
                        },
                        color = TextSecondary,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
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
                    isSaving -> "저장 중..."
                    !current.isSaved -> "저장하기"
                    settings.all { it.isSaved } -> "완료"
                    else -> "다음"
                },
                onClick = {
                    if (isSaving) return@PrimaryWorkoutButton

                    if (!current.isSaved) {
                        if (!validInput) return@PrimaryWorkoutButton

                        updateCurrent {
                            if (requiresDuration) {
                                it.copy(
                                    sets = "1",
                                    reps = "",
                                    weight = "",
                                    restSeconds = 0,
                                    isSaved = true
                                )
                            } else {
                                it.copy(isSaved = true)
                            }
                        }

                    } else if (settings.all { it.isSaved }) {
                        onComplete(settings.toList())

                    } else {
                        val nextUnsavedIndex =
                            settings.indexOfFirst { !it.isSaved }

                        if (nextUnsavedIndex != -1) {
                            selectedIndex = nextUnsavedIndex
                        }
                    }
                }
            )
        }
    }
}

// 공통 운동 숫자 입력 컴포넌트
@Composable
private fun ExerciseNumberInput(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    allowDecimal: Boolean = false,
    enabled: Boolean = true
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
            enabled = enabled,
            onValueChange = { input ->
                val filtered = if (allowDecimal) {
                    input.filter {
                        it.isDigit() || it == '.'
                    }
                } else {
                    input.filter {
                        it.isDigit()
                    }
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