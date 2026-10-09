package com.fitmate.presentation.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

// 컴포넌트 폰트 (GamtanRoad Dotum). 폰트 파일 추가 전까지 기본 폰트 사용
val FitMateFontFamily: FontFamily = FontFamily.Default

// 숫자 강조 폰트 (Wavve PADO). 폰트 파일 추가 전까지 기본 폰트 사용
val FitMateNumberFontFamily: FontFamily = FontFamily.Default

internal fun fitMateTextStyle(
    fontSize: TextUnit,
    lineHeight: TextUnit,
    fontWeight: FontWeight = FontWeight.Normal,
    fontFamily: FontFamily = FitMateFontFamily
) = TextStyle(
    fontFamily = fontFamily,
    fontWeight = fontWeight,
    fontSize = fontSize,
    lineHeight = lineHeight,
    letterSpacing = 0.sp
)

// Roboto = Android 기본 폰트
val FitMateTypography = Typography(
    headlineLarge = fitMateTextStyle(32.sp, 40.sp, FontWeight.Normal, FontFamily.Default),
    headlineMedium = fitMateTextStyle(28.sp, 36.sp, FontWeight.Normal, FontFamily.Default),
    headlineSmall = fitMateTextStyle(24.sp, 32.sp, FontWeight.Normal, FontFamily.Default),
    titleLarge = fitMateTextStyle(22.sp, 28.sp, FontWeight.Normal, FontFamily.Default),
    titleMedium = fitMateTextStyle(16.sp, 24.sp, FontWeight.Medium, FontFamily.Default),
    titleSmall = fitMateTextStyle(14.sp, 20.sp, FontWeight.Medium, FontFamily.Default),
    bodyLarge = fitMateTextStyle(16.sp, 24.sp, FontWeight.Normal, FontFamily.Default),
    bodyMedium = fitMateTextStyle(14.sp, 20.sp, FontWeight.Normal, FontFamily.Default),
    bodySmall = fitMateTextStyle(12.sp, 16.sp, FontWeight.Normal, FontFamily.Default),
    labelLarge = fitMateTextStyle(14.sp, 20.sp, FontWeight.Medium, FontFamily.Default),
    labelMedium = fitMateTextStyle(12.sp, 16.sp, FontWeight.Medium, FontFamily.Default),
    labelSmall = fitMateTextStyle(11.sp, 16.sp, FontWeight.Medium, FontFamily.Default)
)
