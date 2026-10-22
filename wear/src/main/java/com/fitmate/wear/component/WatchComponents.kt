package com.fitmate.wear.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.sp
import com.fitmate.presentation.designsystem.theme.Blue600
import com.fitmate.presentation.designsystem.theme.Gray900
import com.fitmate.presentation.designsystem.theme.White
import com.fitmate.presentation.designsystem.theme.fitMateTextStyle

// Figma 워치 화면의 원형 콘텐츠 영역 지름
val WatchDesignSize = 304.dp

// 워치 테두리선 두께 (칩 / 진행 점 / 보조 버튼)
val WatchBorderWidth = 1.332.dp

// 워치 화면은 Roboto (기본 폰트)
fun watchTextStyle(
    fontSize: TextUnit,
    lineHeight: TextUnit,
    fontWeight: FontWeight = FontWeight.Normal,
    letterSpacing: TextUnit = 0.sp
): TextStyle = fitMateTextStyle(fontSize, lineHeight, fontWeight, FontFamily.Default, letterSpacing)

// 워치 화면 배경. 304dp 디자인을 실제 화면 크기에 맞춰 비율 그대로 확대 / 축소한다
@Composable
fun WatchScreen(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(Gray900),
        contentAlignment = Alignment.Center
    ) {
        val density = LocalDensity.current
        val scale = min(maxWidth, maxHeight) / WatchDesignSize
        CompositionLocalProvider(
            LocalDensity provides Density(density.density * scale, density.fontScale)
        ) {
            Box(
                modifier = Modifier.size(WatchDesignSize),
                contentAlignment = Alignment.Center,
                content = content
            )
        }
    }
}

// 흰 테두리 알약 칩 (5종목 / SET 2 / 4 / 휴대폰과 동기화 완료)
@Composable
fun WatchOutlinedChip(
    text: String,
    modifier: Modifier = Modifier,
    height: Dp = 20.dp,
    horizontalPadding: Dp = 8.dp,
    borderWidth: Dp = WatchBorderWidth,
    textStyle: TextStyle = watchTextStyle(12.sp, 15.sp)
) {
    Box(
        modifier = modifier
            .height(height)
            .border(borderWidth, White, CircleShape)
            .padding(horizontal = horizontalPadding),
        contentAlignment = Alignment.Center
    ) {
        Text(text = text, style = textStyle, color = White)
    }
}

// 원형 주요 버튼 (시작 / 완료)
@Composable
fun WatchCircleButton(
    text: String,
    onClick: () -> Unit,
    size: Dp,
    modifier: Modifier = Modifier
) {
    WatchButton(
        text = text,
        onClick = onClick,
        shape = CircleShape,
        containerColor = Blue600,
        textStyle = watchTextStyle(16.sp, 24.sp, FontWeight.Medium),
        modifier = modifier.size(size)
    )
}

// 알약 모양 버튼. outlined면 배경 없이 흰 테두리 (+30)
@Composable
fun WatchPillButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = Blue600,
    contentColor: Color = White,
    outlined: Boolean = false,
    textStyle: TextStyle = watchTextStyle(16.sp, 24.sp, FontWeight.Medium)
) {
    WatchButton(
        text = text,
        onClick = onClick,
        shape = CircleShape,
        containerColor = if (outlined) Color.Transparent else containerColor,
        contentColor = contentColor,
        borderColor = if (outlined) White else null,
        textStyle = textStyle,
        modifier = modifier
    )
}

@Composable
private fun WatchButton(
    text: String,
    onClick: () -> Unit,
    shape: Shape,
    containerColor: Color,
    textStyle: TextStyle,
    modifier: Modifier = Modifier,
    contentColor: Color = White,
    borderColor: Color? = null
) {
    Box(
        modifier = modifier
            .clip(shape)
            .background(containerColor)
            .then(if (borderColor != null) Modifier.border(WatchBorderWidth, borderColor, shape) else Modifier)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(text = text, style = textStyle, color = contentColor, textAlign = TextAlign.Center)
    }
}

// 운동 종목 진행 점. 완료한 종목은 채워진 점
@Composable
fun WatchProgressDots(
    total: Int,
    completed: Int,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        repeat(total) { index ->
            Box(
                modifier = Modifier
                    .size(5.dp)
                    .clip(CircleShape)
                    .background(if (index < completed) White else Color.Transparent)
                    .border(WatchBorderWidth, White, CircleShape)
            )
        }
    }
}

// 값 + 라벨 통계 (운동 끝 화면의 52분 / 시간)
@Composable
fun WatchStat(value: String, label: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, style = watchTextStyle(15.sp, 22.5.sp, FontWeight.Medium), color = White)
        Text(text = label, style = watchTextStyle(13.sp, 16.5.sp), color = White)
    }
}

internal val WatchCardShape = RoundedCornerShape(34.dp)

@Preview(widthDp = 304, heightDp = 304)
@Composable
private fun WatchComponentsPreview() {
    WatchScreen {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            WatchOutlinedChip(text = "5종목")
            WatchProgressDots(total = 5, completed = 2)
            WatchCircleButton(text = "시작", onClick = {}, size = 72.dp)
            WatchPillButton(text = "+30", onClick = {}, outlined = true, modifier = Modifier.size(56.dp, 32.dp))
            WatchStat(value = "52분", label = "시간")
        }
    }
}
