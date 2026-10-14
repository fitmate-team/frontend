package com.fitmate.presentation.auth

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fitmate.presentation.designsystem.component.FitMatePrimaryButton
import com.fitmate.presentation.designsystem.component.FitMateTextField
import com.fitmate.presentation.designsystem.component.FitMateTitleTopBar
import com.fitmate.presentation.designsystem.component.TextFieldActionButton
import com.fitmate.presentation.designsystem.theme.FitMateTheme
import com.fitmate.presentation.designsystem.theme.Spacing2
import com.fitmate.presentation.designsystem.theme.Spacing5

// 1.1 회원가입 (계정 정보)
@Composable
fun SignupIntroScreen(
    onBack: () -> Unit,
    onNext: () -> Unit,
    onCheckDuplicateId: (String) -> Unit = {}
) {
    var id by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordConfirm by remember { mutableStateOf("") }

    SignupScaffold(
        topBar = { FitMateTitleTopBar(title = "회원가입", onBack = onBack) },
        title = "FitMate를\n시작해볼까요?",
        bottomBar = { FitMatePrimaryButton(text = "다음", onClick = onNext) }
    ) {
        FitMateTextField(
            value = id,
            onValueChange = { id = it },
            placeholder = "아이디",
            leadingIcon = Icons.Outlined.Person,
            endPadding = Spacing2,
            trailingContent = {
                TextFieldActionButton(text = "중복확인", onClick = { onCheckDuplicateId(id) })
            }
        )
        Spacer(modifier = Modifier.height(Spacing2))
        SignupHelperText(text = "아이디는 8~16자로 입력해주세요.")

        Spacer(modifier = Modifier.height(Spacing5))
        FitMateTextField(
            value = password,
            onValueChange = { password = it },
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
            onValueChange = { passwordConfirm = it },
            placeholder = "비밀번호 확인",
            leadingIcon = Icons.Outlined.Lock,
            isPassword = true,
            height = 51.dp
        )
    }
}

@Preview(widthDp = 394, heightDp = 892)
@Composable
private fun SignupIntroScreenPreview() {
    FitMateTheme {
        SignupIntroScreen(onBack = {}, onNext = {})
    }
}
