package com.fitmate.presentation.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fitmate.presentation.designsystem.theme.Blue100
import com.fitmate.presentation.designsystem.theme.Blue600
import com.fitmate.presentation.designsystem.theme.BorderDefault
import com.fitmate.presentation.designsystem.theme.FitMateTheme
import com.fitmate.presentation.designsystem.theme.Spacing2
import com.fitmate.presentation.designsystem.theme.TextSecondary
import com.fitmate.presentation.designsystem.theme.White
import com.fitmate.presentation.designsystem.theme.fitMateTextStyle

private val RestTimeButtonShape = RoundedCornerShape(10.dp)

// 휴식 시간 선택 버튼 (30초, 60초 ...)
@Composable
fun RestTimeButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(width = 60.dp, height = 36.dp)
            .clip(RestTimeButtonShape)
            .background(if (selected) Blue100 else White)
            .border(1.dp, if (selected) Blue600 else BorderDefault, RestTimeButtonShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = fitMateTextStyle(
                fontSize = 12.sp,
                lineHeight = 18.sp,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
            ),
            color = if (selected) Blue600 else TextSecondary
        )
    }
}

@Preview
@Composable
private fun RestTimeButtonPreview() {
    FitMateTheme {
        Row(horizontalArrangement = Arrangement.spacedBy(Spacing2)) {
            RestTimeButton(text = "30초", selected = false, onClick = {})
            RestTimeButton(text = "30초", selected = true, onClick = {})
        }
    }
}
