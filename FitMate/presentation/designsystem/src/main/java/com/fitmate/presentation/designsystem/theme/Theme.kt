package com.fitmate.presentation.designsystem.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val FitMateColorScheme = lightColorScheme(
    primary = BgBrand,
    onPrimary = TextOnBrand,
    background = BgPrimary,
    onBackground = TextPrimary,
    surface = White,
    onSurface = TextPrimary,
    onSurfaceVariant = TextSecondary,
    outline = BorderDefault,
    error = BgDanger,
    onError = TextOnDanger
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
