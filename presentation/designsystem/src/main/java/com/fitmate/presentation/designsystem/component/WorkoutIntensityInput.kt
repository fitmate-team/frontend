package com.fitmate.presentation.designsystem.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fitmate.presentation.designsystem.theme.Blue500
import com.fitmate.presentation.designsystem.theme.BorderDefault
import com.fitmate.presentation.designsystem.theme.FitMateTheme
import com.fitmate.presentation.designsystem.theme.TextPrimary
import com.fitmate.presentation.designsystem.theme.TextSecondary
import com.fitmate.presentation.designsystem.theme.fitMateTextStyle

data class IntensityField(
    val label: String,
    val value: String,
    val onValueChange: (String) -> Unit,
    val keyboardType: KeyboardType = KeyboardType.Number
)

private val IntensityBoxShape = RoundedCornerShape(20.dp)

// 운동 세부 강도 입력 (세트 / 횟수 / 중량). 라벨 아래 숫자를 바로 입력하고, 입력 전에는 '-'
@Composable
fun WorkoutIntensityInput(
    fields: List<IntensityField>,
    modifier: Modifier = Modifier
) {
    val valueStyle = fitMateTextStyle(16.sp, 20.sp, FontWeight.Bold).copy(textAlign = TextAlign.Center)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(92.dp)
            .border(1.dp, BorderDefault, IntensityBoxShape),
        verticalAlignment = Alignment.CenterVertically
    ) {
        fields.forEachIndexed { index, field ->
            Column(
                modifier = Modifier
                    .weight(1f)
                    .height(46.dp)
                    .drawBehind {
                        if (index < fields.lastIndex) {
                            val x = size.width - 0.5.dp.toPx()
                            drawLine(BorderDefault, Offset(x, 0f), Offset(x, size.height), 1.dp.toPx())
                        }
                    },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = field.label, style = fitMateTextStyle(14.sp, 20.sp), color = TextSecondary)
                Spacer(modifier = Modifier.height(6.dp))
                BasicTextField(
                    value = field.value,
                    onValueChange = field.onValueChange,
                    singleLine = true,
                    textStyle = valueStyle.copy(color = TextPrimary),
                    cursorBrush = SolidColor(Blue500),
                    keyboardOptions = KeyboardOptions(keyboardType = field.keyboardType),
                    modifier = Modifier.fillMaxWidth(),
                    decorationBox = { innerTextField ->
                        Box(contentAlignment = Alignment.Center) {
                            if (field.value.isEmpty()) {
                                Text(text = "-", style = valueStyle, color = TextPrimary)
                            }
                            innerTextField()
                        }
                    }
                )
            }
        }
    }
}

@Preview(widthDp = 346)
@Composable
private fun WorkoutIntensityInputPreview() {
    FitMateTheme {
        WorkoutIntensityInput(
            fields = listOf(
                IntensityField("세트", "4", {}),
                IntensityField("횟수", "", {}),
                IntensityField("중량(kg)", "40", {}, KeyboardType.Decimal)
            )
        )
    }
}
