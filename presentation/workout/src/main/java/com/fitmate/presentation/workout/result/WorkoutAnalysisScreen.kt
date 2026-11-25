package com.fitmate.presentation.workout.result

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fitmate.domain.model.WorkoutRecordDetail
import com.fitmate.presentation.designsystem.R
import com.fitmate.presentation.designsystem.component.FitMateSkeleton
import com.fitmate.presentation.designsystem.theme.*
import kotlin.math.roundToInt

/**
 * 운동별 분석 UI 모델
 */
data class WorkoutAnalysisItem(
    val name: String,
    val weightKg: Int = 0,
    val reps: Int = 0,
    val totalSets: Int = 0,
    val completedSets: Int = 0,
    val durationSeconds: Int? = null,
    val completedDurationSeconds: Int = 0,
    val difficulty: String? = null,
    val baselineRecordType: String? = null,
    val actualWeightKg: Double? = null
)

/**
 * 서버 운동 분석 결과를 UI 모델로 변환
 */
fun WorkoutRecordDetail.toAnalysisItems(): List<WorkoutAnalysisItem> {
    return exercises.map { exercise ->
        WorkoutAnalysisItem(
            name = exercise.exerciseName,
            weightKg = exercise.weightKg?.roundToInt() ?: 0,
            reps = exercise.reps ?: 0,
            totalSets = exercise.setCount ?: 0,
            completedSets = exercise.completedSetCount ?: 0,
            durationSeconds = if (
                exercise.baselineRecordType == "DURATION"
            ) {
                exercise.durationSeconds ?: 0
            } else {
                null
            },
            completedDurationSeconds =
                exercise.completedDurationSeconds ?: 0,
            difficulty = exercise.calculatedDifficulty,
            baselineRecordType = exercise.baselineRecordType,
            actualWeightKg = exercise.weightKg
        )
    }
}

@Composable
fun WorkoutAnalysisScreen(
    items: List<WorkoutAnalysisItem>,
    totalSeconds: Long,
    restSeconds: Long? = null,
    minHeartRate: Int? = null,
    maxHeartRate: Int? = null,
    hasNextRoutine: Boolean = true,
    onBack: () -> Unit,
    onNext: () -> Unit,
    modifier: Modifier = Modifier,
    completionRate: Double? = null,
    isLoading: Boolean = false,
    errorMessage: String? = null,
    onRetry: () -> Unit = {}
) {

    // 기존 호출부 및 미리보기 호환용 수행률 계산
    val plannedUnits = items.sumOf {
        if (it.durationSeconds != null) {
            it.durationSeconds.coerceAtLeast(0)
        } else {
            it.totalSets.coerceAtLeast(0)
        }
    }

    val completedUnits = items.sumOf {
        if (it.durationSeconds != null) {
            it.completedDurationSeconds.coerceIn(
                0,
                it.durationSeconds.coerceAtLeast(0)
            )
        } else {
            it.completedSets.coerceIn(
                0,
                it.totalSets.coerceAtLeast(0)
            )
        }
    }

    // 서버 수행률을 우선 사용
    val percent = completionRate
        ?.roundToInt()
        ?.coerceIn(0, 100)
        ?: if (plannedUnits == 0) {
            0
        } else {
            ((completedUnits.toFloat() / plannedUnits) * 100)
                .roundToInt()
                .coerceIn(0, 100)
        }

    val watchConnected =
        minHeartRate != null && maxHeartRate != null

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
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
                painter = painterResource(
                    R.drawable.ic_workout_back
                ),
                contentDescription = "뒤로가기",
                tint = Color.Unspecified,
                modifier = Modifier
                    .size(24.dp)
                    .clickable(onClick = onBack)
            )

            Spacer(Modifier.width(12.dp))

            Text(
                text = "운동 분석",
                fontFamily = FitMateFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Gray700
            )
        }

        HorizontalDivider(
            thickness = 1.dp,
            color = Gray200
        )

        // 분석 결과 조회 중
        if (isLoading) {
            WorkoutAnalysisSkeleton(
                modifier = Modifier.weight(1f)
            )

            return@Column
        }

        // 분석 결과 조회 실패
        if (errorMessage != null) {
            WorkoutAnalysisErrorContent(
                message = errorMessage,
                onRetry = onRetry,
                modifier = Modifier.weight(1f)
            )

            return@Column
        }

        // 운동 분석 본문
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
                .padding(
                    top = 24.dp,
                    bottom = 24.dp
                )
        ) {

            // 전체 운동 수행률 및 요약 정보
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(40.dp)
            ) {

                // 원형 수행률 그래프
                Box(
                    modifier = Modifier.size(154.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        val stroke = 15.dp.toPx()

                        drawArc(
                            color = Color(0xFFE2E6EA),
                            startAngle = -90f,
                            sweepAngle = 360f,
                            useCenter = false,
                            style = Stroke(width = stroke)
                        )

                        drawArc(
                            color = Blue600,
                            startAngle = -90f,
                            sweepAngle = 360f * percent / 100f,
                            useCenter = false,
                            style = Stroke(
                                width = stroke,
                                cap = StrokeCap.Butt
                            )
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Text(
                            text = "$percent",
                            fontFamily = FitMateNumberFontFamily,
                            fontSize = 42.sp,
                            color = Gray700,
                            lineHeight = 48.sp
                        )

                        Text(
                            text = "%",
                            fontFamily = FitMateFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = Gray700,
                            modifier = Modifier.padding(
                                bottom = 6.dp
                            )
                        )
                    }
                }

                // 운동 요약
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    SummaryMetric(
                        label = "전체 수행률",
                        value = "$percent%"
                    )

                    Spacer(Modifier.height(13.dp))

                    SummaryMetric(
                        label = "실제 운동 시간",
                        value = formatAnalysisDuration(
                            totalSeconds
                        )
                    )

                    Spacer(Modifier.height(13.dp))

                    HorizontalDivider(
                        thickness = 1.dp,
                        color = Gray200
                    )

                    Spacer(Modifier.height(12.dp))

                    Text(
                        text = if (watchConnected) {
                            "심박수"
                        } else {
                            "총 휴식 시간"
                        },
                        fontFamily = FitMateFontFamily,
                        fontSize = 12.sp,
                        color = Gray500
                    )

                    Spacer(Modifier.height(8.dp))

                    if (watchConnected) {
                        Row(
                            horizontalArrangement =
                                Arrangement.spacedBy(16.dp)
                        ) {
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                SummaryMetric(
                                    label = "최저",
                                    value = "$minHeartRate",
                                    unit = "bpm",
                                    small = true
                                )
                            }

                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                SummaryMetric(
                                    label = "최고",
                                    value = "$maxHeartRate",
                                    unit = "bpm",
                                    small = true
                                )
                            }
                        }
                    } else {
                        Text(
                            text = restSeconds?.let {
                                formatAnalysisDuration(it)
                            } ?: "기록 없음",
                            fontFamily = if (restSeconds != null) {
                                FitMateNumberFontFamily
                            } else {
                                FitMateFontFamily
                            },
                            fontSize = if (restSeconds != null) {
                                18.sp
                            } else {
                                14.sp
                            },
                            color = Gray700
                        )
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            HorizontalDivider(
                thickness = 1.dp,
                color = Gray200
            )

            Spacer(Modifier.height(28.dp))

            // 운동별 분석
            Text(
                text = "운동별 분석",
                fontFamily = FitMateFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
                color = Gray700
            )

            Spacer(Modifier.height(12.dp))

            items.forEachIndexed { index, item ->

                AnalysisExerciseCard(
                    number = index + 1,
                    item = item
                )

                if (index != items.lastIndex) {
                    Spacer(Modifier.height(8.dp))
                }
            }
        }

        // 하단 버튼
        Button(
            onClick = onNext,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(
                    top = 12.dp,
                    bottom = 16.dp
                )
                .height(50.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Blue600
            )
        ) {
            Text(
                text = if (hasNextRoutine) {
                    "다음 운동 제안 보기"
                } else {
                    "홈으로 돌아가기"
                },
                fontFamily = FitMateFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
    }
}

/**
 * 운동 요약 정보
 */
@Composable
private fun SummaryMetric(
    label: String,
    value: String,
    unit: String = "",
    small: Boolean = false
) {
    Text(
        text = label,
        fontFamily = FitMateFontFamily,
        fontSize = 12.sp,
        color = Gray500
    )

    Spacer(Modifier.height(4.dp))

    Row(
        verticalAlignment = Alignment.Bottom
    ) {
        Text(
            text = value,
            fontFamily = if (small) {
                FitMateFontFamily
            } else {
                FitMateNumberFontFamily
            },
            fontSize = if (small) 18.sp else 20.sp,
            lineHeight = 28.sp,
            color = Gray700
        )

        if (unit.isNotEmpty()) {

            Spacer(Modifier.width(3.dp))

            Text(
                text = unit,
                fontFamily = FitMateFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = Gray700,
                modifier = Modifier.padding(
                    bottom = 2.dp
                )
            )
        }
    }
}

/**
 * 개별 운동 분석 카드
 */
@Composable
private fun AnalysisExerciseCard(
    number: Int,
    item: WorkoutAnalysisItem
) {

    val cardio =
        item.baselineRecordType == "DURATION" ||
                item.durationSeconds != null

    val total = if (cardio) {
        (item.durationSeconds ?: 0).coerceAtLeast(0)
    } else {
        item.totalSets.coerceAtLeast(0)
    }

    val done = if (cardio) {
        item.completedDurationSeconds.coerceAtLeast(0)
    } else {
        item.completedSets.coerceAtLeast(0)
    }

    val segments = if (cardio) {
        4
    } else {
        total.coerceAtLeast(1)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = BorderDefault,
                shape = RoundedCornerShape(20.dp)
            )
            .padding(
                start = 16.dp,
                end = 12.dp,
                top = 14.dp,
                bottom = 16.dp
            )
    ) {

        Row(
            verticalAlignment = Alignment.Top
        ) {

            // 운동 순서
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .background(
                        color = Color(0xFFF0F2F4),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "$number",
                    fontFamily = FitMateFontFamily,
                    fontSize = 12.sp,
                    color = Gray700
                )
            }

            Spacer(Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {

                // 운동 이름
                Text(
                    text = item.name,
                    modifier = Modifier.wrapContentHeight(
                        align = Alignment.Top
                    ),
                    fontFamily = FitMateFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    lineHeight = 14.sp,
                    color = Gray700,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = LocalTextStyle.current.copy(
                        platformStyle = PlatformTextStyle(
                            includeFontPadding = false
                        )
                    )
                )

                Spacer(Modifier.height(2.dp))

                // 운동 종류별 상세 정보
                Text(
                    text = when {

                        // 유산소 운동
                        cardio -> {
                            formatAnalysisDuration(
                                total.toLong()
                            )
                        }

                        // 중량이 없는 운동
                        item.baselineRecordType == "REPS_SETS" -> {
                            "${item.reps}회 × ${item.totalSets}세트"
                        }

                        // 중량 운동
                        else -> {
                            val weight = item.actualWeightKg
                                ?: item.weightKg.toDouble()

                            val weightText =
                                if (weight % 1.0 == 0.0) {
                                    weight.toInt().toString()
                                } else {
                                    weight.toString()
                                }

                            "${weightText}kg / " +
                                    "${item.reps}회 × " +
                                    "${item.totalSets}세트"
                        }
                    },
                    fontFamily = FitMateFontFamily,
                    fontSize = 12.sp,
                    color = Gray500,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(Modifier.width(8.dp))

            // 운동별 분석 난이도
            Box(
                modifier = Modifier
                    .border(
                        width = 1.dp,
                        color = BorderDefault,
                        shape = RoundedCornerShape(30.dp)
                    )
                    .padding(
                        horizontal = 12.dp,
                        vertical = 5.dp
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = formatAnalysisDifficulty(
                        item.difficulty
                    ),
                    fontFamily = FitMateFontFamily,
                    fontSize = 12.sp,
                    color = Gray700
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        // 운동별 수행률 그래프
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(3.dp)
            ) {

                repeat(segments) { index ->

                    val filled = if (cardio) {
                        total > 0 &&
                                done.toFloat() / total >=
                                (index + 1).toFloat() / segments
                    } else {
                        index < done
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(10.dp)
                            .background(
                                color = if (filled) {
                                    Color(0xFF61B3FF)
                                } else {
                                    Color(0xFFE2E6EA)
                                },
                                shape = RoundedCornerShape(3.dp)
                            )
                    )
                }
            }

            Spacer(Modifier.width(12.dp))

            Text(
                text = if (cardio) {
                    "${formatAnalysisDuration(done.toLong())}/" +
                            formatAnalysisDuration(total.toLong())
                } else {
                    "$done/$total 세트"
                },
                fontFamily = FitMateFontFamily,
                fontSize = 12.sp,
                color = Gray700
            )
        }
    }
}

/**
 * 운동 분석 결과 조회 스켈레톤
 */
@Composable
private fun WorkoutAnalysisSkeleton(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(40.dp)
        ) {

            FitMateSkeleton(
                modifier = Modifier.size(154.dp),
                shape = CircleShape
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                repeat(3) {

                    FitMateSkeleton(
                        modifier = Modifier
                            .fillMaxWidth(0.7f)
                            .height(12.dp)
                    )

                    FitMateSkeleton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(24.dp)
                    )
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        HorizontalDivider(
            thickness = 1.dp,
            color = Gray200
        )

        Spacer(Modifier.height(28.dp))

        FitMateSkeleton(
            modifier = Modifier
                .width(100.dp)
                .height(20.dp)
        )

        Spacer(Modifier.height(12.dp))

        repeat(4) {

            FitMateSkeleton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                shape = RoundedCornerShape(20.dp)
            )

            Spacer(Modifier.height(8.dp))
        }
    }
}

/**
 * 운동 분석 조회 오류 화면
 */
@Composable
private fun WorkoutAnalysisErrorContent(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {

    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {

        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = message,
                fontFamily = FitMateFontFamily,
                fontSize = 14.sp,
                color = Gray500,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = onRetry,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Blue600
                )
            ) {
                Text(
                    text = "다시 시도",
                    fontFamily = FitMateFontFamily,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

/**
 * 운동 시간을 분, 초 단위로 변환
 */
private fun formatAnalysisDuration(
    seconds: Long
): String {

    val safeSeconds = seconds.coerceAtLeast(0L)

    val minutes = safeSeconds / 60
    val remainingSeconds = safeSeconds % 60

    return when {
        minutes == 0L -> {
            "${remainingSeconds}초"
        }

        remainingSeconds == 0L -> {
            "${minutes}분"
        }

        else -> {
            "${minutes}분 ${remainingSeconds}초"
        }
    }
}

/**
 * 서버 운동 분석 난이도를 한글로 변환
 */
private fun formatAnalysisDifficulty(
    difficulty: String?
): String {

    return when (difficulty) {
        "TOO_EASY" -> "너무 쉬움"
        "EASY" -> "쉬움"
        "NORMAL", "MODERATE" -> "보통"
        "HARD" -> "어려움"
        "TOO_HARD" -> "너무 어려움"
        null, "" -> "미평가"
        else -> difficulty
    }
}