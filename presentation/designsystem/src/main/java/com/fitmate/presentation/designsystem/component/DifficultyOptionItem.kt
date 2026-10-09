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
import androidx.compose.material.icons.outlined.SentimentVerySatisfied
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fitmate.presentation.designsystem.theme.Blue100
import com.fitmate.presentation.designsystem.theme.Blue500
import com.fitmate.presentation.designsystem.theme.BorderDefault
import com.fitmate.presentation.designsystem.theme.FitMateTheme
import com.fitmate.presentation.designsystem.theme.Gray300
import com.fitmate.presentation.designsystem.theme.MinTouchTarget
import com.fitmate.presentation.designsystem.theme.ShapeLarge
import com.fitmate.presentation.designsystem.theme.ShapeMedium
import com.fitmate.presentation.designsystem.theme.Spacing3
import com.fitmate.presentation.designsystem.theme.Spacing4
import com.fitmate.presentation.designsystem.theme.TextPrimary
import com.fitmate.presentation.designsystem.theme.TextSecondary
import com.fitmate.presentation.designsystem.theme.White
import com.fitmate.presentation.designsystem.theme.fitMateTextStyle

// 운동 난이도 피드백 선택 항목 (너무 쉬웠어요 / 적당했어요 ...)
@Composable
fun DifficultyOptionItem(
    icon: ImageVector,
    title: String,
    description: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .clip(ShapeLarge)
            .background(if (selected) Blue100 else White)
            .border(1.dp, if (selected) Blue500 else BorderDefault, ShapeLarge)
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp),
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
                modifier = Modifier.size(28.dp)
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
private fun DifficultyOptionItemPreview() {
    FitMateTheme {
        Column(verticalArrangement = Arrangement.spacedBy(Spacing3)) {
            DifficultyOptionItem(
                icon = Icons.Outlined.SentimentVerySatisfied,
                title = "너무 쉬웠어요.",
                description = "다음에는 더 높은 강도로!",
                selected = true,
                onClick = {}
            )
            DifficultyOptionItem(
                icon = Icons.Outlined.SentimentVerySatisfied,
                title = "너무 쉬웠어요.",
                description = "다음에는 더 높은 강도로!",
                selected = false,
                onClick = {}
            )
        }
    }
}
