package com.fitmate.presentation.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fitmate.presentation.designsystem.theme.BorderDefault
import com.fitmate.presentation.designsystem.theme.FitMateTheme
import com.fitmate.presentation.designsystem.theme.IconSizeDefault
import com.fitmate.presentation.designsystem.theme.ShapeMedium
import com.fitmate.presentation.designsystem.theme.Spacing4
import com.fitmate.presentation.designsystem.theme.TextPrimary
import com.fitmate.presentation.designsystem.theme.White
import com.fitmate.presentation.designsystem.theme.fitMateTextStyle

private val DropdownRowHeight = 52.dp

// 저장된 값 중 하나를 고르는 드롭다운 (헬스장 선택). 펼치면 필드 아래로 옵션이 이어 붙는다
@Composable
fun FitMateDropdownField(
    selected: String,
    options: List<String>,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(DropdownRowHeight)
                .clip(ShapeMedium)
                .background(White)
                .border(1.dp, BorderDefault, ShapeMedium)
                .clickable { onExpandedChange(!expanded) }
                .padding(horizontal = Spacing4),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = selected,
                style = fitMateTextStyle(14.sp, 20.sp, FontWeight.Bold),
                color = TextPrimary,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.Rounded.KeyboardArrowDown,
                contentDescription = if (expanded) "접기" else "펼치기",
                tint = Color(0xFF1D1B20),
                modifier = Modifier.size(IconSizeDefault)
            )
        }
        if (expanded) {
            // 옵션 칸은 모서리 없이 테두리를 1dp씩 겹쳐 쌓는다
            options.forEachIndexed { index, option ->
                Box(
                    modifier = Modifier
                        .offset(y = (-1).dp * (index + 1))
                        .fillMaxWidth()
                        .height(DropdownRowHeight)
                        .background(White)
                        .border(1.dp, BorderDefault)
                        .clickable {
                            onSelect(option)
                            onExpandedChange(false)
                        }
                        .padding(horizontal = Spacing4),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(text = option, style = fitMateTextStyle(14.sp, 20.sp), color = TextPrimary)
                }
            }
        }
    }
}

@Preview(widthDp = 314)
@Composable
private fun FitMateDropdownFieldPreview() {
    FitMateTheme {
        FitMateDropdownField(
            selected = "Fit Gym",
            options = listOf("Fit Gym", "별내 피트니스 센터"),
            expanded = true,
            onExpandedChange = {},
            onSelect = {}
        )
    }
}
