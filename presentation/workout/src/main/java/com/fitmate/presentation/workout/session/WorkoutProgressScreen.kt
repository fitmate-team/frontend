package com.fitmate.presentation.workout.session

import android.os.SystemClock
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.*
import androidx.compose.ui.window.Dialog
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fitmate.presentation.designsystem.R
import com.fitmate.presentation.designsystem.theme.FitMateFontFamily
import com.fitmate.presentation.designsystem.theme.FitMateNumberFontFamily
import com.fitmate.presentation.designsystem.theme.Blue600
import com.fitmate.presentation.designsystem.theme.Gray700
import com.fitmate.presentation.designsystem.theme.Gray300
import com.fitmate.presentation.designsystem.theme.Gray200
import com.fitmate.presentation.designsystem.theme.Gray50
import com.fitmate.presentation.workout.result.NextWorkoutAdjustmentItem
import com.fitmate.presentation.workout.result.NextWorkoutAdjustmentScreen
import com.fitmate.presentation.workout.result.WorkoutAnalysisItem
import com.fitmate.presentation.workout.result.WorkoutAnalysisScreen
import com.fitmate.presentation.workout.record.WorkoutBodyRecordScreen
import com.fitmate.presentation.workout.components.WorkoutGuideContent
import com.fitmate.domain.model.WorkoutAdjustmentRecommendation
import kotlinx.coroutines.delay

private val ProgressBlue = Blue600
private val ProgressDark = Gray700
private val ProgressMuted = Gray300
private val ProgressBorder = Gray200
private val ProgressSurface = Gray50

/** 운동 진행 화면에서 사용하는 운동 기준 유형. */
enum class ExerciseBaselineType {
    WEIGHT_REPS_SETS,
    REPS_SETS,
    DURATION
}

data class WorkoutProgressExercise(
    val id: Int,
    val name: String,
    val weightKg: Int = 0,
    val reps: Int = 8,
    val sets: Int = 4,
    val restSeconds: Int = 30,
    val durationSeconds: Int? = null,
    val guide: String = "",
    val bodyParts: List<String> = emptyList(),
    val videoUrl: String = "",
    val baselineType: ExerciseBaselineType =
        ExerciseBaselineType.WEIGHT_REPS_SETS,
    val exerciseCode: String = "",
    val exerciseId: Long? = null
)


data class WorkoutSessionResult(
    val totalSeconds: Long,
    val exerciseSeconds: Long,
    val restSeconds: Long,
    val completedSets: Map<Int, Int>,
    val exerciseElapsed: Map<Int, Long>,
    val difficulty: WorkoutDifficulty
)


private enum class WorkoutPhase { ACTIVE, REST, FINISHED }
private enum class WorkoutDialog { NEXT, QUIT, FINISH }

@Composable
fun WorkoutProgressScreen(
    exercises: List<WorkoutProgressExercise> = listOf(
        // 기존 운동 목록 그대로 유지
    ),
    onBack: () -> Unit = {},
    onFinish: (WorkoutSessionResult) -> Unit = {},
    isWorkoutSaved: Boolean = false,
    onApplyAdjustments: (List<NextWorkoutAdjustmentItem>) -> Unit = {},

    // 새로 추가
    recommendation: WorkoutAdjustmentRecommendation? = null,
    isRecommendationLoading: Boolean = false,
    recommendationErrorMessage: String? = null,
    onLoadRecommendation: () -> Unit = {},
    isFinishing: Boolean = false,
    finishErrorMessage: String? = null,

    modifier: Modifier = Modifier
) {
    if (exercises.isEmpty()) return

    var currentIndex by remember { mutableIntStateOf(0) }
    val completedSets = remember { mutableStateMapOf<Int, Int>() }
    var phase by remember { mutableStateOf(WorkoutPhase.ACTIVE) }
    var paused by remember { mutableStateOf(false) }
    var totalSeconds by remember { mutableLongStateOf(0L) }
    var exerciseSeconds by remember { mutableLongStateOf(0L) }
    var restRemaining by remember { mutableIntStateOf(0) }
    var pendingIndex by remember { mutableIntStateOf(0) }
    var dialog by remember { mutableStateOf<WorkoutDialog?>(null) }
    var showBodyRecord by remember { mutableStateOf(false) }
    var showDifficulty by remember { mutableStateOf(false) }
    var showAnalysis by remember { mutableStateOf(false) }
    var showAdjustment by remember { mutableStateOf(false) }
    var pendingFinishResult by remember {
        mutableStateOf<WorkoutSessionResult?>(null)
    }
    var selectedDifficulty by remember { mutableStateOf<WorkoutDifficulty?>(null) }
    val exerciseElapsed = remember { mutableStateMapOf<Int, Long>() }
    var accumulatedRestSeconds by remember { mutableLongStateOf(0L) }
    var showGuide by remember { mutableStateOf(false) }

    val exercise = exercises[currentIndex]
    val completed = completedSets[exercise.id] ?: 0
    // 기존 durationSeconds 기반 유산소 데이터도 계속 지원.
    val isCardio = exercise.durationSeconds != null ||
            exercise.baselineType == ExerciseBaselineType.DURATION
    val isRepsSets = exercise.baselineType == ExerciseBaselineType.REPS_SETS
    val isLast = currentIndex == exercises.lastIndex
    val allSetsDone = !isCardio && completed >= exercise.sets

    fun switchExercise(index: Int) {
        currentIndex = index
        phase = WorkoutPhase.ACTIVE
        paused = false
        exerciseSeconds = 0L
        restRemaining = 0
    }

    // 운동 화면 진입 즉시 시작. 휴식 시간도 전체 운동 시간에 포함.
    LaunchedEffect(phase, paused, currentIndex, showGuide) {
        var last = SystemClock.elapsedRealtime()
        var carryMs = 0L
        while (phase != WorkoutPhase.FINISHED) {
            delay(100L)
            val now = SystemClock.elapsedRealtime()
            val elapsed = now - last
            last = now
            if (!paused) {
                carryMs += elapsed
                val whole = carryMs / 1000L
                if (whole > 0) {
                    carryMs %= 1000L
                    totalSeconds += whole
                    if (phase == WorkoutPhase.ACTIVE) {
                        exerciseSeconds += whole
                        exerciseElapsed[exercise.id] = (exerciseElapsed[exercise.id] ?: 0L) + whole
                    } else {
                        accumulatedRestSeconds += whole
                        restRemaining = (restRemaining - whole.toInt()).coerceAtLeast(0)
                        // 휴식 타이머가 0이 되어도 다음 세트는 자동 시작하지 않음.
                        // 사용자가 '세트 시작'을 눌렀을 때만 ACTIVE로 전환.

                    }
                }
            }
        }
    }

    if (showAdjustment) {

        // AI 추천 화면 진입 시 실제 서버 데이터 조회
        LaunchedEffect(isWorkoutSaved) {
            if (
                isWorkoutSaved &&
                recommendation == null &&
                !isRecommendationLoading &&
                recommendationErrorMessage == null
            ) {
                onLoadRecommendation()
            }
        }


        val adjustmentItems = remember(recommendation) {
            recommendation?.exercises?.mapIndexed { index, item ->

                fun formatValues(
                    weightKg: Int?,
                    reps: Int?,
                    sets: Int?,
                    durationSeconds: Int?
                ): String {
                    return if (durationSeconds != null) {
                        "${durationSeconds / 60}분 ${durationSeconds % 60}초"
                    } else {
                        listOfNotNull(
                            weightKg?.let { "${it}kg" },
                            reps?.let { "${it}회" },
                            sets?.let { "${it}세트" }
                        ).joinToString(" × ")
                    }
                }

                NextWorkoutAdjustmentItem(
                    id = index + 1,
                    name = item.exerciseName,
                    current = formatValues(
                        item.current.weightKg,
                        item.current.reps,
                        item.current.setCount,
                        item.current.durationSeconds
                    ),
                    suggested = formatValues(
                        item.recommended.weightKg,
                        item.recommended.reps,
                        item.recommended.setCount,
                        item.recommended.durationSeconds
                    ),
                    category = when {
                        item.changes.any {
                            it.direction.equals("INCREASE", ignoreCase = true)
                        } -> "증가"

                        item.changes.any {
                            it.direction.equals("DECREASE", ignoreCase = true)
                        } -> "감소"

                        else -> "유지"
                    },
                    reason = item.reason
                )
            }.orEmpty()
        }

        when {
            !isWorkoutSaved && finishErrorMessage != null -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = finishErrorMessage,
                        fontFamily = FitMateFontFamily
                    )

                    Spacer(Modifier.height(16.dp))

                    Button(
                        onClick = {
                            pendingFinishResult?.let(onFinish)
                        },
                        enabled = !isFinishing && pendingFinishResult != null
                    ) {
                        Text("운동 기록 저장 다시 시도")
                    }
                }
            }

            !isWorkoutSaved -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            isRecommendationLoading || recommendation == null &&
                    recommendationErrorMessage == null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            recommendationErrorMessage != null -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = recommendationErrorMessage,
                        fontFamily = FitMateFontFamily
                    )

                    Spacer(Modifier.height(16.dp))

                    Button(onClick = onLoadRecommendation) {
                        Text("다시 시도")
                    }
                }
            }

            else -> {
                NextWorkoutAdjustmentScreen(
                    items = adjustmentItems,
                    onBack = {
                        showAdjustment = false
                        showAnalysis = true
                    },
                    onApplySelected = onApplyAdjustments
                )
            }
        }

        return
    }


    if (showAnalysis) {
        WorkoutAnalysisScreen(
            items = exercises.map { item ->
                WorkoutAnalysisItem(
                    name = item.name,
                    weightKg = item.weightKg,
                    reps = item.reps,
                    totalSets = item.sets,
                    completedSets = completedSets[item.id] ?: 0,
                    durationSeconds = item.durationSeconds,
                    completedDurationSeconds = (exerciseElapsed[item.id] ?: 0L).toInt(),
                    difficulty = selectedDifficulty?.let { difficulty ->
                        when (difficulty) {
                            WorkoutDifficulty.VERY_EASY -> "매우 쉬움"
                            WorkoutDifficulty.EASY -> "쉬움"
                            WorkoutDifficulty.MODERATE -> "보통"
                            WorkoutDifficulty.HARD -> "어려움"
                            WorkoutDifficulty.TOO_HARD -> "매우 어려움"
                        }
                    }
                )
            },
            totalSeconds = totalSeconds,
            restSeconds = accumulatedRestSeconds,
            onBack = { showAnalysis = false; showDifficulty = true },


            onNext = {
                val difficulty = selectedDifficulty

                if (difficulty != null) {
                    if (isWorkoutSaved) {
                        showAnalysis = false
                        showAdjustment = true
                    } else if (!isFinishing) {
                        val result = WorkoutSessionResult(
                            totalSeconds = totalSeconds,
                            exerciseSeconds = (
                                    totalSeconds - accumulatedRestSeconds
                                    ).coerceAtLeast(0L),
                            restSeconds = accumulatedRestSeconds,
                            completedSets = completedSets.toMap(),
                            exerciseElapsed = exerciseElapsed.toMap(),
                            difficulty = difficulty
                        )

                        pendingFinishResult = result
                        onFinish(result)

                        showAnalysis = false
                        showAdjustment = true
                    }
                }
            }


        )
        return
    }

    if (showDifficulty) {
        WorkoutDifficultyScreen(
            onBack = { showDifficulty = false; showBodyRecord = true },
            onAnalyze = { difficulty ->
                selectedDifficulty = difficulty
                showDifficulty = false
                showAnalysis = true
            }
        )
        return
    }

    if (showBodyRecord) {
        WorkoutBodyRecordScreen(
            onBack = { showBodyRecord = false },
            onNext = { _ -> showBodyRecord = false; showDifficulty = true },
            onSkip = { showBodyRecord = false; showDifficulty = true }
        )
        return
    }

    if (showGuide) {
        WorkoutProgressGuideScreen(exercise = exercise, onBack = { showGuide = false })
        return
    }

    Column(modifier = modifier.fillMaxSize().background(Color.White)) {
        Row(
            modifier = Modifier.fillMaxWidth().height(64.dp).padding(start = 12.dp, end = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { dialog = WorkoutDialog.QUIT },
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_workout_back),
                    contentDescription = "뒤로가기",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(Modifier.width(8.dp))
            Text("운동 진행", fontFamily = FitMateFontFamily, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = ProgressDark)
        }
        // 운동 종목 탭: 선택한 탭의 하단 파란색 선은 구분선과 밀착
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .height(56.dp)
                .padding(start = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            exercises.forEachIndexed { index, item ->
                val isSelected = index == currentIndex
                Box(
                    modifier = Modifier
                        .width(if (item.name.length >= 8) 150.dp else 110.dp)
                        .height(56.dp)
                        .drawBehind {
                            if (isSelected) {
                                val lineWidth = 3.dp.toPx()
                                val inset = 8.dp.toPx()
                                drawLine(
                                    color = ProgressBlue,
                                    start = Offset(inset, size.height - lineWidth / 2),
                                    end = Offset(size.width - inset, size.height - lineWidth / 2),
                                    strokeWidth = lineWidth
                                )
                            }
                        }
                        .clickable {
                            if (!isSelected) {
                                pendingIndex = index
                                dialog = WorkoutDialog.NEXT
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = item.name,
                        color = if (isSelected) ProgressBlue else ProgressDark,
                        fontFamily = FitMateFontFamily,
                        fontSize = 14.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                if (index < exercises.lastIndex) {
                    Box(
                        Modifier.width(1.dp).height(18.dp).background(ProgressBorder)
                    )
                }
            }
        }
        HorizontalDivider(color = ProgressBorder, thickness = 1.dp)

        Column(
            modifier = Modifier.weight(1f).verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 22.dp)
        ) {
            Text(exercise.name, fontFamily = FitMateFontFamily, fontSize = 26.sp, fontWeight = FontWeight.Bold, color = ProgressDark)
            Spacer(Modifier.height(6.dp))
            Text(
                when {
                    isCardio -> "유산소 운동 진행 중"
                    allSetsDone -> "${exercise.sets}세트 완료"
                    phase == WorkoutPhase.REST -> "${completed}세트 완료 · 휴식 중"
                    else -> "${(completed + 1).coerceAtMost(exercise.sets)}세트 진행 중"
                },
                fontFamily = FitMateFontFamily, color = ProgressMuted, fontSize = 14.sp
            )
            Spacer(Modifier.height(24.dp))

            if (isCardio) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                        .border(1.dp, ProgressBorder, RoundedCornerShape(14.dp))
                        .padding(vertical = 28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("유산소 운동 시간", fontFamily = FitMateFontFamily, color = ProgressMuted, fontSize = 14.sp)
                    Spacer(Modifier.height(20.dp))
                    Box(
                        modifier = Modifier.size(230.dp).border(4.dp, ProgressBlue, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(formatTime(exerciseSeconds), fontFamily = FitMateNumberFontFamily, color = ProgressBlue, fontSize = 32.sp)
                            Spacer(Modifier.height(12.dp))
                            ProgressPauseButton(paused = paused, onClick = { paused = !paused })
                        }
                    }
                    Spacer(Modifier.height(20.dp))
                    Text("전체 운동 시간  ${formatTime(totalSeconds)}", fontFamily = FitMateFontFamily, color = ProgressMuted, fontSize = 14.sp)
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxWidth()
                        .border(1.dp, ProgressBorder, RoundedCornerShape(14.dp))
                        .padding(horizontal = 49.dp, vertical = 42.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // REPS_SETS 유형은 횟수만, 기존 중량 운동은 중량 × 횟수를 표시.
                    if (isRepsSets) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.Bottom,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "${exercise.reps}",
                                color = ProgressBlue,
                                fontFamily = FitMateNumberFontFamily,
                                fontSize = 66.sp,
                                lineHeight = 72.sp
                            )
                            Text(
                                text = "회",
                                color = ProgressDark,
                                fontFamily = FitMateFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                modifier = Modifier.padding(start = 4.dp, bottom = 10.dp)
                            )
                        }
                    } else {
                        // 운동 중량 × 반복 횟수
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.Bottom,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            // 40kg
                            Row(
                                verticalAlignment = Alignment.Bottom
                            ) {
                                Text(
                                    text = "${exercise.weightKg}",
                                    color = ProgressBlue,
                                    fontFamily = FitMateNumberFontFamily,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 66.sp,
                                    lineHeight = 72.sp
                                )

                                Text(
                                    text = "kg",
                                    color = ProgressDark,
                                    fontFamily = FitMateFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp,
                                    modifier = Modifier.padding(
                                        start = 2.dp,
                                        bottom = 10.dp
                                    )
                                )
                            }

                            // ×
                            Text(
                                text = "×",
                                color = ProgressMuted,
                                fontFamily = FitMateFontFamily,
                                fontWeight = FontWeight.Normal,
                                fontSize = 51.sp,
                                lineHeight = 58.sp,
                                modifier = Modifier.padding(bottom = 3.dp)
                            )

                            // 8회
                            Row(
                                verticalAlignment = Alignment.Bottom
                            ) {
                                Text(
                                    text = "${exercise.reps}",
                                    color = ProgressBlue,
                                    fontFamily = FitMateNumberFontFamily,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 66.sp,
                                    lineHeight = 72.sp
                                )

                                Text(
                                    text = "회",
                                    color = ProgressDark,
                                    fontFamily = FitMateFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp,
                                    modifier = Modifier.padding(
                                        start = 2.dp,
                                        bottom = 10.dp
                                    )
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(49.dp))

                    // 세트 진행도: 첫 원은 왼쪽 끝, 마지막 원은 오른쪽 끝
                    Box(modifier = Modifier.fillMaxWidth()) {
                        if (exercise.sets > 1) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 14.dp, end = 14.dp)
                                    .offset(y = 13.5.dp)
                                    .height(1.dp)
                                    .background(ProgressMuted)
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            repeat(exercise.sets.coerceAtLeast(1)) { index ->
                                val done = index < completed
                                val current = index == completed &&
                                        !allSetsDone && phase == WorkoutPhase.ACTIVE

                                Column(
                                    modifier = Modifier.width(28.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(28.dp)
                                            .background(
                                                when {
                                                    current -> ProgressBlue
                                                    done -> ProgressMuted
                                                    else -> Color.White
                                                },
                                                CircleShape
                                            )
                                            .border(
                                                1.5.dp,
                                                if (current) ProgressBlue else ProgressMuted,
                                                CircleShape
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        if (done) {
                                            Icon(
                                                imageVector = Icons.Rounded.Check,
                                                contentDescription = "완료된 세트",
                                                tint = Color.White,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        } else if (current) {
                                            Text(
                                                text = "${index + 1}",
                                                color = Color.White,
                                                fontFamily = FitMateNumberFontFamily,
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }

                                    Spacer(Modifier.height(7.dp))

                                    Text(
                                        text = when {
                                            current -> "진행"
                                            else -> "${index + 1}"
                                        },
                                        modifier = Modifier.requiredWidth(48.dp),
                                        fontFamily = FitMateFontFamily,
                                        fontSize = 12.sp,
                                        color = if (current) ProgressBlue else ProgressMuted,
                                        textAlign = TextAlign.Center,
                                        maxLines = 1
                                    )
                                }
                            }
                        }
                    }
                }
                Spacer(Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .border(1.dp, Color(0xFFD7E9FF), RoundedCornerShape(12.dp))
                        .padding(horizontal = 18.dp, vertical = 17.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(if (phase == WorkoutPhase.REST) "휴게 시간" else "운동 시간", fontFamily = FitMateFontFamily,
                            color = ProgressMuted, fontSize = 14.sp)
                        Spacer(Modifier.height(4.dp))
                        Text(
                            if (phase == WorkoutPhase.REST) formatTime(restRemaining.toLong()) else formatTime(totalSeconds),
                            fontFamily = FitMateNumberFontFamily, color = ProgressBlue, fontSize = 32.sp, lineHeight = 38.sp
                        )
                    }
                    ProgressPauseButton(paused = paused, onClick = { paused = !paused })
                }
            }

            Spacer(Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth()
                    .border(1.dp, ProgressBorder, RoundedCornerShape(12.dp))
                    .clickable { showGuide = true }
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 기존 drawable 재사용: ic_workout_guide 신규 리소스 불필요
                Icon(painterResource(R.drawable.ic_workout_preview), contentDescription = null,
                    tint = Color.Unspecified, modifier = Modifier.size(22.dp))
                Spacer(Modifier.width(10.dp))
                Text("가이드 영상 확인하기", fontFamily = FitMateFontFamily, fontSize = 14.sp,
                    fontWeight = FontWeight.Bold, color = ProgressDark)
                Spacer(Modifier.width(4.dp))
                Icon(painterResource(R.drawable.ic_workout_chevron_right), contentDescription = null,
                    tint = Color.Unspecified, modifier = Modifier.size(16.dp))
            }
        }

        Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 12.dp)) {
            Button(
                onClick = {
                    when {
                        isCardio || allSetsDone -> {
                            if (isLast) dialog = WorkoutDialog.FINISH else switchExercise(currentIndex + 1)
                        }
                        phase == WorkoutPhase.REST -> {
                            phase = WorkoutPhase.ACTIVE
                            exerciseSeconds = 0L
                            paused = false
                        }
                        else -> {
                            completedSets[exercise.id] = (completed + 1).coerceAtMost(exercise.sets)
                            if (completed + 1 < exercise.sets) {
                                phase = WorkoutPhase.REST
                                restRemaining = exercise.restSeconds
                                paused = false
                            } else {
                                phase = WorkoutPhase.ACTIVE
                                exerciseSeconds = 0L
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(54.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ProgressBlue)
            ) {
                Text(
                    when {
                        isCardio || allSetsDone -> if (isLast) "운동 완료" else "다음 운동"
                        phase == WorkoutPhase.REST -> "세트 시작"
                        else -> "세트 완료"
                    },
                    fontFamily = FitMateNumberFontFamily, fontWeight = FontWeight.Normal, fontSize = 16.sp
                )
            }
            Spacer(Modifier.height(10.dp))
            OutlinedButton(
                onClick = { dialog = WorkoutDialog.QUIT },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, ProgressBorder),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = ProgressMuted)
            ) {
                Text("운동 그만하기", fontFamily = FitMateNumberFontFamily, fontSize = 15.sp, fontWeight = FontWeight.Normal)
            }
        }
    }

    dialog?.let { kind ->
        val title = when (kind) {
            WorkoutDialog.NEXT -> "다음 운동으로 넘어갈까요?"
            WorkoutDialog.QUIT -> "운동을 종료할까요?"
            WorkoutDialog.FINISH -> "운동을 완료할까요?"
        }
        val message = when (kind) {
            WorkoutDialog.NEXT -> "아직 완료하지 않은 세트가 남아있어요.\n지금 넘어가면 남은 세트는 미완료로 기록돼요."
            WorkoutDialog.QUIT -> "아직 완료하지 않은 운동이 남아있어요.\n지금 종료하면 남은 운동은 미완료로 기록돼요."
            WorkoutDialog.FINISH -> "운동 기록을 저장하고 종료할까요?"
        }
        Dialog(onDismissRequest = { dialog = null }) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                color = Color.White,
                shadowElevation = 8.dp
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = title,
                        fontFamily = FitMateFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = ProgressDark
                    )
                    Spacer(Modifier.height(14.dp))
                    Text(
                        text = message,
                        fontFamily = FitMateFontFamily,
                        fontSize = 13.sp,
                        lineHeight = 19.sp,
                        color = ProgressMuted
                    )
                    Spacer(Modifier.height(22.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = { dialog = null },
                            modifier = Modifier.weight(1f).height(40.dp),
                            shape = RoundedCornerShape(9.dp),
                            border = BorderStroke(1.dp, ProgressBlue),
                            contentPadding = PaddingValues(0.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = ProgressBlue)
                        ) {
                            Text("취소", fontFamily = FitMateFontFamily, fontSize = 13.sp,
                                fontWeight = FontWeight.Bold)
                        }
                        Button(
                            onClick = {
                                dialog = null
                                when (kind) {
                                    WorkoutDialog.NEXT -> switchExercise(pendingIndex)
                                    WorkoutDialog.QUIT, WorkoutDialog.FINISH -> {
                                        phase = WorkoutPhase.FINISHED
                                        showBodyRecord = true
                                    }
                                }
                            },
                            modifier = Modifier.weight(1f).height(40.dp),
                            shape = RoundedCornerShape(9.dp),
                            contentPadding = PaddingValues(0.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = ProgressBlue)
                        ) {
                            Text("확인", fontFamily = FitMateFontFamily, fontSize = 13.sp,
                                fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ProgressPauseButton(paused: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier.size(46.dp)
            .border(1.5.dp, ProgressBlue, CircleShape)
            .clip(CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = if (paused) Icons.Rounded.PlayArrow else Icons.Rounded.Pause,
            contentDescription = if (paused) "타이머 재생" else "타이머 일시정지",
            tint = ProgressBlue,
            modifier = Modifier.size(23.dp)
        )
    }
}

/** 중량 설정 화면과 동일한 가이드 콘텐츠를 재사용. */
@Composable
private fun WorkoutProgressGuideScreen(
    exercise: WorkoutProgressExercise,
    onBack: () -> Unit
) {
    Column(Modifier.fillMaxSize().background(Color.White)) {
        Row(
            modifier = Modifier.fillMaxWidth().height(68.dp).padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack, modifier = Modifier.size(40.dp)) {
                Icon(
                    painter = painterResource(R.drawable.ic_workout_back),
                    contentDescription = "뒤로가기",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(Modifier.width(8.dp))
            Text("운동 가이드", fontFamily = FitMateFontFamily, fontSize = 20.sp,
                fontWeight = FontWeight.Bold, color = ProgressDark)
        }
        HorizontalDivider(color = ProgressBorder)
        Column(
            modifier = Modifier.weight(1f).verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 24.dp)
        ) {
            Text(exercise.name, fontFamily = FitMateFontFamily, fontSize = 24.sp,
                fontWeight = FontWeight.Bold, color = ProgressDark)
            Spacer(Modifier.height(8.dp))
            Text("올바른 자세를 확인하고 운동을 진행해주세요.",
                fontFamily = FitMateFontFamily, fontSize = 14.sp, color = ProgressMuted)
            Spacer(Modifier.height(36.dp))
            WorkoutGuideContent(
                guide = exercise.guide,
                bodyParts = exercise.bodyParts,
                videoUrl = exercise.videoUrl
            )
        }
        Button(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 16.dp).height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = ProgressBlue),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("다시 운동하기", fontFamily = FitMateFontFamily, fontWeight = FontWeight.Bold)
        }
    }
}

private fun formatTime(seconds: Long): String {
    val safe = seconds.coerceAtLeast(0L)
    return "%02d:%02d:%02d".format(safe / 3600, (safe / 60) % 60, safe % 60)
}




