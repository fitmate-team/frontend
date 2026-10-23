package com.fitmate.data.remote

import com.fitmate.data.local.TokenStorage
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

// 로그인이 필요한 API에 Access 토큰을 붙인다
class AuthInterceptor @Inject constructor(
    private val tokenStorage: TokenStorage
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        if (request.url.encodedPath in PublicPaths) return chain.proceed(request)

        val token = runBlocking { tokenStorage.accessToken() } ?: return chain.proceed(request)
        return chain.proceed(
            request.newBuilder()
                .header("Authorization", "Bearer $token")
                .build()
        )
    }

    companion object {
        // Swagger에서 security가 비어 있는(인증 불필요) API
        val PublicPaths = setOf(
            "/api/auth/login",
            "/api/auth/reissue",
            "/api/members/signup",
            "/api/members/id-check",
            "/api/exercises",
            "/api/equipments"
        )
    }
}
