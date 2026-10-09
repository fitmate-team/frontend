package com.fitmate.presentation.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.TrendingUp
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
import com.fitmate.presentation.designsystem.theme.Blue500
import com.fitmate.presentation.designsystem.theme.BorderDefault
import com.fitmate.presentation.designsystem.theme.FitMateTheme
import com.fitmate.presentation.designsystem.theme.Gray700
import com.fitmate.presentation.designsystem.theme.Spacing3
import com.fitmate.presentation.designsystem.theme.TextPrimary
import com.fitmate.presentation.designsystem.theme.White
import com.fitmate.presentation.designsystem.theme.fitMateTextStyle

private val RecommendReasonShape = RoundedCornerShape(14.dp)

// AI 추천 이유 카드 (아이콘 + 항목명 + 내용)
@Composable
fun RecommendReasonCard(
    icon: ImageVector,
    title: String,
    content: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RecommendReasonShape)
            .background(White)
            .border(1.dp, BorderDefault, RecommendReasonShape)
            .padding(horizontal = 14.dp, vertical = Spacing3)
    ) {
        Row(
            modifier = Modifier.padding(bottom = 2.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Blue500,
                modifier = Modifier.size(18.dp)
            )
            Text(
                text = title,
                style = fitMateTextStyle(11.sp, 16.5.sp, FontWeight.Bold),
                color = Gray700
            )
        }
        Text(
            text = content,
            style = fitMateTextStyle(13.sp, 18.sp),
            color = TextPrimary
        )
    }
}

@Preview
@Composable
private fun RecommendReasonCardPreview() {
    FitMateTheme {
        RecommendReasonCard(
            icon = Icons.AutoMirrored.Rounded.TrendingUp,
            title = "운동 목표",
            content = "체지방 감량 · 균형형",
            modifier = Modifier.width(168.dp)
        )
    }
}
