package com.fitmate.presentation.auth

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext

// ViewModel의 일회성 이벤트(안내 문구 / 화면 이동)를 처리한다. 안내 문구는 토스트로 보여준다

@Composable
fun LoginEventEffect(viewModel: LoginViewModel, onLoginSuccess: () -> Unit) {
    val context = LocalContext.current
    val currentOnLoginSuccess by rememberUpdatedState(onLoginSuccess)
    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                is LoginEvent.Message -> Toast.makeText(context, event.text, Toast.LENGTH_SHORT).show()
                LoginEvent.Success -> currentOnLoginSuccess()
            }
        }
    }
}

// 회원가입 단계 화면마다 호출한다. 현재 보이는 화면 하나만 이벤트를 받는다
@Composable
fun SignupEventEffect(
    viewModel: SignupViewModel,
    onCompleted: () -> Unit = {},
    onNeedLogin: () -> Unit = {}
) {
    val context = LocalContext.current
    val currentOnCompleted by rememberUpdatedState(onCompleted)
    val currentOnNeedLogin by rememberUpdatedState(onNeedLogin)
    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                is SignupEvent.Message -> Toast.makeText(context, event.text, Toast.LENGTH_SHORT).show()
                SignupEvent.Completed -> currentOnCompleted()
                SignupEvent.NeedLogin -> currentOnNeedLogin()
            }
        }
    }
}
