package com.fitmate.presentation.routine

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fitmate.presentation.designsystem.R
import com.fitmate.presentation.designsystem.component.FitMateTitleTopBar
import com.fitmate.presentation.designsystem.component.FormTitle
import com.fitmate.presentation.designsystem.theme.Blue200
import com.fitmate.presentation.designsystem.theme.Blue300
import com.fitmate.presentation.designsystem.theme.Blue400
import com.fitmate.presentation.designsystem.theme.Blue500
import com.fitmate.presentation.designsystem.theme.Blue700
import com.fitmate.presentation.designsystem.theme.FitMateTheme
import com.fitmate.presentation.designsystem.theme.ShapeLarge
import com.fitmate.presentation.designsystem.theme.Spacing3
import com.fitmate.presentation.designsystem.theme.Spacing4
import com.fitmate.presentation.designsystem.theme.Spacing5
import com.fitmate.presentation.designsystem.theme.Spacing6
import com.fitmate.presentation.designsystem.theme.TextPrimary
import com.fitmate.presentation.designsystem.theme.TextSecondary
import com.fitmate.presentation.designsystem.theme.White
import com.fitmate.presentation.designsystem.theme.fitMateTextStyle

private val MethodIconShape = RoundedCornerShape(8.dp)
private val RecommendBadgeShape = RoundedCornerShape(topEnd = 12.dp, bottomStart = 12.dp)

// 3.3 루틴 만들기 - 방식 선택
@Composable
fun RoutineCreationMethodScreen(
    onBack: () -> Unit = {},
    onAiHandsOff: () -> Unit = {},
    onAiTogether: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
    ) {
        FitMateTitleTopBar(title = "루틴 만들기", onBack = onBack)
        Column(modifier = Modifier.padding(horizontal = Spacing6)) {
            Spacer(modifier = Modifier.height(Spacing6))
            FormTitle(text = "어떻게 루틴을 만들까요?")
            Spacer(modifier = Modifier.height(Spacing6))
            RoutineMethodCard(
                title = "AI에게 맡기기",
                titleColor = Blue500,
                description = "내 목표와 운동 환경에 맞게\n주간 루틴 전체를 만들어드려요.",
                borderColor = Blue400,
                iconBackground = Blue500,
                recommended = true,
                onClick = onAiHandsOff
            ) {
                Image(
                    painter = painterResource(R.drawable.img_robot),
                    contentDescription = null,
                    modifier = Modifier
                        .offset(x = 3.67.dp, y = 4.dp)
                        .size(54.dp)
                )
            }
            Spacer(modifier = Modifier.height(Spacing3))
            RoutineMethodCard(
                title = "AI와 함께 만들기",
                titleColor = TextPrimary,
                description = "내가 정한 운동은 유지하고\n빈 부분은 AI가 채워드려요.",
                borderColor = Blue200,
                iconBackground = Blue300,
                recommended = false,
                onClick = onAiTogether
            ) {
                Icon(
                    imageVector = Icons.Rounded.AutoAwesome,
                    contentDescription = null,
                    tint = White,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(28.dp)
                )
            }
        }
    }
}

@Composable
private fun RoutineMethodCard(
    title: String,
    titleColor: Color,
    description: String,
    borderColor: Color,
    iconBackground: Color,
    recommended: Boolean,
    onClick: () -> Unit,
    icon: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(110.dp)
            .clip(ShapeLarge)
            .background(White)
            .border(1.dp, borderColor, ShapeLarge)
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(Spacing5),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(MethodIconShape)
                    .background(iconBackground),
                content = icon
            )
            Spacer(modifier = Modifier.width(Spacing4))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, style = fitMateTextStyle(16.sp, 24.sp, FontWeight.Bold), color = titleColor)
                Text(text = description, style = fitMateTextStyle(13.sp, 19.sp), color = TextSecondary)
            }
            Spacer(modifier = Modifier.width(Spacing4))
            Icon(
                imageVector = Icons.Rounded.ChevronRight,
                contentDescription = null,
                tint = Blue700,
                modifier = Modifier.size(20.dp)
            )
        }
        if (recommended) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .clip(RecommendBadgeShape)
                    .background(Blue500)
                    .padding(start = 10.dp, top = 4.dp, end = Spacing3, bottom = 4.dp)
            ) {
                Text(
                    text = "FitMate 추천",
                    style = fitMateTextStyle(10.sp, 15.sp, FontWeight.Medium, FontFamily.Default),
                    color = White
                )
            }
        }
    }
}

@Preview(widthDp = 394, heightDp = 892)
@Composable
private fun RoutineCreationMethodScreenPreview() {
    FitMateTheme { RoutineCreationMethodScreen() }
}
