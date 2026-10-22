package com.fitmate.presentation.auth

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Psychology
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fitmate.presentation.designsystem.R
import com.fitmate.presentation.designsystem.component.FitMatePrimaryButton
import com.fitmate.presentation.designsystem.theme.Blue100
import com.fitmate.presentation.designsystem.theme.Blue200
import com.fitmate.presentation.designsystem.theme.Blue300
import com.fitmate.presentation.designsystem.theme.Blue400
import com.fitmate.presentation.designsystem.theme.Blue500
import com.fitmate.presentation.designsystem.theme.BorderDefault
import com.fitmate.presentation.designsystem.theme.FitMateTheme
import com.fitmate.presentation.designsystem.theme.Gray700
import com.fitmate.presentation.designsystem.theme.ShapeFull
import com.fitmate.presentation.designsystem.theme.ShapeLarge
import com.fitmate.presentation.designsystem.theme.Spacing2
import com.fitmate.presentation.designsystem.theme.Spacing3
import com.fitmate.presentation.designsystem.theme.Spacing4
import com.fitmate.presentation.designsystem.theme.Spacing5
import com.fitmate.presentation.designsystem.theme.Spacing6
import com.fitmate.presentation.designsystem.theme.TextPrimary
import com.fitmate.presentation.designsystem.theme.TextSecondary
import com.fitmate.presentation.designsystem.theme.White
import com.fitmate.presentation.designsystem.theme.fitMateDisplayTextStyle
import com.fitmate.presentation.designsystem.theme.fitMateTextStyle

private val HeaderHeight = 220.dp

// 1.2 회원가입 (기본 정보 입력 전)
@Composable
fun AITrainerIntroScreen(
    onNext: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            Column {
                IntroHeader()
                Column(modifier = Modifier.padding(horizontal = Spacing6)) {
                    Spacer(modifier = Modifier.height(Spacing6))
                    Text(
                        text = "나에게 맞는 운동을\n시작해볼까요?",
                        style = fitMateDisplayTextStyle(26.sp, 36.sp, letterSpacing = (-0.3).sp),
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(Spacing4))
                    Text(
                        text = "몇 가지 정보를 알려주시면 FitMate가 회원님에게\n맞는 운동 계획을 만들어드려요.",
                        style = fitMateTextStyle(16.sp, 24.sp),
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(28.dp))
                    AiTrainerCard()
                    Spacer(modifier = Modifier.height(Spacing6))
                }
            }
            Image(
                painter = painterResource(R.drawable.img_logo_transparent),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(y = 114.dp)
                    .size(width = 169.dp, height = 150.dp)
            )
        }
        FitMatePrimaryButton(
            text = "시작하기",
            onClick = onNext,
            modifier = Modifier.padding(start = Spacing6, end = Spacing6, bottom = Spacing6)
        )
    }
}

// 파란 배경 + 오른쪽으로 걸친 타원 라인
@Composable
private fun IntroHeader() {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(HeaderHeight)
            .clipToBounds()
            .background(Blue500)
    ) {
        val strokeWidth = 1.2.dp.toPx()
        drawOval(
            color = Blue200,
            topLeft = Offset(110.dp.toPx() + strokeWidth / 2, -120.dp.toPx() + strokeWidth / 2),
            size = Size(527.dp.toPx() - strokeWidth, 460.dp.toPx() - strokeWidth),
            style = Stroke(width = strokeWidth)
        )
    }
}

@Composable
private fun AiTrainerCard() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(ShapeLarge)
            .background(Blue100)
            .border(1.dp, BorderDefault, ShapeLarge)
            .padding(start = 22.8.dp, top = 22.8.dp, end = 22.8.dp, bottom = 25.2.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Blue400),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.Psychology,
                    contentDescription = null,
                    tint = White,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "FitMate AI Trainer",
                style = fitMateDisplayTextStyle(14.sp, 20.sp),
                color = Gray700
            )
        }
        Spacer(modifier = Modifier.height(Spacing3))
        Text(
            text = "신체 정보, 운동 목표, 운동 환경과 실제 운동 기록을 반영해 나에게 맞는 루틴으로 계속 조정해요.",
            style = fitMateTextStyle(14.sp, 22.sp),
            color = TextPrimary
        )
        Spacer(modifier = Modifier.height(Spacing5))
        Row(horizontalArrangement = Arrangement.spacedBy(Spacing2)) {
            listOf("맞춤 루틴", "실시간 조정", "목표 최적화").forEach { tag ->
                AiTrainerTag(text = tag)
            }
        }
    }
}

@Composable
private fun AiTrainerTag(text: String) {
    Box(
        modifier = Modifier
            .height(28.dp)
            .clip(ShapeFull)
            .background(Blue300)
            .padding(horizontal = Spacing3),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = fitMateTextStyle(12.sp, 18.sp, FontWeight.Bold),
            color = White
        )
    }
}

@Preview(widthDp = 394, heightDp = 892)
@Composable
private fun AITrainerIntroScreenPreview() {
    FitMateTheme {
        AITrainerIntroScreen(onNext = {})
    }
}
