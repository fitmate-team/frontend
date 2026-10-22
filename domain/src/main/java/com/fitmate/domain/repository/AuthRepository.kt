package com.fitmate.domain.repository

import com.fitmate.domain.model.SignUpForm

interface AuthRepository {

    // 로그인 성공 시 토큰을 저장한다
    suspend fun login(loginId: String, password: String): Result<Unit>

    // true면 사용 가능한 아이디
    suspend fun isLoginIdAvailable(loginId: String): Result<Boolean>

    suspend fun signUp(form: SignUpForm): Result<Unit>
}
