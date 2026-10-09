package com.fitmate.presentation.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fitmate.presentation.designsystem.theme.Blue100
import com.fitmate.presentation.designsystem.theme.Blue600
import com.fitmate.presentation.designsystem.theme.Blue700
import com.fitmate.presentation.designsystem.theme.BorderDefault
import com.fitmate.presentation.designsystem.theme.FitMateNumberFontFamily
import com.fitmate.presentation.designsystem.theme.FitMateTheme
import com.fitmate.presentation.designsystem.theme.Gray600
import com.fitmate.presentation.designsystem.theme.ShapeMedium
import com.fitmate.presentation.designsystem.theme.Spacing2
import com.fitmate.presentation.designsystem.theme.Spacing4
import com.fitmate.presentation.designsystem.theme.TextPrimary
import com.fitmate.presentation.designsystem.theme.TextSecondary
import com.fitmate.presentation.designsystem.theme.White
import com.fitmate.presentation.designsystem.theme.fitMateTextStyle

// 신체 수치 카드 (몸무게 68.4kg 등)
@Composable
fun BodyMetricCard(
    label: String,
    value: String,
    unit: String,
    caption: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .height(92.dp)
            .clip(ShapeMedium)
            .background(if (selected) Blue100 else White)
            .border(1.dp, if (selected) Blue600 else BorderDefault, ShapeMedium)
            .clickable(onClick = onClick)
            .padding(horizontal = Spacing4, vertical = 14.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = fitMateTextStyle(13.sp, 18.sp),
            color = if (selected) Blue700 else Gray600
        )
        Text(
            text = buildAnnotatedString {
                append(value)
                withStyle(SpanStyle(fontSize = 14.sp)) { append(unit) }
            },
            style = fitMateTextStyle(23.sp, 28.sp, fontFamily = FitMateNumberFontFamily),
            color = TextPrimary
        )
        Text(
            text = caption,
            style = fitMateTextStyle(11.sp, 16.sp),
            color = TextSecondary
        )
    }
}

@Preview
@Composable
private fun BodyMetricCardPreview() {
    FitMateTheme {
        Row(horizontalArrangement = Arrangement.spacedBy(Spacing2)) {
            BodyMetricCard(
                label = "몸무게", value = "68.4", unit = "kg", caption = "최근 측정",
                selected = true, onClick = {}, modifier = Modifier.width(166.dp)
            )
            BodyMetricCard(
                label = "몸무게", value = "68.4", unit = "kg", caption = "최근 측정",
                selected = false, onClick = {}, modifier = Modifier.width(166.dp)
            )
        }
    }
}
