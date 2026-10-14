package com.fitmate.presentation.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fitmate.presentation.designsystem.theme.Blue100
import com.fitmate.presentation.designsystem.theme.Blue500
import com.fitmate.presentation.designsystem.theme.Blue600
import com.fitmate.presentation.designsystem.theme.BorderDefault
import com.fitmate.presentation.designsystem.theme.FitMateTheme
import com.fitmate.presentation.designsystem.theme.MinTouchTarget
import com.fitmate.presentation.designsystem.theme.ShapeMedium
import com.fitmate.presentation.designsystem.theme.TextSecondary
import com.fitmate.presentation.designsystem.theme.White
import com.fitmate.presentation.designsystem.theme.fitMateTextStyle

// 텍스트 한 줄짜리 선택 박스 (성별 / 요일). 너비는 호출하는 쪽에서 정한다
@Composable
fun SelectableBox(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    selectedTextColor: Color = Blue600,
    height: Dp = MinTouchTarget
) {
    Box(
        modifier = modifier
            .height(height)
            .clip(ShapeMedium)
            .background(if (selected) Blue100 else White)
            .border(1.dp, if (selected) Blue500 else BorderDefault, ShapeMedium)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = fitMateTextStyle(
                fontSize = 14.sp,
                lineHeight = 21.sp,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
            ),
            color = if (selected) selectedTextColor else TextSecondary
        )
    }
}

@Preview
@Composable
private fun SelectableBoxPreview() {
    FitMateTheme {
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            SelectableBox(text = "남성", selected = false, onClick = {}, modifier = Modifier.weight(1f))
            SelectableBox(text = "여성", selected = true, onClick = {}, modifier = Modifier.weight(1f))
        }
    }
}
