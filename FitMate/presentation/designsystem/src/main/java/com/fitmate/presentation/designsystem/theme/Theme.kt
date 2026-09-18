package com.fitmate.presentation.designsystem.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val FitMateColorScheme = lightColorScheme(
    primary = Black,
    background = White,
    surface = White,
    onPrimary = White,
    onBackground = Black,
    onSurface = Black
)

@Composable
fun FitMateTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = FitMateColorScheme,
        typography = FitMateTypography,
        content = content
    )
}