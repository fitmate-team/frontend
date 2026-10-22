package com.fitmate.presentation.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fitmate.presentation.designsystem.R
import com.fitmate.presentation.designsystem.theme.Blue100
import com.fitmate.presentation.designsystem.theme.Blue500
import com.fitmate.presentation.designsystem.theme.BorderDefault
import com.fitmate.presentation.designsystem.theme.FitMateTheme
import com.fitmate.presentation.designsystem.theme.Gray100
import com.fitmate.presentation.designsystem.theme.Gray500
import com.fitmate.presentation.designsystem.theme.MinTouchTarget
import com.fitmate.presentation.designsystem.theme.Spacing4
import com.fitmate.presentation.designsystem.theme.Spacing5
import com.fitmate.presentation.designsystem.theme.Spacing6
import com.fitmate.presentation.designsystem.theme.TextPrimary
import com.fitmate.presentation.designsystem.theme.White
import com.fitmate.presentation.designsystem.theme.fitMateDisplayTextStyle
import com.fitmate.presentation.designsystem.theme.fitMateTextStyle

private val TopBarHeight = 80.dp
private val SearchTopBarHeight = 132.dp
private val TopBarDividerWidth = 1.3.dp
private val ProgressBarWidth = 270.dp
private val ProgressBarHeight = 4.dp
private val ProgressBarShape = RoundedCornerShape(2.dp)

// 타이틀이 있는 상단 바 (회원가입 / 오늘의 운동). action은 오른쪽 끝에 붙는다
// bottomContent가 있으면 바를 132dp로 늘리고 제목 아래(y=76)에 붙인다 (운동 설정 탭)
@Composable
fun FitMateTitleTopBar(
    title: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    action: (@Composable () -> Unit)? = null,
    bottomContent: (@Composable () -> Unit)? = null
) {
    TopBarContainer(
        dividerColor = BorderDefault,
        modifier = modifier,
        height = if (bottomContent != null) SearchTopBarHeight else TopBarHeight
    ) {
        BackArrowButton(onClick = onBack)
        Text(
            text = title,
            style = fitMateDisplayTextStyle(20.sp, 24.sp),
            color = TextPrimary,
            modifier = Modifier.offset(x = 56.dp, y = 27.dp)
        )
        if (action != null) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = Spacing5)
            ) { action() }
        }
        if (bottomContent != null) {
            Box(modifier = Modifier.offset(y = 76.dp)) { bottomContent() }
        }
    }
}

// 뒤로가기 없는 탭 화면 상단 바 (나의 루틴). action은 오른쪽 끝에 붙는다
@Composable
fun FitMateTabTopBar(
    title: String,
    modifier: Modifier = Modifier,
    action: (@Composable () -> Unit)? = null
) {
    TopBarContainer(dividerColor = Gray100, modifier = modifier) {
        Text(
            text = title,
            style = fitMateDisplayTextStyle(20.sp, 18.sp),
            color = TextPrimary,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = Spacing6)
        )
        if (action != null) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = Spacing4)
            ) { action() }
        }
    }
}

// 뒤로가기만 있는 상단 바 (워치 기록 동기화 결과)
@Composable
fun FitMateBackTopBar(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    showDivider: Boolean = true
) {
    TopBarContainer(dividerColor = if (showDivider) BorderDefault else null, modifier = modifier) {
        BackArrowButton(onClick = onBack)
    }
}

// 탭 화면 상단 바. 왼쪽에 FitMate 로고 텍스트
// 행간(18)이 글자 크기(28)보다 작아 y 오프셋으로 두면 로고가 아래로 밀린다. 디자인 기준 베이스라인(y=54)에 맞춘다
@Composable
fun FitMateLogoTopBar(modifier: Modifier = Modifier) {
    TopBarContainer(dividerColor = Gray100, modifier = modifier) {
        Text(
            text = "FitMate",
            style = fitMateDisplayTextStyle(28.sp, 18.sp),
            color = Blue500,
            modifier = Modifier
                .offset(x = 21.dp)
                .paddingFromBaseline(top = 54.dp)
        )
    }
}

// 회원가입 단계 진행률 상단 바 (1 / 10)
@Composable
fun ProgressTopBar(
    step: Int,
    totalSteps: Int,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopBarContainer(dividerColor = Gray100, modifier = modifier) {
        BackArrowButton(onClick = onBack)
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = 38.dp)
                .width(ProgressBarWidth)
                .height(ProgressBarHeight)
                .clip(ProgressBarShape)
                .background(Blue100)
        ) {
            Box(
                modifier = Modifier
                    .width(ProgressBarWidth * step / totalSteps)
                    .height(ProgressBarHeight)
                    .clip(ProgressBarShape)
                    .background(Blue500)
            )
        }
        // 진행률 바 오른쪽 끝에서 13dp 떨어진 위치
        Text(
            text = "$step / $totalSteps",
            style = fitMateTextStyle(12.sp, 18.sp, FontWeight.Bold, FontFamily.Default),
            color = Gray500,
            modifier = Modifier.offset(x = maxWidth / 2 + ProgressBarWidth / 2 + 13.dp, y = 31.dp)
        )
    }
}

// 운동 검색 상단 바: 뒤로가기 + 검색창, 아래쪽에 카테고리 칩 등을 넣는다
@Composable
fun SearchTopBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "운동 검색",
    bottomContent: @Composable () -> Unit = {}
) {
    TopBarContainer(dividerColor = BorderDefault, height = SearchTopBarHeight, modifier = modifier) {
        BackArrowButton(onClick = onBack)
        // 검색창은 x=54에서 시작해 오른쪽 32dp를 비운다
        FitMateSearchField(
            query = query,
            onQueryChange = onQueryChange,
            placeholder = placeholder,
            modifier = Modifier
                .offset(x = 54.dp, y = Spacing4)
                .width(maxWidth - 54.dp - 32.dp)
        )
        Box(modifier = Modifier.offset(y = 76.dp)) {
            bottomContent()
        }
    }
}

@Composable
private fun TopBarContainer(
    dividerColor: Color?,
    modifier: Modifier = Modifier,
    height: Dp = TopBarHeight,
    content: @Composable BoxWithConstraintsScope.() -> Unit
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .background(White)
            .drawBehind {
                if (dividerColor != null) {
                    val y = size.height - TopBarDividerWidth.toPx() / 2
                    drawLine(dividerColor, Offset(0f, y), Offset(size.width, y), TopBarDividerWidth.toPx())
                }
            },
        content = content
    )
}

// 화살표(13.2 x 20.2)가 x=21.8, y=29.9에 오도록 48dp 터치 영역을 배치한다
@Composable
private fun BackArrowButton(onClick: () -> Unit) {
    val arrowWidth = 13.2457.dp
    val arrowHeight = 20.2299.dp
    Box(
        modifier = Modifier
            .offset(
                x = 21.76.dp - (MinTouchTarget - arrowWidth) / 2,
                y = 29.89.dp - (MinTouchTarget - arrowHeight) / 2
            )
            .size(MinTouchTarget)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_arrow_left),
            contentDescription = "뒤로",
            tint = TextPrimary,
            modifier = Modifier.size(arrowWidth, arrowHeight)
        )
    }
}

@Preview
@Composable
private fun FitMateTopBarPreview() {
    FitMateTheme {
        Column(verticalArrangement = Arrangement.spacedBy(Spacing4)) {
            FitMateTitleTopBar(title = "회원가입", onBack = {})
            ProgressTopBar(step = 3, totalSteps = 10, onBack = {})
            SearchTopBar(query = "", onQueryChange = {}, onBack = {})
            FitMateLogoTopBar()
        }
    }
}
