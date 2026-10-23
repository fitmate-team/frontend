package com.fitmate.data.remote

import com.fitmate.data.local.TokenStorage
import com.fitmate.data.remote.api.TokenApi
import com.fitmate.data.remote.dto.TokenReissueRequestDto
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

// 401을 받으면 Refresh 토큰으로 Access 토큰을 재발급받아 한 번만 다시 요청한다
// 재발급까지 실패하면 저장된 토큰을 지운다 (다시 로그인 필요)
class TokenAuthenticator @Inject constructor(
    private val tokenStorage: TokenStorage,
    private val tokenApi: TokenApi
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        if (response.request.url.encodedPath in AuthInterceptor.PublicPaths) return null
        // 이미 재발급한 토큰으로 다시 보낸 요청이 또 401이면 포기한다
        if (response.priorResponse != null) return null

        val failedToken = response.request.header("Authorization")?.removePrefix("Bearer ")

        synchronized(this) {
            val newToken = runBlocking {
                val current = tokenStorage.accessToken()
                // 다른 요청이 먼저 재발급을 끝냈다면 그 토큰을 쓴다
                if (current != null && current != failedToken) return@runBlocking current

                val refreshToken = tokenStorage.refreshToken() ?: return@runBlocking null
                val reissued = runCatching {
                    tokenApi.reissue(TokenReissueRequestDto(refreshToken)).execute()
                }.getOrNull()?.takeIf { it.isSuccessful }?.body()?.accessToken

                if (reissued == null) {
                    tokenStorage.clear()
                } else {
                    tokenStorage.saveAccessToken(reissued)
                }
                reissued
            } ?: return null

            return response.request.newBuilder()
                .header("Authorization", "Bearer $newToken")
                .build()
        }
    }
}
