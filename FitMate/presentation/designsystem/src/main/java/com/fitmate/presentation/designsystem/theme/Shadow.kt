package com.fitmate.presentation.designsystem.theme

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// FitMate는 그림자보다 보더와 배경 색상으로 계층을 표현한다.
// 그림자는 플로팅 요소, 모달 / 바텀 시트에만 사용한다.
data class FitMateShadow(
    val offsetY: Dp,
    val blur: Dp,
    val alpha: Float
)

val ShadowSmall = FitMateShadow(offsetY = 2.dp, blur = 8.dp, alpha = 0.08f)  // 플로팅 요소
val ShadowLarge = FitMateShadow(offsetY = 8.dp, blur = 24.dp, alpha = 0.12f) // 모달 / 바텀 시트

// Figma 드롭 섀도(Y, Blur, 투명도)를 그대로 그린다. API 28 미만에서는 그림자가 그려지지 않는다.
fun Modifier.fitMateShadow(shadow: FitMateShadow, cornerRadius: Dp) = drawBehind {
    drawIntoCanvas { canvas ->
        val paint = Paint()
        paint.asFrameworkPaint().apply {
            color = android.graphics.Color.WHITE
            setShadowLayer(
                shadow.blur.toPx() / 2,
                0f,
                shadow.offsetY.toPx(),
                Color.Black.copy(alpha = shadow.alpha).toArgb()
            )
        }
        val radius = cornerRadius.toPx()
        canvas.drawRoundRect(0f, 0f, size.width, size.height, radius, radius, paint)
    }
}
