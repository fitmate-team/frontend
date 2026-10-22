package com.fitmate.presentation.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.SentimentVerySatisfied
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fitmate.presentation.designsystem.theme.FitMateTheme
import com.fitmate.presentation.designsystem.theme.Spacing3

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
    SelectOptionCard(
        icon = icon,
        title = title,
        description = description,
        selected = selected,
        onClick = onClick,
        modifier = modifier,
        iconSize = 28.dp
    )
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
