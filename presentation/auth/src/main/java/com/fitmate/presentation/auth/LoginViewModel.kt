package com.fitmate.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitmate.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface LoginEvent {
    data class Message(val text: String) : LoginEvent
    data object Success : LoginEvent
}

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _events = Channel<LoginEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    fun login(loginId: String, password: String) {
        if (_isLoading.value) return
        if (loginId.isBlank() || password.isBlank()) {
            _events.trySend(LoginEvent.Message("아이디와 비밀번호를 입력해주세요."))
            return
        }
        _isLoading.value = true
        viewModelScope.launch {
            authRepository.login(loginId.trim(), password)
                .onSuccess { _events.send(LoginEvent.Success) }
                .onFailure { error -> _events.send(LoginEvent.Message(error.message ?: "로그인에 실패했어요.")) }
            _isLoading.value = false
        }
    }
}
