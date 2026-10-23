package com.fitmate.presentation.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fitmate.presentation.designsystem.R
import com.fitmate.presentation.designsystem.theme.Blue500
import com.fitmate.presentation.designsystem.theme.Blue600
import com.fitmate.presentation.designsystem.theme.FitMateTheme
import com.fitmate.presentation.designsystem.theme.PrimaryButtonHeight
import com.fitmate.presentation.designsystem.theme.ShapeMedium
import com.fitmate.presentation.designsystem.theme.Spacing3
import com.fitmate.presentation.designsystem.theme.White
import com.fitmate.presentation.designsystem.theme.fitMateDisplayTextStyle
import com.fitmate.presentation.designsystem.theme.fitMateTextStyle

// 화면 하단 주요 액션 버튼 (다음 / 로그인 / 시작하기)
@Composable
fun FitMatePrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    height: Dp = PrimaryButtonHeight
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clip(ShapeMedium)
            .background(Blue600)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = fitMateDisplayTextStyle(16.sp, 24.sp, letterSpacing = 0.16.sp),
            color = White
        )
    }
}

// 보조 액션 버튼 (건너뛰기 / 운동 완료)
@Composable
fun FitMateOutlinedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    height: Dp = PrimaryButtonHeight,
    borderColor: Color = Blue600,
    textStyle: TextStyle = fitMateTextStyle(16.sp, 24.sp, FontWeight.Bold, letterSpacing = 0.16.sp)
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clip(ShapeMedium)
            .border(1.dp, borderColor, ShapeMedium)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = textStyle,
            color = Blue600
        )
    }
}

// 작은 버튼 (+ 루틴 생성하기 / 루틴 재생성하기). outlined면 흰 바탕 파란 테두리
@Composable
fun FitMateCompactButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    outlined: Boolean = false,
    leadingIcon: Painter? = null
) {
    Row(
        modifier = modifier
            .height(if (outlined) 32.dp else 36.dp)
            .clip(ShapeMedium)
            .background(if (outlined) White else Blue600)
            .then(if (outlined) Modifier.border(1.dp, Blue500, ShapeMedium) else Modifier)
            .clickable(onClick = onClick)
            .padding(start = if (leadingIcon != null) 11.dp else 16.dp, end = if (outlined) 12.dp else 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (leadingIcon != null) {
            Icon(painter = leadingIcon, contentDescription = null, tint = Blue500, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
        }
        Text(
            text = text,
            style = if (outlined) {
                fitMateTextStyle(14.sp, 24.sp, letterSpacing = 0.16.sp)
            } else {
                fitMateDisplayTextStyle(14.sp, 24.sp, letterSpacing = 0.16.sp)
            },
            color = if (outlined) Blue500 else White
        )
    }
}

@Preview
@Composable
private fun FitMateButtonPreview() {
    FitMateTheme {
        Column(verticalArrangement = Arrangement.spacedBy(Spacing3)) {
            FitMatePrimaryButton(text = "다음", onClick = {})
            FitMateOutlinedButton(text = "건너뛰기", onClick = {})
            FitMateCompactButton(text = "+ 루틴 생성하기", onClick = {})
            FitMateCompactButton(
                text = "루틴 재생성하기",
                onClick = {},
                outlined = true,
                leadingIcon = painterResource(R.drawable.ic_refresh)
            )
        }
    }
}
