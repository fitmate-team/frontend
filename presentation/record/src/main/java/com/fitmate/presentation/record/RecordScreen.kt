package com.fitmate.presentation.record

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import com.fitmate.presentation.designsystem.theme.*
import java.time.LocalDate
import java.time.YearMonth
import com.fitmate.presentation.designsystem.R

// ========================================
// 1. 운동 기록 더미 데이터
// ========================================

private data class WorkoutEntry(
    val date: LocalDate,
    val title: String,
    val minutes: Int,
    val completion: Int,
    val heartRate: Int,
    val exercises: String
)

private val sampleWorkouts = listOf(
    WorkoutEntry(
        LocalDate.of(2026, 9, 3),
        "상체 PUSH", 55, 92, 128,
        "벤치프레스 · 숄더프레스 · 체스트프레스"
    ),
    WorkoutEntry(
        LocalDate.of(2026, 10, 8),
        "하체", 60, 100, 135,
        "스쿼트 · 레그프레스 · 레그컬"
    ),
    WorkoutEntry(
        LocalDate.of(2026, 10, 12),
        "상체 PULL", 48, 88, 124,
        "랫풀다운 · 시티드로우 · 바벨로우"
    ),
    WorkoutEntry(
        LocalDate.of(2026, 10, 17),
        "상체 PUSH", 52, 95, 130,
        "벤치프레스 · 숄더프레스"
    ),
    WorkoutEntry(
        LocalDate.of(2026, 10, 20),
        "유산소", 40, 100, 142,
        "러닝머신 · 사이클"
    ),
    WorkoutEntry(
        LocalDate.of(2026, 10, 24),
        "하체", 65, 90, 133,
        "스쿼트 · 런지 · 레그컬"
    ),
    WorkoutEntry(
        LocalDate.of(2026, 10, 27),
        "상체 PULL", 50, 94, 127,
        "랫풀다운 · 데드리프트"
    ),
    WorkoutEntry(
        LocalDate.of(2026, 10, 30),
        "전신", 58, 98, 136,
        "스쿼트 · 벤치프레스 · 플랭크"
    )
)

// ========================================
// 2. 공통 텍스트 스타일
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
// 3. 월별 기록 / 성장 분석 탭
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
// 4. 기록 메인 화면
// ========================================

@Composable
fun RecordScreen(
    onOpenWorkoutDetail: (String) -> Unit = {}
) {
    var tab by remember {
        mutableIntStateOf(0)
    }

    // 오늘 날짜 기준으로 캘린더 시작
    val today = remember {
        LocalDate.now()
    }

    var month by remember {
        mutableStateOf(YearMonth.from(today))
    }

    // 날짜 필터링용
    // 파란 동그라미 표시와는 별개
    var chosen by remember {
        mutableStateOf<LocalDate?>(null)
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
            GrowthAnalysisContent(
                Modifier.weight(1f)
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

                // 캘린더
                CalendarCard(
                    month = month,
                    today = today,
                    marked = sampleWorkouts
                        .map { it.date }
                        .toSet(),

                    onPrevious = {
                        month = month.minusMonths(1)
                        chosen = null
                    },

                    onNext = {
                        month = month.plusMonths(1)
                        chosen = null
                    },

                    onDateClick = { date ->
                        // 날짜 클릭 시 기록 필터링
                        // 파란 동그라미는 변경하지 않음
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

                // 현재 보고 있는 달의 기록
                val monthly = sampleWorkouts.filter {
                    YearMonth.from(it.date) == month
                }

                Text(
                    text = "이번 달",
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
                        text = "${monthly.size}회",
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

                    val totalMinutes = monthly.sumOf {
                        it.minutes
                    }

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

                // 날짜별 운동 기록 필터링
                val filtered = monthly
                    .filter {
                        chosen == null ||
                                it.date == chosen
                    }
                    .sortedByDescending {
                        it.date
                    }

                if (filtered.isEmpty()) {
                    Text(
                        text = "선택한 날짜에 운동 기록이 없어요.",
                        style = recordStyle(13),
                        color = Gray500,
                        modifier = Modifier.padding(
                            vertical = 24.dp
                        )
                    )
                }

                // 운동 기록 아이템
                filtered.forEach { workout ->

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(86.dp)
                            .clickable {
                                onOpenWorkoutDetail(
                                    "${workout.date.monthValue}월 ${workout.date.dayOfMonth}일 운동"
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

                            // 동그라미와 선 간격
                            Spacer(
                                Modifier.height(3.5.dp)
                            )

                            // 마지막 아이템도 선 표시
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

                            Text(
                                text = "${workout.date.monthValue}월 ${workout.date.dayOfMonth}일",
                                style = recordStyle(13),
                                color = Gray600
                            )

                            Spacer(
                                Modifier.height(8.dp)
                            )

                            Text(
                                text = workout.title,
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

                            Text(
                                text = "${workout.minutes}분 · 수행률 ${workout.completion}% · 평균 심박 ${workout.heartRate}bpm",
                                style = recordStyle(12),
                                color = Gray600
                            )

                            Spacer(
                                Modifier.height(6.dp)
                            )

                            Text(
                                text = workout.exercises,
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
                            contentDescription = "운동 기록 상세 보기",
                            tint = Color.Unspecified,
                            modifier = Modifier.size(10.dp)
                        )
                    }

                    Spacer(
                        Modifier.height(26.dp)
                    )
                }

                Spacer(
                    Modifier.height(23.dp)
                )
            }
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
                verticalAlignment = Alignment.CenterVertically
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
                        .clickable {
                            onNext()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(
                            id = R.drawable.ic_record_chevron_right
                        ),
                        contentDescription = "다음 달",
                        tint = Color.Unspecified,
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
                                contentAlignment = Alignment.Center
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

                            // 운동 완료 날짜 표시
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
