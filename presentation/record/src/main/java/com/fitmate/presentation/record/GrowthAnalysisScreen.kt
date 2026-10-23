
package com.fitmate.presentation.record

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.fitmate.presentation.designsystem.component.BodyMetricCard
import com.fitmate.presentation.designsystem.theme.*
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import com.fitmate.presentation.designsystem.R
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.clickable
import androidx.compose.foundation.border


// ========================================
// 1. 데이터
// ========================================

private data class Metric(
    val label: String,
    val value: String,
    val unit: String,
    val points: List<Float>,
    val highest: List<Float>,
    val lowest: List<Float>
)

private val metrics = listOf(
    Metric(
        label = "몸무게",
        value = "68.4",
        unit = "kg",
        points = listOf(72f, 71f, 70.5f, 70f, 69f, 68.4f),
        highest = listOf(73f, 72f, 71.5f, 71f, 70f, 69.4f),
        lowest = listOf(71f, 70f, 69.5f, 69f, 68f, 67.8f)
    ),
    Metric(
        label = "골격근량",
        value = "25.6",
        unit = "kg",
        points = listOf(23.4f, 23.9f, 24.1f, 24.8f, 25.1f, 25.6f),
        highest = listOf(23.8f, 24.3f, 24.5f, 25.1f, 25.4f, 26f),
        lowest = listOf(23f, 23.5f, 23.8f, 24.4f, 24.8f, 25.2f)
    ),
    Metric(
        label = "체지방률",
        value = "28.4",
        unit = "%",
        points = listOf(32f, 31.4f, 30.8f, 29.9f, 29f, 28.4f),
        highest = listOf(32.5f, 31.9f, 31.3f, 30.4f, 29.5f, 28.9f),
        lowest = listOf(31.5f, 30.9f, 30.3f, 29.4f, 28.5f, 27.9f)
    ),
    Metric(
        label = "체지방량",
        value = "19.4",
        unit = "kg",
        points = listOf(22.1f, 21.6f, 21.3f, 20.7f, 20f, 19.4f),
        highest = listOf(22.6f, 22.1f, 21.8f, 21.2f, 20.5f, 19.9f),
        lowest = listOf(21.6f, 21.1f, 20.8f, 20.2f, 19.5f, 18.9f)
    )
)

private val exerciseSeries = mapOf(
    "벤치프레스" to listOf(
        30f, 32.5f, 35f, 37.5f, 40f, 42.5f
    ),
    "스쿼트" to listOf(
        40f, 45f, 47.5f, 50f, 55f, 60f
    ),
    "데드리프트" to listOf(
        45f, 50f, 55f, 60f, 65f, 70f
    )
)

private val months = listOf(
    "4월", "5월", "6월", "7월", "8월", "9월"
)

// 출석률 UI 확인용 더미 데이터 (%)
private val attendanceRates = listOf(
    60, 75, 80, 70, 85, 90
)

// ========================================
// 2. 성장 분석 화면
// ========================================

@Composable
fun GrowthAnalysisScreen(
    onBack: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
    ) {
        RecordTabs(1) { selected ->
            if (selected == 0) {
                onBack()
            }
        }

        GrowthAnalysisContent(
            modifier = Modifier.weight(1f)
        )
    }
}

// ========================================
// 3. 성장 분석 콘텐츠
// ========================================

@Composable
fun GrowthAnalysisContent(
    modifier: Modifier = Modifier
) {
    var selectedMetric by remember {
        mutableIntStateOf(0)
    }

    var selectedExercise by remember {
        mutableStateOf("벤치프레스")
    }

    var expanded by remember {
        mutableStateOf(false)
    }

    val currentMetric = metrics[selectedMetric]

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

        // 기존 BodyMetricCard 4개 유지
        metrics.chunked(2).forEachIndexed { rowIndex, pair ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                pair.forEachIndexed { index, metric ->
                    val position = rowIndex * 2 + index

                    BodyMetricCard(
                        label = metric.label,
                        value = metric.value,
                        unit = metric.unit,
                        caption = "최근 측정",
                        selected = selectedMetric == position,
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
            lowest = currentMetric.lowest
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


        // 운동 종류 선택 드롭다운
        ExerciseDropdown(
            selectedExercise = selectedExercise,
            expanded = expanded,
            onExpandedChange = { expanded = it },
            onExerciseSelected = { exercise ->
                selectedExercise = exercise
                expanded = false
            }
        )


        Spacer(Modifier.height(8.dp))

        val exerciseValues = exerciseSeries.getValue(
            selectedExercise
        )

        // 운동 성장도 동일한 최고/최저 그래프
        // 현재는 기존 운동 기록을 기준으로
        // 임시 최고/최저 범위를 표시
        DualLineChartCard(
            highest = exerciseValues.map { it + 2.5f },
            lowest = exerciseValues.map { it - 2.5f }
        )

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
            values = attendanceRates
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
    highest: List<Float>,
    lowest: List<Float>
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
                verticalAlignment = Alignment.CenterVertically
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

            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                if (
                    highest.size < 2 ||
                    lowest.size < 2 ||
                    highest.size != lowest.size
                ) {
                    return@Canvas
                }

                val allValues = highest + lowest

                val minValue =
                    (allValues.minOrNull() ?: 0f) - 1f

                val maxValue =
                    (allValues.maxOrNull() ?: 1f) + 1f

                val range = (maxValue - minValue)
                    .coerceAtLeast(1f)

                val pointRadius = 4.dp.toPx()
                val chartPadding = pointRadius + 1.5.dp.toPx()

                fun makePoints(
                    values: List<Float>
                ): List<Offset> {
                    val usableWidth =
                        size.width - chartPadding * 2

                    val usableHeight =
                        size.height - chartPadding * 2

                    return values.mapIndexed { index, value ->
                        val x = chartPadding +
                                usableWidth * index /
                                (values.size - 1)

                        val y = chartPadding +
                                usableHeight *
                                (1f - (value - minValue) / range)

                        Offset(x, y)
                    }
                }

                fun drawSeries(
                    values: List<Float>,
                    lineColor: Color
                ) {
                    val positions = makePoints(values)

                    val path = Path().apply {
                        positions.forEachIndexed { index, point ->
                            if (index == 0) {
                                moveTo(point.x, point.y)
                            } else {
                                lineTo(point.x, point.y)
                            }
                        }
                    }

                    drawPath(
                        path = path,
                        color = lineColor,
                        style = Stroke(
                            width = 2.5.dp.toPx()
                        )
                    )

                    positions.forEach { point ->
                        // 원형 데이터 점
                        drawCircle(
                            color = lineColor,
                            radius = pointRadius,
                            center = point
                        )

                        // 흰색 1.5dp 테두리
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

            Spacer(Modifier.height(12.dp))

            // 월별 레이블 위 가로 기준선
            HorizontalDivider(
                thickness = 1.dp,
                color = Gray200
            )

            Spacer(Modifier.height(10.dp))

            MonthLabels()
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
private fun MonthLabels() {
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
    values: List<Int>
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
            // 막대그래프 영역
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Bottom
            ) {
                values.forEach { rate ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        // 퍼센트 기호 없이 숫자만 표시
                        Text(
                            text = "$rate",
                            style = recordStyle(10),
                            color = Gray600
                        )

                        Spacer(Modifier.height(4.dp))

                        // 아래 기준선에 딱 붙는 사각형 막대
                        Box(
                            modifier = Modifier
                                .width(25.dp)
                                .height((rate.coerceIn(0, 100) * 1.05f).dp)
                                .background(Blue400)
                        )
                    }
                }
            }

            // 막대 아래에 여백 없이 바로 기준선
            HorizontalDivider(
                thickness = 1.dp,
                color = Gray200
            )

            Spacer(Modifier.height(10.dp))

            // 월별 레이블
            MonthLabels()
        }
    }
}


@Composable
private fun ExerciseDropdown(
    selectedExercise: String,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onExerciseSelected: (String) -> Unit
) {
    val exercises = exerciseSeries.keys.toList()

    var buttonSize by remember {
        mutableStateOf(IntSize.Zero)
    }

    val density = LocalDensity.current

    val buttonHeight = with(density) {
        buttonSize.height.toDp()
    }

    val closedShape = RoundedCornerShape(14.dp)

    val openedButtonShape = RoundedCornerShape(
        topStart = 14.dp,
        topEnd = 14.dp,
        bottomStart = 0.dp,
        bottomEnd = 0.dp
    )

    val openedListShape = RoundedCornerShape(
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
                .clickable {
                    onExpandedChange(!expanded)
                }
                .padding(horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = selectedExercise,
                style = recordStyle(
                    size = 16,
                    bold = true
                ),
                color = Gray900
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
                tint = Color.Unspecified,
                modifier = Modifier
                    .size(24.dp)
                    .rotate(
                        if (expanded) 180f else 0f
                    )
            )
        }

        // 차트 위에 겹쳐 표시되는 목록
        if (expanded && buttonSize.width > 0) {
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
                        .clip(openedListShape)
                        .background(White)
                        .border(
                            width = 1.dp,
                            color = BorderDefault,
                            shape = openedListShape
                        )
                ) {
                    exercises.forEachIndexed { index, exercise ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(64.dp)
                                .clickable {
                                    onExerciseSelected(exercise)
                                }
                                .padding(horizontal = 24.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = exercise,
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
