package com.fitmate.presentation.record

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fitmate.presentation.designsystem.R
import com.fitmate.presentation.designsystem.component.FitMateSkeleton
import com.fitmate.presentation.designsystem.theme.*
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId
import kotlin.math.roundToInt

// ========================================
// 1. 공통 텍스트 스타일
// ========================================

internal fun recordStyle(
    size: Int,
    bold: Boolean = false,
    number: Boolean = false
) = TextStyle(
    fontFamily = if (number) {
        FitMateNumberFontFamily
    } else {
        FitMateFontFamily
    },
    fontSize = size.sp,
    fontWeight = if (bold) {
        FontWeight.Bold
    } else {
        FontWeight.Normal
    },
    lineHeight = (size * 1.4f).sp
)

// ========================================
// 2. 월별 기록 / 성장 분석 탭
// ========================================

@Composable
internal fun RecordTabs(
    selected: Int,
    onSelect: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .background(White)
            .padding(horizontal = 24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        listOf("월별 기록", "성장 분석")
            .forEachIndexed { index, title ->

                val active = selected == index

                Box(
                    modifier = Modifier
                        .padding(end = 3.dp)
                        .background(
                            color = if (active) {
                                Blue500
                            } else {
                                Gray50
                            },
                            shape = RoundedCornerShape(6.dp)
                        )
                        .border(
                            width = 1.dp,
                            color = if (active) {
                                Color.Transparent
                            } else {
                                Gray100
                            },
                            shape = RoundedCornerShape(6.dp)
                        )
                        .clickable {
                            onSelect(index)
                        }
                        .padding(
                            horizontal = 14.dp,
                            vertical = 9.dp
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = title,
                        style = recordStyle(
                            size = 13,
                            bold = active
                        ),
                        color = if (active) {
                            White
                        } else {
                            Gray600
                        }
                    )
                }
            }
    }
}

// ========================================
// 3. 기록 메인 화면
// ========================================

@Composable
fun RecordScreen(
    onOpenWorkoutDetail: (Long) -> Unit = {},
    viewModel: RecordViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    var tab by remember {
        mutableIntStateOf(0)
    }

    val today = LocalDate.now(
        ZoneId.of("Asia/Seoul")
    )

    val month = uiState.selectedMonth

    // 날짜별 운동 기록 필터링
    var chosen by remember(month) {
        mutableStateOf<LocalDate?>(null)
    }

    val monthlyRecord = uiState.monthlyRecord

    // 기록 화면 진입 시 월별 기록 조회
    LaunchedEffect(viewModel) {
        viewModel.loadMonthlyRecords()
    }

    // 성장 분석 탭 진입 시 API 호출
    LaunchedEffect(tab) {
        if (tab == 1) {
            viewModel.loadGrowthAnalysis()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
    ) {

        // 상단 헤더
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .padding(horizontal = 24.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = "운동 기록",
                style = recordStyle(
                    size = 20,
                    number = true
                ),
                color = Color.Black
            )
        }

        HorizontalDivider(
            color = Gray100
        )

        // 상단 탭
        RecordTabs(
            selected = tab,
            onSelect = { tab = it }
        )

        if (tab == 1) {

            // 성장 분석 화면
            // 다음 단계에서 실제 API 데이터 연결
            GrowthAnalysisContent(
                modifier = Modifier.weight(1f),
                uiState = uiState,
                onExerciseSelected = viewModel::loadExerciseGrowth,
                onRetry = {
                    viewModel.loadGrowthAnalysis(
                        forceRefresh = true
                    )
                }
            )

        } else {

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(
                        rememberScrollState()
                    )
                    .padding(horizontal = 24.dp)
            ) {

                Spacer(
                    Modifier.height(16.dp)
                )

                // ============================
                // 월별 캘린더
                // ============================

                CalendarCard(
                    month = month,
                    today = today,
                    marked = monthlyRecord
                        ?.workoutDates
                        ?.toSet()
                        .orEmpty(),

                    onPrevious = {
                        chosen = null
                        viewModel.selectPreviousMonth()
                    },

                    onNext = {
                        if (month.isBefore(
                                YearMonth.from(today)
                            )
                        ) {
                            chosen = null
                            viewModel.selectNextMonth()
                        }
                    },

                    onDateClick = { date ->
                        chosen = if (chosen == date) {
                            null
                        } else {
                            date
                        }
                    }
                )

                Spacer(
                    Modifier.height(28.dp)
                )

                // ============================
                // 로딩 상태
                // ============================

                val isLoading =
                    uiState.isMonthlyLoading ||
                            (
                                    monthlyRecord == null &&
                                            uiState.monthlyErrorMessage == null
                                    )

                if (isLoading) {

                    MonthlyRecordSkeleton()

                } else if (
                    uiState.monthlyErrorMessage != null
                ) {

                    // ============================
                    // 오류 상태
                    // ============================

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp),
                        horizontalAlignment =
                            Alignment.CenterHorizontally
                    ) {

                        Text(
                            text = uiState
                                .monthlyErrorMessage
                                .orEmpty(),
                            style = recordStyle(13),
                            color = Gray600,
                            textAlign = TextAlign.Center
                        )

                        Spacer(
                            Modifier.height(8.dp)
                        )

                        TextButton(
                            onClick = {
                                viewModel.loadMonthlyRecords()
                            }
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

                } else if (monthlyRecord != null) {

                    // ============================
                    // 월별 운동 통계
                    // ============================

                    val monthly = monthlyRecord.records

                    Text(
                        text = if (
                            month == YearMonth.from(today)
                        ) {
                            "이번 달"
                        } else {
                            "${month.monthValue}월"
                        },
                        style = recordStyle(14),
                        color = Gray600
                    )

                    Spacer(
                        Modifier.height(12.dp)
                    )

                    Row(
                        verticalAlignment = Alignment.Bottom
                    ) {

                        Text(
                            text = "${monthlyRecord.workoutCount}회",
                            style = recordStyle(
                                size = 30,
                                bold = false,
                                number = true
                            ),
                            color = Color.Black
                        )

                        Spacer(
                            Modifier.width(12.dp)
                        )

                        val totalMinutes =
                            monthlyRecord.totalExerciseSeconds / 60L

                        Text(
                            text = "운동 총 ${totalMinutes / 60}시간 ${totalMinutes % 60}분",
                            style = recordStyle(12),
                            color = Gray600,
                            modifier = Modifier.padding(
                                bottom = 5.dp
                            )
                        )
                    }

                    Spacer(
                        Modifier.height(30.dp)
                    )

                    // ============================
                    // 날짜별 운동 기록 필터링
                    // ============================

                    val filtered = monthly.filter {
                        chosen == null ||
                                it.date == chosen
                    }

                    // 서버에서 최신순으로 반환하므로
                    // 기존 응답 순서를 유지

                    if (filtered.isEmpty()) {

                        Text(
                            text = if (chosen == null) {
                                "이번 달 운동 기록이 없어요."
                            } else {
                                "선택한 날짜에 운동 기록이 없어요."
                            },
                            style = recordStyle(13),
                            color = Gray500,
                            modifier = Modifier.padding(
                                vertical = 24.dp
                            )
                        )
                    }

                    // ============================
                    // 운동 기록 목록
                    // ============================

                    filtered.forEach { workout ->

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(86.dp)
                                .clickable {
                                    onOpenWorkoutDetail(
                                        workout.workoutRecordId
                                    )
                                },
                            horizontalArrangement =
                                Arrangement.spacedBy(12.dp)
                        ) {

                            // 왼쪽 타임라인
                            Column(
                                horizontalAlignment =
                                    Alignment.CenterHorizontally
                            ) {

                                // 파란 동그라미
                                Box(
                                    modifier = Modifier
                                        .padding(top = 5.dp)
                                        .size(8.dp)
                                        .background(
                                            Blue600,
                                            CircleShape
                                        )
                                )

                                Spacer(
                                    Modifier.height(3.5.dp)
                                )

                                // 타임라인 세로선
                                Box(
                                    modifier = Modifier
                                        .width(1.dp)
                                        .height(68.5.dp)
                                        .background(Gray200)
                                )
                            }

                            // 운동 정보
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {

                                // 운동 날짜
                                Text(
                                    text = "${workout.date.monthValue}월 ${workout.date.dayOfMonth}일",
                                    style = recordStyle(13),
                                    color = Gray600
                                )

                                Spacer(
                                    Modifier.height(8.dp)
                                )

                                // 주요 운동 부위
                                Text(
                                    text = workout.mainTarget,
                                    style = recordStyle(
                                        size = 16,
                                        bold = false,
                                        number = true
                                    ),
                                    color = Color.Black
                                )

                                Spacer(
                                    Modifier.height(4.dp)
                                )

                                // 실제 운동 시간 및 수행률
                                Text(
                                    text = buildString {

                                        val minutes =
                                            workout.actualExerciseSeconds / 60L

                                        val seconds =
                                            workout.actualExerciseSeconds % 60L

                                        if (minutes == 0L) {
                                            append("${seconds}초")
                                        } else {
                                            append("${minutes}분")
                                        }

                                        append(
                                            " · 수행률 ${workout.completionRate.roundToInt()}%"
                                        )

                                        workout.maxHeartRate?.let {
                                                heartRate ->

                                            append(
                                                " · 최대 심박 ${heartRate}bpm"
                                            )
                                        }
                                    },
                                    style = recordStyle(12),
                                    color = Gray600
                                )

                                Spacer(
                                    Modifier.height(6.dp)
                                )

                                // 실제 수행 운동 목록
                                Text(
                                    text = workout.exerciseNames
                                        .joinToString(" · "),
                                    style = recordStyle(12),
                                    color = Gray600,
                                    maxLines = 1
                                )
                            }

                            // 운동 기록 상세 이동 아이콘
                            Icon(
                                painter = painterResource(
                                    id = R.drawable.ic_record_arrow_right
                                ),
                                contentDescription =
                                    "운동 기록 상세 보기",
                                tint = Color.Unspecified,
                                modifier = Modifier.size(10.dp)
                            )
                        }

                        Spacer(
                            Modifier.height(26.dp)
                        )
                    }
                }

                Spacer(
                    Modifier.height(23.dp)
                )
            }
        }
    }
}

// ========================================
// 4. 월별 기록 스켈레톤
// ========================================

@Composable
private fun MonthlyRecordSkeleton() {

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        // 이번 달 텍스트 영역
        FitMateSkeleton(
            modifier = Modifier
                .width(55.dp)
                .height(18.dp)
        )

        Spacer(
            Modifier.height(12.dp)
        )

        // 월별 운동 횟수 및 시간
        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement =
                Arrangement.spacedBy(12.dp)
        ) {

            FitMateSkeleton(
                modifier = Modifier
                    .width(65.dp)
                    .height(38.dp)
            )

            FitMateSkeleton(
                modifier = Modifier
                    .width(145.dp)
                    .height(18.dp)
            )
        }

        Spacer(
            Modifier.height(30.dp)
        )

        // 운동 기록 목록 스켈레톤
        repeat(3) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(86.dp),
                horizontalArrangement =
                    Arrangement.spacedBy(12.dp)
            ) {

                Column(
                    horizontalAlignment =
                        Alignment.CenterHorizontally
                ) {

                    FitMateSkeleton(
                        modifier = Modifier
                            .padding(top = 5.dp)
                            .size(8.dp)
                    )

                    Spacer(
                        Modifier.height(3.5.dp)
                    )

                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(68.5.dp)
                            .background(Gray200)
                    )
                }

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    FitMateSkeleton(
                        modifier = Modifier
                            .width(85.dp)
                            .height(15.dp)
                    )

                    Spacer(
                        Modifier.height(8.dp)
                    )

                    FitMateSkeleton(
                        modifier = Modifier
                            .width(100.dp)
                            .height(20.dp)
                    )

                    Spacer(
                        Modifier.height(4.dp)
                    )

                    FitMateSkeleton(
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .height(14.dp)
                    )

                    Spacer(
                        Modifier.height(6.dp)
                    )

                    FitMateSkeleton(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .height(14.dp)
                    )
                }
            }

            Spacer(
                Modifier.height(26.dp)
            )
        }
    }
}

// ========================================
// 5. 월별 캘린더
// ========================================

@Composable
private fun CalendarCard(
    month: YearMonth,
    today: LocalDate,
    marked: Set<LocalDate>,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    onDateClick: (LocalDate) -> Unit
) {

    val canGoNext =
        month.isBefore(YearMonth.from(today))

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(14.dp),
        color = White,
        border = BorderStroke(
            1.dp,
            BorderDefault
        )
    ) {

        Column(
            modifier = Modifier.padding(
                start = 19.dp,
                top = 19.dp,
                end = 19.dp,
                bottom = 29.dp
            )
        ) {

            // 연월 및 이전/다음 버튼
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.SpaceBetween,
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                // 이전 달 아이콘
                Box(
                    modifier = Modifier
                        .size(
                            width = 35.dp,
                            height = 35.dp
                        )
                        .clickable {
                            onPrevious()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(
                            id = R.drawable.ic_record_chevron_left
                        ),
                        contentDescription = "이전 달",
                        tint = Color.Unspecified,
                        modifier = Modifier.size(
                            width = 10.dp,
                            height = 10.dp
                        )
                    )
                }

                Text(
                    text = "${month.year}년 ${month.monthValue}월",
                    style = recordStyle(
                        size = 17,
                        bold = true
                    ),
                    color = Color.Black
                )

                // 다음 달 아이콘
                Box(
                    modifier = Modifier
                        .size(
                            width = 35.dp,
                            height = 35.dp
                        )
                        .clickable(
                            enabled = canGoNext
                        ) {
                            onNext()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(
                            id = R.drawable.ic_record_chevron_right
                        ),
                        contentDescription = "다음 달",
                        tint = if (canGoNext) {
                            Color.Unspecified
                        } else {
                            Gray200
                        },
                        modifier = Modifier.size(
                            width = 10.dp,
                            height = 10.dp
                        )
                    )
                }
            }

            Spacer(
                Modifier.height(41.dp)
            )

            // 요일
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                listOf(
                    "일", "월", "화", "수",
                    "목", "금", "토"
                ).forEach { day ->

                    Text(
                        text = day,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        style = recordStyle(12),
                        color = Gray600
                    )
                }
            }

            Spacer(
                Modifier.height(21.dp)
            )

            // 해당 월 첫째 날의 요일
            val offset =
                month.atDay(1).dayOfWeek.value % 7

            // 캘린더 날짜 생성
            val cells =
                List<LocalDate?>(offset) { null } +
                        (1..month.lengthOfMonth()).map {
                            month.atDay(it)
                        }

            val padded = cells +
                    List(
                        (7 - cells.size % 7) % 7
                    ) { null }

            // 주 단위 배치
            padded.chunked(7).forEach { week ->

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(39.dp)
                ) {

                    week.forEach { date ->

                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .clickable(
                                    enabled = date != null
                                ) {
                                    date?.let(onDateClick)
                                },
                            horizontalAlignment =
                                Alignment.CenterHorizontally
                        ) {

                            // 오늘 날짜인지 확인
                            val isToday =
                                date != null && date == today

                            // 날짜 표시
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .background(
                                        color = if (isToday) {
                                            Blue600
                                        } else {
                                            Color.Transparent
                                        },
                                        shape = CircleShape
                                    ),
                                contentAlignment =
                                    Alignment.Center
                            ) {

                                if (date != null) {

                                    Text(
                                        text = "${date.dayOfMonth}",
                                        style = recordStyle(14),
                                        color = if (isToday) {
                                            White
                                        } else {
                                            Color.Black
                                        }
                                    )
                                }
                            }

                            // 실제 운동 완료 날짜 표시
                            if (
                                date != null &&
                                date in marked
                            ) {
                                Box(
                                    modifier = Modifier
                                        .padding(top = 2.dp)
                                        .size(5.dp)
                                        .background(
                                            Blue600,
                                            CircleShape
                                        )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
