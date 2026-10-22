package com.fitmate.presentation.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.fitmate.presentation.designsystem.R

// ========================================
// 1. FitMate 기본 폰트 - 감탄로드 돋움체
// ========================================

val FitMateFontFamily = FontFamily(
    Font(
        resId = R.font.gamtanroad_thin,
        weight = FontWeight.Thin
    ),
    Font(
        resId = R.font.gamtanroad_regular,
        weight = FontWeight.Normal
    ),
    Font(
        resId = R.font.gamtanroad_bold,
        weight = FontWeight.Bold
    )
)

// ========================================
// 2. FitMate 숫자 폰트 - 웨이브 파도체
// ========================================

val FitMateNumberFontFamily = FontFamily(
    Font(
        resId = R.font.wavve_pado_regular,
        weight = FontWeight.Normal
    )
)

// Wavve PADO는 Regular 한 굵기뿐이다 (획이 두꺼워 Normal로 충분하다)
val FitMateNumberFontWeight: FontWeight = FontWeight.Normal

// ========================================
// 3. 공통 TextStyle 생성 함수
// ========================================

fun fitMateTextStyle(
    fontSize: TextUnit,
    lineHeight: TextUnit,
    fontWeight: FontWeight = FontWeight.Normal,
    fontFamily: FontFamily = FitMateFontFamily,
    letterSpacing: TextUnit = 0.sp
) = TextStyle(
    fontFamily = fontFamily,
    fontWeight = fontWeight,
    fontSize = fontSize,
    lineHeight = lineHeight,
    letterSpacing = letterSpacing
)

// Wavve PADO 텍스트 (화면 타이틀, 버튼 라벨)
fun fitMateDisplayTextStyle(
    fontSize: TextUnit,
    lineHeight: TextUnit,
    letterSpacing: TextUnit = 0.sp
) = fitMateTextStyle(
    fontSize = fontSize,
    lineHeight = lineHeight,
    fontWeight = FitMateNumberFontWeight,
    fontFamily = FitMateNumberFontFamily,
    letterSpacing = letterSpacing
)

// ========================================
// 4. FitMate Typography
// ========================================

val FitMateTypography = Typography(

    // 큰 제목
    headlineLarge = fitMateTextStyle(
        fontSize = 32.sp,
        lineHeight = 40.sp,
        fontWeight = FontWeight.Bold
    ),

    headlineMedium = fitMateTextStyle(
        fontSize = 28.sp,
        lineHeight = 36.sp,
        fontWeight = FontWeight.Bold
    ),

    headlineSmall = fitMateTextStyle(
        fontSize = 24.sp,
        lineHeight = 32.sp,
        fontWeight = FontWeight.Bold
    ),

    // 제목
    titleLarge = fitMateTextStyle(
        fontSize = 22.sp,
        lineHeight = 28.sp,
        fontWeight = FontWeight.Bold
    ),

    titleMedium = fitMateTextStyle(
        fontSize = 16.sp,
        lineHeight = 24.sp,
        fontWeight = FontWeight.Bold
    ),

    titleSmall = fitMateTextStyle(
        fontSize = 14.sp,
        lineHeight = 20.sp,
        fontWeight = FontWeight.Bold
    ),

    // 본문
    bodyLarge = fitMateTextStyle(
        fontSize = 16.sp,
        lineHeight = 24.sp,
        fontWeight = FontWeight.Normal
    ),

    bodyMedium = fitMateTextStyle(
        fontSize = 14.sp,
        lineHeight = 20.sp,
        fontWeight = FontWeight.Normal
    ),

    bodySmall = fitMateTextStyle(
        fontSize = 12.sp,
        lineHeight = 16.sp,
        fontWeight = FontWeight.Normal
    ),

    // 버튼 및 라벨
    labelLarge = fitMateTextStyle(
        fontSize = 14.sp,
        lineHeight = 20.sp,
        fontWeight = FontWeight.Bold
    ),

    labelMedium = fitMateTextStyle(
        fontSize = 12.sp,
        lineHeight = 16.sp,
        fontWeight = FontWeight.Normal
    ),

    labelSmall = fitMateTextStyle(
        fontSize = 11.sp,
        lineHeight = 16.sp,
        fontWeight = FontWeight.Normal
    )
)