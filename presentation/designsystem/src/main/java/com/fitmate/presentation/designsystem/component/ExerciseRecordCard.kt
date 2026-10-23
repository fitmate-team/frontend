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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FitnessCenter
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fitmate.presentation.designsystem.theme.Blue100
import com.fitmate.presentation.designsystem.theme.Blue500
import com.fitmate.presentation.designsystem.theme.BorderDefault
import com.fitmate.presentation.designsystem.theme.FitMateTheme
import com.fitmate.presentation.designsystem.theme.ShapeLarge
import com.fitmate.presentation.designsystem.theme.Spacing1
import com.fitmate.presentation.designsystem.theme.Spacing2
import com.fitmate.presentation.designsystem.theme.Spacing3
import com.fitmate.presentation.designsystem.theme.Spacing4
import com.fitmate.presentation.designsystem.theme.TextPrimary
import com.fitmate.presentation.designsystem.theme.TextSecondary
import com.fitmate.presentation.designsystem.theme.White
import com.fitmate.presentation.designsystem.theme.fitMateTextStyle

private val RecordFieldShape = RoundedCornerShape(10.dp)
private const val RECORD_FIELD_SLOTS = 3

// 최근 운동 기록 카드. fields는 오른쪽부터 채워지는 3칸(kg / 회 / 세트),
// trailing은 제목 줄 오른쪽에 붙는 입력 칸(유산소 시간)
@Composable
fun ExerciseRecordCard(
    title: String,
    modifier: Modifier = Modifier,
    trailing: (@Composable () -> Unit)? = null,
    fields: List<@Composable () -> Unit> = emptyList()
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(ShapeLarge)
            .background(White)
            .border(1.dp, BorderDefault, ShapeLarge)
            .padding(start = Spacing4, top = Spacing4, end = Spacing4, bottom = if (fields.isEmpty()) Spacing4 else 17.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RecordFieldShape)
                    .background(Blue100)
                    .border(1.dp, BorderDefault, RecordFieldShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.FitnessCenter,
                    contentDescription = null,
                    tint = Blue500,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.width(Spacing3))
            Text(
                text = title,
                style = fitMateTextStyle(14.sp, 21.sp, FontWeight.Bold),
                color = TextPrimary,
                modifier = Modifier.weight(1f)
            )
            if (trailing != null) {
                Box(modifier = Modifier.width(100.dp)) { trailing() }
            }
        }
        if (fields.isNotEmpty()) {
            Spacer(modifier = Modifier.height(Spacing3))
            Row(horizontalArrangement = Arrangement.spacedBy(7.dp)) {
                repeat(RECORD_FIELD_SLOTS - fields.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
                fields.forEach { field ->
                    Box(modifier = Modifier.weight(1f)) { field() }
                }
            }
        }
    }
}

// 기록 카드 안의 숫자 + 단위 입력 칸. 입력 전에는 '-'로 표시
@Composable
fun RecordValueField(
    value: String,
    onValueChange: (String) -> Unit,
    unit: String,
    modifier: Modifier = Modifier
) {
    val valueStyle = fitMateTextStyle(14.sp, 20.sp, FontWeight.Bold)
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        textStyle = valueStyle.copy(color = TextPrimary),
        cursorBrush = SolidColor(Blue500),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = modifier,
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .clip(RecordFieldShape)
                    .background(White)
                    .border(1.dp, BorderDefault, RecordFieldShape)
                    .padding(start = Spacing3, end = Spacing2),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    if (value.isEmpty()) {
                        Text(text = "-", style = valueStyle, color = TextPrimary)
                    }
                    innerTextField()
                }
                Spacer(modifier = Modifier.width(Spacing1))
                Text(text = unit, style = fitMateTextStyle(13.sp, 19.5.sp), color = TextSecondary)
            }
        }
    )
}

@Preview(widthDp = 346)
@Composable
private fun ExerciseRecordCardPreview() {
    FitMateTheme {
        Column(verticalArrangement = Arrangement.spacedBy(Spacing3)) {
            ExerciseRecordCard(
                title = "벤치 프레스",
                fields = listOf(
                    { RecordValueField(value = "60", onValueChange = {}, unit = "kg") },
                    { RecordValueField(value = "8", onValueChange = {}, unit = "회") },
                    { RecordValueField(value = "4", onValueChange = {}, unit = "세트") }
                )
            )
            ExerciseRecordCard(
                title = "줄넘기",
                trailing = { RecordValueField(value = "40", onValueChange = {}, unit = "분") }
            )
        }
    }
}
