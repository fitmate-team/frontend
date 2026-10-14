package com.fitmate.presentation.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fitmate.presentation.designsystem.R
import com.fitmate.presentation.designsystem.component.FitMatePrimaryButton
import com.fitmate.presentation.designsystem.component.FitMateTextField
import com.fitmate.presentation.designsystem.theme.Blue600
import com.fitmate.presentation.designsystem.theme.BorderDefault
import com.fitmate.presentation.designsystem.theme.FitMateTheme
import com.fitmate.presentation.designsystem.theme.Gray600
import com.fitmate.presentation.designsystem.theme.MinTouchTarget
import com.fitmate.presentation.designsystem.theme.Spacing3
import com.fitmate.presentation.designsystem.theme.Spacing4
import com.fitmate.presentation.designsystem.theme.Spacing5
import com.fitmate.presentation.designsystem.theme.Spacing6
import com.fitmate.presentation.designsystem.theme.TextPrimary
import com.fitmate.presentation.designsystem.theme.TextSecondary
import com.fitmate.presentation.designsystem.theme.White
import com.fitmate.presentation.designsystem.theme.fitMateDisplayTextStyle
import com.fitmate.presentation.designsystem.theme.fitMateTextStyle

@Composable
fun LoginScreen(
    onNavigateToSignup: () -> Unit,
    onLoginSuccess: () -> Unit,
    onFindAccount: () -> Unit = {}
) {
    var id by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 로고 영역
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(366.dp)
                .padding(top = 120.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.img_logo),
                contentDescription = "FitMate 로고",
                modifier = Modifier.size(108.dp)
            )
            Spacer(modifier = Modifier.height(Spacing5))
            Text(
                text = "FitMate",
                style = fitMateDisplayTextStyle(32.sp, 40.sp, letterSpacing = (-0.5).sp),
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "나에게 맞는 운동을 시작해보세요.",
                style = fitMateTextStyle(16.sp, 24.sp, FontWeight.Thin),
                color = Gray600
            )
        }

        // 입력 영역
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = Spacing5, start = Spacing6, end = Spacing6),
            verticalArrangement = Arrangement.spacedBy(Spacing3)
        ) {
            FitMateTextField(
                value = id,
                onValueChange = { id = it },
                placeholder = "아이디",
                leadingIcon = Icons.Outlined.Person
            )
            FitMateTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = "비밀번호",
                leadingIcon = Icons.Outlined.Lock,
                isPassword = true,
                height = 51.dp
            )
        }

        Spacer(modifier = Modifier.height(25.dp))
        FitMatePrimaryButton(
            text = "로그인",
            onClick = onLoginSuccess,
            height = 51.dp,
            modifier = Modifier.padding(horizontal = Spacing5)
        )

        Spacer(modifier = Modifier.height(6.dp))
        Box(
            modifier = Modifier
                .height(MinTouchTarget)
                .clickable(onClick = onFindAccount)
                .padding(horizontal = Spacing4),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "아이디/비밀번호 찾기",
                style = fitMateTextStyle(14.sp, 21.sp),
                color = TextSecondary
            )
        }

        Spacer(modifier = Modifier.height(34.dp))
        HorizontalDivider(
            color = BorderDefault,
            thickness = 1.dp,
            modifier = Modifier.padding(horizontal = Spacing5)
        )

        Spacer(modifier = Modifier.height(Spacing4))
        Text(
            text = "아직 회원이 아니신가요?",
            style = fitMateTextStyle(14.sp, 21.sp),
            color = TextSecondary
        )
        Spacer(modifier = Modifier.height(2.dp))
        Box(
            modifier = Modifier
                .heightIn(min = MinTouchTarget)
                .clickable(onClick = onNavigateToSignup)
                .padding(horizontal = Spacing4),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "회원가입",
                style = fitMateTextStyle(14.sp, 21.sp, FontWeight.Bold, letterSpacing = 0.14.sp)
                    .copy(textDecoration = TextDecoration.Underline),
                color = Blue600
            )
        }
        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Preview(widthDp = 394, heightDp = 892)
@Composable
private fun LoginScreenPreview() {
    FitMateTheme {
        LoginScreen(onNavigateToSignup = {}, onLoginSuccess = {})
    }
}
