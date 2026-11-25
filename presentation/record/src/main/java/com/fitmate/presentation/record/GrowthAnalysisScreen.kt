package com.fitmate.presentation.record

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.fitmate.domain.model.BodyCompositionGrowth
import com.fitmate.domain.model.GrowthExercise
import com.fitmate.presentation.designsystem.R
import com.fitmate.presentation.designsystem.component.BodyMetricCard
import com.fitmate.presentation.designsystem.component.FitMateSkeleton
import com.fitmate.presentation.designsystem.theme.*
import java.time.YearMonth
import java.time.ZoneId
import kotlin.math.roundToInt

// ========================================
// 1. 신체 지표 UI 모델
// ========================================

private data class Metric(
    val label: String,
    val value: String,
    val unit: String,
    val measuredDate: String?,
    val highest: List<Float?>,
    val lowest: List<Float?>
)

// 최근 6개월 생성
private fun recentMonths(): List<YearMonth> {

    val currentMonth = YearMonth.now(
        ZoneId.of("Asia/Seoul")
    )

    return (5 downTo 0).map {
        currentMonth.minusMonths(it.toLong())
    }
}

// 측정값 표시 형식
private fun formatMetricValue(value: Double?): String {

    return value?.let {
        java.math.BigDecimal.valueOf(it)
            .stripTrailingZeros()
            .toPlainString()
    } ?: "-"
}

// 서버 신체 정보 → 화면 표시 데이터
private fun BodyCompositionGrowth.toMetrics(
    months: List<YearMonth>
): List<Metric> {

    val records = monthlyRecords.associateBy {
        YearMonth.of(it.year, it.month)
    }

    return listOf(

        // 몸무게
        Metric(
            label = "몸무게",
            value = formatMetricValue(
                latest?.bodyWeight?.value
            ),
            unit = "kg",
            measuredDate =
                latest?.bodyWeight?.measuredDate,

            highest = months.map {
                records[it]?.bodyWeight?.max?.toFloat()
            },

            lowest = months.map {
                records[it]?.bodyWeight?.min?.toFloat()
            }
        ),

        // 골격근량
        Metric(
            label = "골격근량",
            value = formatMetricValue(
                latest?.skeletalMuscleMass?.value
            ),
            unit = "kg",
            measuredDate =
                latest?.skeletalMuscleMass?.measuredDate,

            highest = months.map {
                records[it]?.skeletalMuscleMass
                    ?.max?.toFloat()
            },

            lowest = months.map {
                records[it]?.skeletalMuscleMass
                    ?.min?.toFloat()
            }
        ),

        // 체지방률
        Metric(
            label = "체지방률",
            value = formatMetricValue(
                latest?.bodyFatPercentage?.value
            ),
            unit = "%",
            measuredDate =
                latest?.bodyFatPercentage?.measuredDate,

            highest = months.map {
                records[it]?.bodyFatPercentage
                    ?.max?.toFloat()
            },

            lowest = months.map {
                records[it]?.bodyFatPercentage
                    ?.min?.toFloat()
            }
        ),

        // 체지방량
        Metric(
            label = "체지방량",
            value = formatMetricValue(
                latest?.bodyFatMass?.value
            ),
            unit = "kg",
            measuredDate =
                latest?.bodyFatMass?.measuredDate,

            highest = months.map {
                records[it]?.bodyFatMass?.max?.toFloat()
            },

            lowest = months.map {
                records[it]?.bodyFatMass?.min?.toFloat()
            }
        )
    )
}

// ========================================
// 2. 성장 분석 화면
// ========================================

@Composable
fun GrowthAnalysisScreen(
    onBack: () -> Unit = {},
    viewModel: RecordViewModel
) {

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(viewModel) {
        viewModel.loadGrowthAnalysis()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
    ) {

        RecordTabs(
            selected = 1,
            onSelect = { selected ->
                if (selected == 0) {
                    onBack()
                }
            }
        )

        GrowthAnalysisContent(
            modifier = Modifier.weight(1f),
            uiState = uiState,
            onExerciseSelected =
                viewModel::loadExerciseGrowth,
            onRetry = {
                viewModel.loadGrowthAnalysis(
                    forceRefresh = true
                )
            }
        )
    }
}

// ========================================
// 3. 성장 분석 콘텐츠
// ========================================

@Composable
fun GrowthAnalysisContent(
    modifier: Modifier = Modifier,
    uiState: RecordUiState,
    onExerciseSelected: (Long) -> Unit,
    onRetry: () -> Unit
) {

    var selectedMetric by remember {
        mutableIntStateOf(0)
    }

    var selectedExerciseId by remember {
        mutableStateOf<Long?>(null)
    }

    var expanded by remember {
        mutableStateOf(false)
    }

    val analysis = uiState.growthAnalysis

    // 초기 로딩
    if (
        uiState.isGrowthLoading ||
        (
                analysis == null &&
                        uiState.growthErrorMessage == null
                )
    ) {

        GrowthAnalysisSkeleton(
            modifier = modifier
        )

        return
    }

    // 초기 조회 실패
    if (uiState.growthErrorMessage != null) {

        Box(
            modifier = modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {

            GrowthErrorContent(
                message = uiState.growthErrorMessage,
                onRetry = onRetry
            )
        }

        return
    }

    if (analysis == null) return

    // 최근 6개월
    val months = recentMonths()

    val monthLabels = months.map {
        "${it.monthValue}월"
    }

    // 신체 지표
    val metrics = analysis.bodyComposition
        .toMetrics(months)

    val currentMetric = metrics[selectedMetric]

    // 실제 수행한 중량 운동 목록
    val exercises = analysis.workoutGrowth.exercises

    // 성장 분석 초기 조회 시 기본 운동 선택
    LaunchedEffect(analysis) {

        selectedExerciseId =
            analysis.workoutGrowth
                .selectedExercise
                ?.exerciseId
                ?: exercises.firstOrNull()?.exerciseId

        expanded = false

        val exerciseId = selectedExerciseId

        if (
            exerciseId != null &&
            uiState.exerciseGrowth?.exerciseId != exerciseId
        ) {
            onExerciseSelected(exerciseId)
        }
    }

    val currentExercise = exercises.find {
        it.exerciseId == selectedExerciseId
    } ?: analysis.workoutGrowth.selectedExercise?.let {
        GrowthExercise(
            exerciseId = it.exerciseId,
            exerciseName = it.exerciseName
        )
    }

    // 선택한 운동과 조회 결과의 ID가 일치할 때만 사용
    val exerciseGrowth = uiState.exerciseGrowth
        ?.takeIf {
            it.exerciseId == currentExercise?.exerciseId
        }

    val exerciseRecords = exerciseGrowth
        ?.monthlyRecords
        .orEmpty()
        .associateBy {
            YearMonth.of(it.year, it.month)
        }

    // 실제 월별 최대 중량
    val exerciseHighest = months.map {
        exerciseRecords[it]?.maxWeight?.toFloat()
    }

    // 실제 월별 최소 중량
    val exerciseLowest = months.map {
        exerciseRecords[it]?.minWeight?.toFloat()
    }

    // 실제 월별 출석률
    val attendanceRecords = analysis.attendance
        .monthlyRecords
        .associateBy {
            YearMonth.of(it.year, it.month)
        }

    val attendanceRates = months.map {
        attendanceRecords[it]
            ?.attendanceRate
            ?.roundToInt()
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
    ) {

        Spacer(Modifier.height(16.dp))

        // ========================================
        // 신체 정보
        // ========================================

        Text(
            text = "신체 정보",
            style = recordStyle(
                size = 14,
                bold = true
            ),
            color = Color.Black
        )

        Spacer(Modifier.height(12.dp))

        // 기존 BodyMetricCard 4개 유지
        metrics.chunked(2)
            .forEachIndexed { rowIndex, pair ->

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement =
                        Arrangement.spacedBy(14.dp)
                ) {

                    pair.forEachIndexed { index, metric ->

                        val position = rowIndex * 2 + index

                        BodyMetricCard(
                            label = metric.label,
                            value = metric.value,
                            unit = metric.unit,
                            caption = if (
                                metric.measuredDate != null
                            ) {
                                "최근 측정"
                            } else {
                                "측정 기록 없음"
                            },
                            selected =
                                selectedMetric == position,
                            onClick = {
                                selectedMetric = position
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                if (rowIndex == 0) {
                    Spacer(Modifier.height(12.dp))
                }
            }

        Spacer(Modifier.height(28.dp))

        // ========================================
        // 신체 변화 그래프
        // ========================================

        Text(
            text = "${currentMetric.label} 변화(${currentMetric.unit})",
            style = recordStyle(
                size = 14,
                bold = true
            ),
            color = Color.Black
        )

        Spacer(Modifier.height(12.dp))

        DualLineChartCard(
            highest = currentMetric.highest,
            lowest = currentMetric.lowest,
            months = monthLabels
        )

        ChartPeriodLabel()

        Spacer(Modifier.height(28.dp))

        // ========================================
        // 운동 성장
        // ========================================

        Text(
            text = "운동 성장(kg)",
            style = recordStyle(
                size = 14,
                bold = true
            ),
            color = Color.Black
        )

        Spacer(Modifier.height(12.dp))

        // 실제 운동 목록 드롭다운
        ExerciseDropdown(
            selectedExercise =
                currentExercise?.exerciseName
                    ?: "운동 기록 없음",

            exercises = exercises,

            expanded = expanded,

            onExpandedChange = {
                expanded = it
            },

            onExerciseSelected = { exercise ->

                selectedExerciseId =
                    exercise.exerciseId

                expanded = false

                onExerciseSelected(
                    exercise.exerciseId
                )
            }
        )

        Spacer(Modifier.height(8.dp))

        when {

            // 운동별 성장 기록 로딩
            uiState.isExerciseGrowthLoading -> {

                FitMateSkeleton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(216.dp)
                )
            }

            // 운동별 성장 기록 조회 실패
            uiState.exerciseGrowthErrorMessage != null -> {

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(216.dp),
                    color = White,
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(
                        1.dp,
                        BorderDefault
                    )
                ) {

                    Box(
                        contentAlignment = Alignment.Center
                    ) {

                        GrowthErrorContent(
                            message =
                                uiState.exerciseGrowthErrorMessage,

                            onRetry = {

                                currentExercise
                                    ?.exerciseId
                                    ?.let(onExerciseSelected)
                            }
                        )
                    }
                }
            }

            // 선택 운동의 실제 최고/최저 중량
            else -> {

                DualLineChartCard(
                    highest = exerciseHighest,
                    lowest = exerciseLowest,
                    months = monthLabels
                )
            }
        }

        ChartPeriodLabel()

        Spacer(Modifier.height(28.dp))

        // ========================================
        // 출석률
        // ========================================

        Text(
            text = "출석률",
            style = recordStyle(
                size = 14,
                bold = true
            ),
            color = Color.Black
        )

        Spacer(Modifier.height(12.dp))

        AttendanceChartCard(
            values = attendanceRates,
            months = monthLabels
        )

        ChartPeriodLabel()

        Spacer(Modifier.height(23.dp))
    }
}

// ========================================
// 4. 최고/최저 선 그래프
// ========================================

@Composable
private fun DualLineChartCard(
    highest: List<Float?>,
    lowest: List<Float?>,
    months: List<String>
) {

    val allValues =
        (highest + lowest).filterNotNull()

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(216.dp),
        color = White,
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(
            1.dp,
            BorderDefault
        )
    ) {

        Column(
            modifier = Modifier.padding(
                start = 24.dp,
                end = 24.dp,
                top = 20.dp,
                bottom = 20.dp
            )
        ) {

            // 오른쪽 위 범례
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                ChartLegend(
                    label = "최고",
                    color = Red500
                )

                Spacer(Modifier.width(16.dp))

                ChartLegend(
                    label = "최저",
                    color = Blue600
                )
            }

            Spacer(Modifier.height(16.dp))

            if (allValues.isEmpty()) {

                // 기록이 없는 경우
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {

                    Text(
                        text = "아직 기록이 없어요.",
                        style = recordStyle(13),
                        color = Gray500
                    )
                }

            } else {

                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {

                    val minValue =
                        (allValues.minOrNull() ?: 0f) - 1f

                    val maxValue =
                        (allValues.maxOrNull() ?: 1f) + 1f

                    val range =
                        (maxValue - minValue)
                            .coerceAtLeast(1f)

                    val pointRadius = 4.dp.toPx()

                    val chartPadding =
                        pointRadius + 1.5.dp.toPx()

                    val usableWidth =
                        (size.width - chartPadding * 2)
                            .coerceAtLeast(0f)

                    val usableHeight =
                        (size.height - chartPadding * 2)
                            .coerceAtLeast(0f)

                    // null인 달은 좌표를 생성하지 않음
                    fun makePoints(
                        values: List<Float?>
                    ): List<Offset?> {

                        return values.mapIndexed {
                                index, value ->

                            if (value == null) {

                                null

                            } else {

                                // 월 레이블의 중앙과 일치
                                val x = size.width *
                                        (index + 0.5f) /
                                        months.size.coerceAtLeast(1)

                                val y = chartPadding +
                                        usableHeight *
                                        (
                                                1f -
                                                        (value - minValue) /
                                                        range
                                                )

                                Offset(x, y)
                            }
                        }
                    }

                    fun drawSeries(
                        values: List<Float?>,
                        lineColor: Color
                    ) {

                        // 실제 기록이 존재하는 좌표만 사용
                        val positions = makePoints(values)
                            .filterNotNull()

                        if (positions.isEmpty()) return

                        // 기록이 있는 점끼리 연결
                        val path = Path().apply {

                            positions.forEachIndexed { index, point ->

                                if (index == 0) {
                                    moveTo(point.x, point.y)
                                } else {
                                    lineTo(point.x, point.y)
                                }
                            }
                        }

                        // 그래프 연결선
                        if (positions.size >= 2) {

                            drawPath(
                                path = path,
                                color = lineColor,
                                style = Stroke(
                                    width = 2.5.dp.toPx()
                                )
                            )
                        }

                        // 실제 기록이 존재하는 달에만 점 표시
                        positions.forEach { point ->

                            drawCircle(
                                color = lineColor,
                                radius = pointRadius,
                                center = point
                            )

                            // 기존 흰색 테두리 유지
                            drawCircle(
                                color = White,
                                radius = pointRadius,
                                center = point,
                                style = Stroke(
                                    width = 1.5.dp.toPx()
                                )
                            )
                        }
                    }

                    drawSeries(
                        values = highest,
                        lineColor = Red500
                    )

                    drawSeries(
                        values = lowest,
                        lineColor = Blue600
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            // 월별 레이블 위 가로 기준선
            HorizontalDivider(
                thickness = 1.dp,
                color = Gray200
            )

            Spacer(Modifier.height(10.dp))

            MonthLabels(
                months = months
            )
        }
    }
}

// ========================================
// 5. 범례
// ========================================

@Composable
private fun ChartLegend(
    label: String,
    color: Color
) {

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(8.dp)
                .background(
                    color = color,
                    shape = CircleShape
                )
        )

        Spacer(Modifier.width(5.dp))

        Text(
            text = label,
            style = recordStyle(12),
            color = Gray500
        )
    }
}

// ========================================
// 6. 공통 월 레이블
// ========================================

@Composable
private fun MonthLabels(
    months: List<String>
) {

    Row(
        modifier = Modifier.fillMaxWidth()
    ) {

        months.forEach { month ->

            Text(
                text = month,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                style = recordStyle(11),
                color = Gray500
            )
        }
    }
}

// ========================================
// 7. 최근 6개월 표시
// ========================================

@Composable
private fun ChartPeriodLabel() {

    Spacer(Modifier.height(12.dp))

    Text(
        text = "최근 6개월",
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.End,
        style = recordStyle(12),
        color = Gray500
    )
}

// ========================================
// 8. 출석률 막대 그래프
// ========================================

@Composable
private fun AttendanceChartCard(
    values: List<Int?>,
    months: List<String>
) {

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(216.dp),
        color = White,
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(
            1.dp,
            BorderDefault
        )
    ) {

        Column(
            modifier = Modifier.padding(
                horizontal = 20.dp,
                vertical = 22.dp
            )
        ) {

            if (values.all { it == null }) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {

                    Text(
                        text = "아직 출석 기록이 없어요.",
                        style = recordStyle(13),
                        color = Gray500
                    )
                }

            } else {

                // 막대그래프 영역
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalAlignment = Alignment.Bottom
                ) {

                    values.forEach { rate ->

                        Column(
                            modifier = Modifier.weight(1f),
                            horizontalAlignment =
                                Alignment.CenterHorizontally,
                            verticalArrangement =
                                Arrangement.Bottom
                        ) {

                            // null은 0%와 구분
                            Text(
                                text = rate?.toString() ?: "-",
                                style = recordStyle(10),
                                color = Gray600
                            )

                            Spacer(Modifier.height(4.dp))

                            if (rate != null) {

                                Box(
                                    modifier = Modifier
                                        .width(25.dp)
                                        .height(
                                            (
                                                    rate.coerceIn(
                                                        0,
                                                        100
                                                    ) * 1.05f
                                                    ).dp
                                        )
                                        .background(Blue400)
                                )
                            }
                        }
                    }
                }
            }

            // 막대 아래 기준선
            HorizontalDivider(
                thickness = 1.dp,
                color = Gray200
            )

            Spacer(Modifier.height(10.dp))

            MonthLabels(
                months = months
            )
        }
    }
}

// ========================================
// 9. 운동 선택 드롭다운
// ========================================

@Composable
private fun ExerciseDropdown(
    selectedExercise: String,
    exercises: List<GrowthExercise>,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onExerciseSelected: (GrowthExercise) -> Unit
) {

    var buttonSize by remember {
        mutableStateOf(IntSize.Zero)
    }

    val density = LocalDensity.current

    val closedShape =
        RoundedCornerShape(14.dp)

    val openedButtonShape =
        RoundedCornerShape(
            topStart = 14.dp,
            topEnd = 14.dp,
            bottomStart = 0.dp,
            bottomEnd = 0.dp
        )

    val openedListShape =
        RoundedCornerShape(
            topStart = 0.dp,
            topEnd = 0.dp,
            bottomStart = 14.dp,
            bottomEnd = 14.dp
        )

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {

        // 드롭다운 버튼
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .onSizeChanged {
                    buttonSize = it
                }
                .clip(
                    if (expanded) {
                        openedButtonShape
                    } else {
                        closedShape
                    }
                )
                .background(White)
                .border(
                    width = 1.dp,
                    color = BorderDefault,
                    shape = if (expanded) {
                        openedButtonShape
                    } else {
                        closedShape
                    }
                )
                .clickable(
                    enabled = exercises.isNotEmpty()
                ) {
                    onExpandedChange(!expanded)
                }
                .padding(horizontal = 24.dp),
            verticalAlignment =
                Alignment.CenterVertically,
            horizontalArrangement =
                Arrangement.SpaceBetween
        ) {

            Text(
                text = selectedExercise,
                style = recordStyle(
                    size = 16,
                    bold = true
                ),
                color = if (exercises.isEmpty()) {
                    Gray500
                } else {
                    Gray900
                },
                modifier = Modifier.weight(1f),
                maxLines = 1
            )

            Icon(
                painter = painterResource(
                    id = R.drawable.ic_record_dropdown_arrow
                ),
                contentDescription = if (expanded) {
                    "운동 목록 닫기"
                } else {
                    "운동 목록 열기"
                },
                tint = if (exercises.isEmpty()) {
                    Gray200
                } else {
                    Color.Unspecified
                },
                modifier = Modifier
                    .size(24.dp)
                    .rotate(
                        if (expanded) 180f else 0f
                    )
            )
        }

        // 차트 위에 겹쳐 표시되는 운동 목록
        if (
            expanded &&
            buttonSize.width > 0 &&
            exercises.isNotEmpty()
        ) {

            Popup(
                alignment = Alignment.TopStart,
                offset = IntOffset(
                    x = 0,
                    y = buttonSize.height - with(density) {
                        1.dp.roundToPx()
                    }
                ),
                onDismissRequest = {
                    onExpandedChange(false)
                },
                properties = PopupProperties(
                    focusable = true,
                    dismissOnBackPress = true,
                    dismissOnClickOutside = true
                )
            ) {

                Column(
                    modifier = Modifier
                        .width(
                            with(density) {
                                buttonSize.width.toDp()
                            }
                        )
                        .heightIn(max = 320.dp)
                        .clip(openedListShape)
                        .background(White)
                        .border(
                            width = 1.dp,
                            color = BorderDefault,
                            shape = openedListShape
                        )
                        .verticalScroll(
                            rememberScrollState()
                        )
                ) {

                    exercises.forEachIndexed {
                            index, exercise ->

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(64.dp)
                                .clickable {
                                    onExerciseSelected(exercise)
                                }
                                .padding(horizontal = 24.dp),
                            verticalAlignment =
                                Alignment.CenterVertically
                        ) {

                            Text(
                                text = exercise.exerciseName,
                                style = recordStyle(16),
                                color = Gray900
                            )
                        }

                        if (index != exercises.lastIndex) {

                            HorizontalDivider(
                                thickness = 1.dp,
                                color = Gray200
                            )
                        }
                    }
                }
            }
        }
    }
}

// ========================================
// 10. 성장 분석 스켈레톤
// ========================================

@Composable
private fun GrowthAnalysisSkeleton(
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
    ) {

        Spacer(Modifier.height(16.dp))

        Text(
            text = "신체 정보",
            style = recordStyle(
                size = 14,
                bold = true
            ),
            color = Color.Black
        )

        Spacer(Modifier.height(12.dp))

        // 신체 정보 카드 4개
        repeat(2) { index ->

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.spacedBy(14.dp)
            ) {

                repeat(2) {

                    FitMateSkeleton(
                        modifier = Modifier
                            .weight(1f)
                            .height(100.dp)
                    )
                }
            }

            if (index == 0) {
                Spacer(Modifier.height(12.dp))
            }
        }

        Spacer(Modifier.height(28.dp))

        // 신체 변화 그래프
        Text(
            text = "신체 변화",
            style = recordStyle(
                size = 14,
                bold = true
            ),
            color = Color.Black
        )

        Spacer(Modifier.height(12.dp))

        FitMateSkeleton(
            modifier = Modifier
                .fillMaxWidth()
                .height(216.dp)
        )

        ChartPeriodLabel()

        Spacer(Modifier.height(28.dp))

        // 운동 성장
        Text(
            text = "운동 성장(kg)",
            style = recordStyle(
                size = 14,
                bold = true
            ),
            color = Color.Black
        )

        Spacer(Modifier.height(12.dp))

        FitMateSkeleton(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
        )

        Spacer(Modifier.height(8.dp))

        FitMateSkeleton(
            modifier = Modifier
                .fillMaxWidth()
                .height(216.dp)
        )

        ChartPeriodLabel()

        Spacer(Modifier.height(28.dp))

        // 출석률
        Text(
            text = "출석률",
            style = recordStyle(
                size = 14,
                bold = true
            ),
            color = Color.Black
        )

        Spacer(Modifier.height(12.dp))

        FitMateSkeleton(
            modifier = Modifier
                .fillMaxWidth()
                .height(216.dp)
        )

        ChartPeriodLabel()

        Spacer(Modifier.height(23.dp))
    }
}

// ========================================
// 11. API 오류 및 재시도
// ========================================

@Composable
private fun GrowthErrorContent(
    message: String,
    onRetry: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {

        Text(
            text = message,
            style = recordStyle(13),
            color = Gray600,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(8.dp))

        TextButton(
            onClick = onRetry
        ) {

            Text(
                text = "다시 시도",
                style = recordStyle(
                    size = 13,
                    bold = true
                ),
                color = Blue600
            )
        }
    }
}
