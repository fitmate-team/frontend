
package com.fitmate.presentation.mypage

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fitmate.presentation.designsystem.R
import com.fitmate.presentation.designsystem.theme.*

// ========================================
// 1. 마이페이지 메뉴 데이터
// ========================================

private data class MyPageMenu(
    val title: String,
    val iconRes: Int
)

private val myPageMenus = listOf(
    MyPageMenu(
        title = "운동 프로필",
        iconRes = R.drawable.ic_mypage_profile
    ),
    MyPageMenu(
        title = "내 운동 환경",
        iconRes = R.drawable.ic_mypage_location
    ),
    MyPageMenu(
        title = "앱 설정",
        iconRes = R.drawable.ic_mypage_app_settings
    ),
    MyPageMenu(
        title = "AI Trainer 설정",
        iconRes = R.drawable.ic_mypage_ai_trainer
    )
)

// ========================================
// 2. 마이페이지 메인 화면
// ========================================

@Composable
fun MyPageScreen(
    onOpenMenu: (String) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
            .verticalScroll(rememberScrollState())
    ) {

        // ========================================
        // 상단 파란 배경 + 프로필 카드
        // ========================================

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(230.dp)
        ) {

            // 파란 배경
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(178.dp)
                    .clip(
                        RoundedCornerShape(
                            topStart = 0.dp,
                            topEnd = 0.dp,
                            bottomStart = 24.dp,
                            bottomEnd = 24.dp
                        )
                    )
                    .background(Blue500)
            ) {
                // 배경 장식용 원형 곡선
                // 피그마 상단 곡선과 유사한 느낌
                androidx.compose.foundation.Canvas(
                    modifier = Modifier.fillMaxSize()
                ) {
                    drawCircle(
                        color = Color.White.copy(alpha = 0.65f),
                        radius = 150.dp.toPx(),
                        center = androidx.compose.ui.geometry.Offset(
                            x = size.width * 0.77f,
                            y = size.height * 0.46f
                        ),
                        style = androidx.compose.ui.graphics.drawscope.Stroke(
                            width = 1.dp.toPx()
                        )
                    )
                }

                Text(
                    text = "마이페이지",
                    modifier = Modifier.padding(
                        start = 24.dp,
                        top = 24.dp
                    ),
                    fontFamily = FitMateNumberFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 20.sp,
                    color = White
                )
            }

            // 프로필 정보 카드
            Surface(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(horizontal = 37.dp),
                shape = RoundedCornerShape(20.dp),
                color = White,
                border = BorderStroke(
                    1.dp,
                    BorderDefault
                )
            ) {
                Column(
                    modifier = Modifier.padding(
                        horizontal = 24.dp,
                        vertical = 24.dp
                    )
                ) {

                    // 닉네임 + 운동 수준
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "fitmate0901",
                            fontFamily = FitMateNumberFontFamily,
                            fontWeight = FontWeight.Normal,
                            fontSize = 20.sp,
                            color = Blue700
                        )

                        Spacer(Modifier.width(10.dp))

                        Text(
                            text = "중급 · 주 4회",
                            fontFamily = FitMateFontFamily,
                            fontSize = 12.sp,
                            color = Gray500
                        )
                    }

                    Spacer(Modifier.height(27.dp))

                    // 목표 / 운동 방식 / 운동 장소
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement =
                            Arrangement.SpaceBetween
                    ) {
                        ProfileInfoItem(
                            value = "체지방 감량",
                            label = "목표",
                            modifier = Modifier.weight(1f)
                        )

                        ProfileInfoItem(
                            value = "균형형",
                            label = "운동 방식",
                            modifier = Modifier.weight(1f)
                        )

                        ProfileInfoItem(
                            value = "Fit Gym",
                            label = "운동 장소",
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        // ========================================
        // 내 정보 · 설정
        // ========================================

        Spacer(Modifier.height(23.dp))

        Column(
            modifier = Modifier.padding(
                horizontal = 24.dp
            )
        ) {

            Text(
                text = "내 정보 · 설정",
                fontFamily = FitMateFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Gray900
            )

            Spacer(Modifier.height(12.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                color = White,
                border = BorderStroke(
                    width = 1.dp,
                    color = BorderDefault
                )
            ) {
                Column() {
                    myPageMenus.forEach { menu ->
                        MyPageMenuItem(
                            menu = menu,
                            onClick = {
                                onOpenMenu(menu.title)
                            }
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(30.dp))
    }
}

// ========================================
// 3. 프로필 정보 아이템
// ========================================

@Composable
private fun ProfileInfoItem(
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            fontFamily = FitMateFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = Gray900,
            maxLines = 1,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(6.dp))

        Text(
            text = label,
            fontFamily = FitMateFontFamily,
            fontSize = 14.sp,
            color = Gray500,
            textAlign = TextAlign.Center
        )
    }
}

// ========================================
// 4. 마이페이지 메뉴 아이템
// ========================================

@Composable
private fun MyPageMenuItem(
    menu: MyPageMenu,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(
                horizontal = 14.dp,
                vertical = 16.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        // 왼쪽 아이콘 배경
        Box(
            modifier = Modifier
                .size(30.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Gray50),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(
                    id = menu.iconRes
                ),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(12.dp)
            )
        }

        Spacer(Modifier.width(12.dp))

        Text(
            text = menu.title,
            modifier = Modifier.weight(1f),
            fontFamily = FitMateFontFamily,
            fontSize = 14.sp,
            color = Gray900
        )

        Icon(
            painter = painterResource(
                id = R.drawable.ic_mypage_arrow_right
            ),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.size(12.dp)
        )
    }
}


@Preview(
    showBackground = true,
    showSystemUi = false,
    name = "FitMate 마이페이지"
)
@Composable
private fun MyPageScreenPreview() {
    FitMateTheme {
        MyPageScreen()
    }
}