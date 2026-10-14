package com.fitmate.presentation.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fitmate.presentation.designsystem.theme.Blue100
import com.fitmate.presentation.designsystem.theme.Blue500
import com.fitmate.presentation.designsystem.theme.BorderDefault
import com.fitmate.presentation.designsystem.theme.FitMateTheme
import com.fitmate.presentation.designsystem.theme.Gray300
import com.fitmate.presentation.designsystem.theme.IconSizeDefault
import com.fitmate.presentation.designsystem.theme.MinTouchTarget
import com.fitmate.presentation.designsystem.theme.ShapeLarge
import com.fitmate.presentation.designsystem.theme.ShapeMedium
import com.fitmate.presentation.designsystem.theme.Spacing3
import com.fitmate.presentation.designsystem.theme.Spacing4
import com.fitmate.presentation.designsystem.theme.TextPrimary
import com.fitmate.presentation.designsystem.theme.TextSecondary
import com.fitmate.presentation.designsystem.theme.White
import com.fitmate.presentation.designsystem.theme.fitMateTextStyle

// 아이콘 + 제목 + 설명으로 된 단일 선택 카드 (운동 경험 / 목표 / 방식 / 시간 등)
// expandedContent가 있으면 선택됐을 때 카드 아래쪽에 펼쳐 보여준다 (헬스장 이름 입력)
@Composable
fun SelectOptionCard(
    icon: ImageVector,
    title: String,
    description: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    iconSize: Dp = IconSizeDefault,
    height: Dp = 80.dp,
    expandedTopPadding: Dp = 6.8.dp,
    expandedContent: (@Composable () -> Unit)? = null
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(ShapeLarge)
            .background(if (selected) Blue100 else White)
            .border(1.dp, if (selected) Blue500 else BorderDefault, ShapeLarge)
            .clickable(onClick = onClick)
    ) {
        SelectOptionHeader(
            icon = icon,
            title = title,
            description = description,
            selected = selected,
            iconSize = iconSize,
            height = height
        )
        if (selected && expandedContent != null) {
            Column(modifier = Modifier.padding(start = Spacing4, top = expandedTopPadding, end = Spacing4, bottom = 17.2.dp)) {
                expandedContent()
            }
        }
    }
}

@Composable
private fun SelectOptionHeader(
    icon: ImageVector,
    title: String,
    description: String,
    selected: Boolean,
    iconSize: Dp,
    height: Dp
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .padding(horizontal = 14.8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(MinTouchTarget)
                .clip(ShapeMedium)
                .background(if (selected) Blue500 else White)
                .border(1.dp, BorderDefault, ShapeMedium),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (selected) White else Gray300,
                modifier = Modifier.size(iconSize)
            )
        }
        Spacer(modifier = Modifier.width(Spacing4))
        Column {
            Text(
                text = title,
                style = fitMateTextStyle(16.sp, 24.sp, FontWeight.Bold),
                color = TextPrimary
            )
            Text(
                text = description,
                style = fitMateTextStyle(12.sp, 18.sp),
                color = TextSecondary
            )
        }
    }
}

@Preview
@Composable
private fun SelectOptionCardPreview() {
    FitMateTheme {
        Column(verticalArrangement = Arrangement.spacedBy(Spacing3)) {
            SelectOptionCard(
                icon = Icons.Rounded.Bolt,
                title = "20분",
                description = "짧고 집중적인 운동",
                selected = true,
                onClick = {},
                iconSize = 26.dp
            )
            SelectOptionCard(
                icon = Icons.Rounded.Bolt,
                title = "20분",
                description = "짧고 집중적인 운동",
                selected = false,
                onClick = {},
                iconSize = 26.dp
            )
        }
    }
}
