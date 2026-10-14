package com.fitmate.presentation.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FitnessCenter
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fitmate.presentation.designsystem.theme.Blue100
import com.fitmate.presentation.designsystem.theme.Blue200
import com.fitmate.presentation.designsystem.theme.BorderDefault
import com.fitmate.presentation.designsystem.theme.FitMateTheme
import com.fitmate.presentation.designsystem.theme.MinTouchTarget
import com.fitmate.presentation.designsystem.theme.ShapeLarge
import com.fitmate.presentation.designsystem.theme.ShapeMedium
import com.fitmate.presentation.designsystem.theme.Spacing3
import com.fitmate.presentation.designsystem.theme.Spacing4
import com.fitmate.presentation.designsystem.theme.TextPrimary
import com.fitmate.presentation.designsystem.theme.TextSecondary
import com.fitmate.presentation.designsystem.theme.White
import com.fitmate.presentation.designsystem.theme.fitMateTextStyle

// 연한 파란 아이콘 + 안내 문구 카드 (운동이 없을 때). action이 있으면 아래에 버튼을 붙인다
@Composable
fun EmptyStateCard(
    icon: ImageVector,
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    iconSize: Dp = 28.dp,
    containerColor: Color = White,
    action: (@Composable () -> Unit)? = null
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(ShapeLarge)
            .background(containerColor)
            .border(1.dp, BorderDefault, ShapeLarge)
    ) {
        Row(
            modifier = Modifier.padding(start = 14.8.dp, top = Spacing4, end = 14.8.dp, bottom = if (action == null) Spacing4 else 0.dp),
            verticalAlignment = if (action == null) Alignment.CenterVertically else Alignment.Top
        ) {
            SoftIconBox(icon = icon, iconSize = iconSize)
            Spacer(modifier = Modifier.width(Spacing4))
            Column {
                Text(
                    text = title,
                    style = fitMateTextStyle(16.sp, 24.sp, FontWeight.Bold),
                    color = TextPrimary
                )
                // 버튼이 붙는 카드는 설명이 여러 줄이라 제목과 4dp 띄운다
                if (action != null) Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    style = fitMateTextStyle(12.sp, 18.sp),
                    color = TextSecondary
                )
            }
        }
        if (action != null) {
            Box(modifier = Modifier.padding(start = 20.dp, top = 15.8.dp, end = 20.dp, bottom = 21.2.dp)) {
                action()
            }
        }
    }
}

// 연한 파란 배경 48dp 아이콘 박스 (운동 카드 / 빈 상태 카드)
@Composable
fun SoftIconBox(
    icon: ImageVector,
    modifier: Modifier = Modifier,
    iconSize: Dp = 28.dp
) {
    Box(
        modifier = modifier
            .size(MinTouchTarget)
            .clip(ShapeMedium)
            .background(Blue100)
            .border(1.dp, BorderDefault, ShapeMedium),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Blue200,
            modifier = Modifier.size(iconSize)
        )
    }
}

@Preview(widthDp = 346)
@Composable
private fun EmptyStateCardPreview() {
    FitMateTheme {
        Column(verticalArrangement = Arrangement.spacedBy(Spacing3)) {
            EmptyStateCard(
                icon = Icons.Rounded.FitnessCenter,
                title = "오늘은 운동하는 날이 아니에요.",
                description = "다음에 다시 만나요!"
            )
            EmptyStateCard(
                icon = Icons.Rounded.FitnessCenter,
                title = "오늘의 운동이 없어요.",
                description = "이번 주 운동을 추가하면,\n오늘의 운동이 여기에 표시돼요.",
                action = { FitMatePrimaryButton(text = "운동 추가하기", onClick = {}, height = 50.dp) }
            )
        }
    }
}
