package com.fitmate.presentation.workout

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fitmate.presentation.designsystem.R
import com.fitmate.presentation.designsystem.theme.*

/** 서버의 다음 운동 제안 결과를 화면에 전달하기 위한 UI 모델. */
data class NextWorkoutAdjustmentItem(
    val id: Int,
    val name: String,
    val current: String,
    val suggested: String,
    val category: String,
    val reason: String
)

/**
 * 다음 운동 조정 화면.
 *
 * 기본 상태: 모든 운동의 '적용' 버튼 선택
 * 그대로 유지: 해당 운동의 AI 제안을 적용하지 않음
 * 적용: 해당 운동의 AI 제안을 적용함
 */
@Composable
fun NextWorkoutAdjustmentScreen(
    items: List<NextWorkoutAdjustmentItem>,
    onBack: () -> Unit,
    onApplySelected: (List<NextWorkoutAdjustmentItem>) -> Unit,
    onGoHome: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    // true = 적용 선택 / false = 그대로 유지 선택
    val selectedIds = remember(items) {
        mutableStateMapOf<Int, Boolean>().apply {
            items.forEach { put(it.id, true) }
        }
    }

    val blue = Color(0xFF2793F4)
    val blueDark = Color(0xFF07518A)
    val paleBlue = Color(0xFFE1F2FF)
    val outline = Color(0xFFDFE5EB)

    // 두 선택 버튼이 공통으로 사용하는 색상
    val selectedButtonColor = Color(0xFF2D97F4)
    val unselectedButtonColor = Color.White
    val unselectedTextColor = Gray500

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        // ─────────────────────────────
        // 1. 상단 헤더
        // ─────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .padding(horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_workout_back),
                contentDescription = "뒤로가기",
                tint = Color.Unspecified,
                modifier = Modifier
                    .size(width = 10.dp, height = 18.dp)
                    .clickable(onClick = onBack)
            )

            Spacer(Modifier.width(22.dp))

            Text(
                text = "다음 운동 조정",
                fontFamily = FitMateNumberFontFamily,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Gray700
            )
        }

        HorizontalDivider(
            thickness = 1.dp,
            color = outline
        )

        // ─────────────────────────────
        // 2. 스크롤 본문
        // ─────────────────────────────
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
                .padding(top = 19.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // ─────────────────────────────
            // 3. AI 로봇 안내 영역
            // ─────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 101.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Image(
                    painter = painterResource(
                        R.drawable.img_fitmate_robot
                    ),
                    contentDescription = "FitMate 로봇",
                    modifier = Modifier.size(
                        width = 100.dp,
                        height = 101.dp
                    )
                )

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .border(
                            width = 1.dp,
                            color = blue,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .background(
                            color = paleBlue,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(
                            horizontal = 20.dp,
                            vertical = 16.dp
                        )
                ) {
                    Text(
                        text = "오늘의 수행 기록과 세션 난이도를 바탕으로\n다음 추천 운동을 조정했어요.",
                        fontFamily = FitMateFontFamily,
                        fontSize = 12.sp,
                        lineHeight = 18.sp,
                        color = Gray700
                    )
                }
            }

            // ─────────────────────────────
            // 4. 운동별 AI 제안 카드
            // ─────────────────────────────
            items.forEach { item ->

                val isSelected = selectedIds[item.id] == true

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = 1.dp,
                            color = outline,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(
                            horizontal = 16.dp,
                            vertical = 16.dp
                        )
                ) {

                    // 운동 이름 + 카테고리
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = item.name,
                            modifier = Modifier.weight(1f),
                            fontFamily = FitMateFontFamily,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Gray900
                        )

                        Box(
                            modifier = Modifier
                                .border(
                                    width = 1.dp,
                                    color = outline,
                                    shape = RoundedCornerShape(30.dp)
                                )
                                .background(
                                    color = Color(0xFFF8FAFC),
                                    shape = RoundedCornerShape(30.dp)
                                )
                                .padding(
                                    horizontal = 12.dp,
                                    vertical = 4.dp
                                )
                        ) {
                            Text(
                                text = item.category,
                                fontFamily = FitMateFontFamily,
                                fontSize = 11.sp,
                                color = Gray500
                            )
                        }
                    }

                    Spacer(Modifier.height(15.dp))

                    // ─────────────────────────────
                    // 현재 운동 ↔ AI 제안
                    // ─────────────────────────────
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        AdjustmentValueBox(
                            title = "현재",
                            value = item.current,
                            background = Color(0xFFF6F7F9),
                            border = null,
                            textColor = Gray700,
                            modifier = Modifier.weight(1f)
                        )

                        // 가운데 화살표
                        Canvas(
                            modifier = Modifier
                                .padding(horizontal = 14.dp)
                                .size(18.dp)
                        ) {
                            val y = size.height / 2

                            val path = Path().apply {
                                moveTo(size.width * 0.1f, y)
                                lineTo(size.width * 0.9f, y)

                                moveTo(
                                    size.width * 0.58f,
                                    size.height * 0.2f
                                )
                                lineTo(size.width * 0.9f, y)
                                lineTo(
                                    size.width * 0.58f,
                                    size.height * 0.8f
                                )
                            }

                            drawPath(
                                path = path,
                                color = Color(0xFF343C45),
                                style = Stroke(
                                    width = 2.5.dp.toPx()
                                )
                            )
                        }

                        AdjustmentValueBox(
                            title = "AI 제안",
                            value = item.suggested,
                            background = paleBlue,
                            border = Color(0xFF91CFFF),
                            textColor = blueDark,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(Modifier.height(13.dp))

                    // AI 운동 조정 이유
                    Text(
                        text = item.reason,
                        fontFamily = FitMateFontFamily,
                        fontSize = 13.sp,
                        lineHeight = 18.sp,
                        color = Gray700
                    )

                    Spacer(Modifier.height(20.dp))

                    // ─────────────────────────────
                    // 5. 그대로 유지 / 적용 버튼
                    //
                    // 선택된 버튼은 둘 다 동일한 디자인
                    // 파란 배경 + 흰색 글씨
                    // ─────────────────────────────
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(9.dp)
                    ) {

                        // 그대로 유지 버튼
                        Button(
                            onClick = {
                                selectedIds[item.id] = false
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(38.dp),
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(0.dp),

                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (!isSelected) {
                                    selectedButtonColor
                                } else {
                                    unselectedButtonColor
                                }
                            ),

                            border = BorderStroke(
                                width = 1.dp,
                                color = if (!isSelected) {
                                    selectedButtonColor
                                } else {
                                    outline
                                }
                            )
                        ) {
                            Text(
                                text = "그대로 유지",
                                fontFamily = FitMateFontFamily,
                                fontSize = 12.sp,
                                fontWeight = if (!isSelected) {
                                    FontWeight.Bold
                                } else {
                                    FontWeight.Normal
                                },
                                color = if (!isSelected) {
                                    Color.White
                                } else {
                                    unselectedTextColor
                                }
                            )
                        }

                        // 적용 버튼
                        Button(
                            onClick = {
                                selectedIds[item.id] = true
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(38.dp),
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(0.dp),

                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isSelected) {
                                    selectedButtonColor
                                } else {
                                    unselectedButtonColor
                                }
                            ),

                            border = BorderStroke(
                                width = 1.dp,
                                color = if (isSelected) {
                                    selectedButtonColor
                                } else {
                                    outline
                                }
                            )
                        ) {
                            Text(
                                text = "적용",
                                fontFamily = FitMateFontFamily,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) {
                                    FontWeight.Bold
                                } else {
                                    FontWeight.Normal
                                },
                                color = if (isSelected) {
                                    Color.White
                                } else {
                                    unselectedTextColor
                                }
                            )
                        }
                    }
                }
            }

            // ─────────────────────────────
            // 6. 하단 안내 박스
            // ─────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = paleBlue,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(
                        start = 12.dp,
                        end = 12.dp,
                        top = 12.dp,
                        bottom = 14.dp
                    ),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                // 이전 화면과 동일한 느낌표
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .background(
                            color = Blue600,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "!",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontFamily = FitMateFontFamily,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 12.sp,
                        modifier = Modifier.offset(y = (-0.5).dp)
                    )
                }

                Text(
                    text = "운동 기록이 쌓일수록 FitMate가 회원님에게 더 잘 맞는\n운동을 제안할 수 있어요.",
                    modifier = Modifier.weight(1f),
                    fontFamily = FitMateFontFamily,
                    fontSize = 13.sp,
                    lineHeight = 18.sp,
                    color = Color(0xFF0067C3)
                )
            }
        }

        // ─────────────────────────────
        // 7. 하단 고정 버튼
        // ─────────────────────────────
        Button(
            onClick = {
                // '적용'을 선택한 운동만 전달
                onApplySelected(
                    items.filter { selectedIds[it.id] == true }
                )

                // 홈 화면으로 이동
                onGoHome()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(top = 14.dp, bottom = 14.dp)
                .height(52.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF0879E9)
            )
        ) {
            Text(
                text = "선택한 변경사항 적용",
                fontFamily = FitMateNumberFontFamily,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

/**
 * 현재 운동 / AI 제안 정보 박스
 */
@Composable
private fun AdjustmentValueBox(
    title: String,
    value: String,
    background: Color,
    border: Color?,
    textColor: Color,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(10.dp)

    Column(
        modifier = modifier
            .heightIn(min = 64.dp)
            .then(
                if (border != null) {
                    Modifier.border(
                        width = 1.dp,
                        color = border,
                        shape = shape
                    )
                } else {
                    Modifier
                }
            )
            .background(
                color = background,
                shape = shape
            )
            .padding(
                start = 12.dp,
                end = 16.dp,
                top = 10.dp,
                bottom = 11.dp
            ),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = title,
            fontFamily = FitMateFontFamily,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = textColor
        )

        Text(
            text = value,
            fontFamily = FitMateNumberFontFamily,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = textColor,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}