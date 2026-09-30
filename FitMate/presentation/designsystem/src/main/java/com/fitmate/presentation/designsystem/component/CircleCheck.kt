package com.fitmate.presentation.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fitmate.presentation.designsystem.theme.Blue500
import com.fitmate.presentation.designsystem.theme.BorderDefault
import com.fitmate.presentation.designsystem.theme.FitMateTheme
import com.fitmate.presentation.designsystem.theme.Spacing2
import com.fitmate.presentation.designsystem.theme.White

private val CircleCheckShape = RoundedCornerShape(10.dp)

// 체크 상태 표시용. 클릭은 감싸는 항목에서 처리한다.
@Composable
fun CircleCheck(
    checked: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(22.dp)
            .clip(CircleCheckShape)
            .background(if (checked) Blue500 else White)
            .border(1.dp, BorderDefault, CircleCheckShape),
        contentAlignment = Alignment.Center
    ) {
        if (checked) {
            Icon(
                imageVector = Icons.Rounded.Check,
                contentDescription = null,
                tint = White,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Preview
@Composable
private fun CircleCheckPreview() {
    FitMateTheme {
        Row(horizontalArrangement = Arrangement.spacedBy(Spacing2)) {
            CircleCheck(checked = true)
            CircleCheck(checked = false)
        }
    }
}
