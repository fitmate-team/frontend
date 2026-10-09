package com.fitmate.presentation.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardDoubleArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fitmate.presentation.designsystem.theme.Blue600
import com.fitmate.presentation.designsystem.theme.BorderDefault
import com.fitmate.presentation.designsystem.theme.FitMateTheme
import com.fitmate.presentation.designsystem.theme.Spacing2
import com.fitmate.presentation.designsystem.theme.Spacing5
import com.fitmate.presentation.designsystem.theme.TextSecondary
import com.fitmate.presentation.designsystem.theme.White
import com.fitmate.presentation.designsystem.theme.fitMateTextStyle

private val ExerciseChangeShape = RoundedCornerShape(14.dp)

// 운동 교체 안내 카드: 기존 운동(취소선) → 새 운동
@Composable
fun ExerciseChangeCard(
    before: String,
    after: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(110.dp)
            .clip(ExerciseChangeShape)
            .background(White)
            .border(1.dp, BorderDefault, ExerciseChangeShape)
            .padding(horizontal = Spacing5, vertical = Spacing2),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterVertically)
    ) {
        Text(
            text = before,
            style = fitMateTextStyle(13.sp, 19.5.sp).copy(textDecoration = TextDecoration.LineThrough),
            color = TextSecondary
        )
        Icon(
            imageVector = Icons.Rounded.KeyboardDoubleArrowDown,
            contentDescription = null,
            tint = Blue600,
            modifier = Modifier.size(28.dp)
        )
        Text(
            text = after,
            style = fitMateTextStyle(17.sp, 25.5.sp, FontWeight.Bold),
            color = Blue600
        )
    }
}

@Preview
@Composable
private fun ExerciseChangeCardPreview() {
    FitMateTheme {
        ExerciseChangeCard(before = "레그프레스", after = "덤벨 스쿼트")
    }
}
