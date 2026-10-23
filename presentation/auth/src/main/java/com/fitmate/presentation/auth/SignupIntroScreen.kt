package com.fitmate.presentation.auth

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fitmate.presentation.designsystem.component.FitMatePrimaryButton
import com.fitmate.presentation.designsystem.component.FitMateTextField
import com.fitmate.presentation.designsystem.component.FitMateTitleTopBar
import com.fitmate.presentation.designsystem.component.TextFieldActionButton
import com.fitmate.presentation.designsystem.theme.Blue600
import com.fitmate.presentation.designsystem.theme.FitMateTheme
import com.fitmate.presentation.designsystem.theme.Red500
import com.fitmate.presentation.designsystem.theme.Spacing2
import com.fitmate.presentation.designsystem.theme.Spacing5

// 1.1 회원가입 (계정 정보). 중복확인 결과에 따라 아이디 안내 문구가 바뀐다
@Composable
fun SignupIntroScreen(
    loginId: String,
    password: String,
    passwordConfirm: String,
    idCheck: IdCheckState,
    onLoginIdChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onPasswordConfirmChange: (String) -> Unit,
    onCheckDuplicateId: () -> Unit,
    onBack: () -> Unit,
    onNext: () -> Unit
) {
    SignupScaffold(
        topBar = { FitMateTitleTopBar(title = "회원가입", onBack = onBack) },
        title = "FitMate를\n시작해볼까요?",
        bottomBar = { FitMatePrimaryButton(text = "다음", onClick = onNext) }
    ) {
        FitMateTextField(
            value = loginId,
            onValueChange = onLoginIdChange,
            placeholder = "아이디",
            leadingIcon = Icons.Outlined.Person,
            endPadding = Spacing2,
            trailingContent = {
                TextFieldActionButton(
                    text = if (idCheck == IdCheckState.Checking) "확인 중" else "중복확인",
                    onClick = onCheckDuplicateId
                )
            }
        )
        Spacer(modifier = Modifier.height(Spacing2))
        when (idCheck) {
            IdCheckState.Available -> SignupHelperText(text = "사용할 수 있는 아이디예요.", color = Blue600)
            IdCheckState.Taken -> SignupHelperText(text = "이미 사용 중인 아이디예요.", color = Red500)
            else -> SignupHelperText(text = "아이디는 8~16자로 입력해주세요.")
        }

        Spacer(modifier = Modifier.height(Spacing5))
        FitMateTextField(
            value = password,
            onValueChange = onPasswordChange,
            placeholder = "비밀번호",
            leadingIcon = Icons.Outlined.Lock,
            isPassword = true,
            height = 51.dp
        )
        Spacer(modifier = Modifier.height(Spacing2))
        SignupHelperText(text = "비밀번호는 영문, 숫자를 포함해 10자 이상으로 입력해주세요.")

        Spacer(modifier = Modifier.height(Spacing5))
        FitMateTextField(
            value = passwordConfirm,
            onValueChange = onPasswordConfirmChange,
            placeholder = "비밀번호 확인",
            leadingIcon = Icons.Outlined.Lock,
            isPassword = true,
            height = 51.dp
        )
        if (passwordConfirm.isNotEmpty() && passwordConfirm != password) {
            Spacer(modifier = Modifier.height(Spacing2))
            SignupHelperText(text = "비밀번호가 일치하지 않아요.", color = Red500)
        }
    }
}

@Preview(widthDp = 394, heightDp = 892)
@Composable
private fun SignupIntroScreenPreview() {
    FitMateTheme {
        SignupIntroScreen(
            loginId = "fitmate01",
            password = "",
            passwordConfirm = "",
            idCheck = IdCheckState.Available,
            onLoginIdChange = {},
            onPasswordChange = {},
            onPasswordConfirmChange = {},
            onCheckDuplicateId = {},
            onBack = {},
            onNext = {}
        )
    }
}
