package com.fitmate.presentation.designsystem.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fitmate.presentation.designsystem.R
import com.fitmate.presentation.designsystem.theme.Blue200
import com.fitmate.presentation.designsystem.theme.Blue500
import com.fitmate.presentation.designsystem.theme.FitMateTheme
import com.fitmate.presentation.designsystem.theme.Spacing3
import com.fitmate.presentation.designsystem.theme.TextPrimary
import com.fitmate.presentation.designsystem.theme.White
import com.fitmate.presentation.designsystem.theme.fitMateDisplayTextStyle

// AI 분석 중 전체 화면 (로봇 + 문구 + 로딩 스피너)
@Composable
fun AiAnalyzingScreen(
    modifier: Modifier = Modifier,
    message: String = "회원님에게 맞는 운동을\n분석하고 있어요."
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(White),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(R.drawable.img_robot),
                contentDescription = null,
                modifier = Modifier.size(width = 149.dp, height = 150.dp)
            )
            Spacer(modifier = Modifier.height(Spacing3))
            Text(
                text = message,
                style = fitMateDisplayTextStyle(24.sp, 36.sp, letterSpacing = (-0.3).sp),
                color = TextPrimary,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(30.dp))
            CircularProgressIndicator(
                modifier = Modifier.size(40.dp),
                color = Blue500,
                trackColor = Blue200,
                strokeWidth = 4.dp
            )
        }
    }
}

@Preview(widthDp = 394, heightDp = 892)
@Composable
private fun AiAnalyzingScreenPreview() {
    FitMateTheme { AiAnalyzingScreen() }
}
