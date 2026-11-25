package com.fitmate.presentation.designsystem.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

@Composable
fun FitMateSkeleton(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(8.dp)
) {
    val transition = rememberInfiniteTransition(
        label = "SkeletonTransition"
    )

    val translateAnimation by transition.animateFloat(
        initialValue = -500f,
        targetValue = 1500f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1500,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "SkeletonShimmer"
    )

    val brush = Brush.linearGradient(
        colors = listOf(
            Color(0xFFE9EDF2),
            Color(0xFFF7F9FC),
            Color(0xFFE9EDF2)
        ),
        start = Offset(
            translateAnimation,
            translateAnimation
        ),
        end = Offset(
            translateAnimation + 500f,
            translateAnimation + 500f
        )
    )

    Box(
        modifier = modifier.background(
            brush = brush,
            shape = shape
        )
    )
}