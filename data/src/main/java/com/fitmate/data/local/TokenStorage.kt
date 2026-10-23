package com.fitmate.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

private val Context.tokenDataStore by preferencesDataStore(name = "auth_tokens")

// Access / Refresh 토큰 보관. 인터셉터에서 매 요청마다 읽기 때문에 메모리에도 들고 있는다
@Singleton
class TokenStorage @Inject constructor(
    @ApplicationContext private val context: Context
) {
    @Volatile
    private var cachedAccessToken: String? = null

    @Volatile
    private var loaded = false

    suspend fun accessToken(): String? {
        if (!loaded) load()
        return cachedAccessToken
    }

    suspend fun refreshToken(): String? =
        context.tokenDataStore.data.first()[REFRESH_TOKEN]

    suspend fun saveTokens(accessToken: String, refreshToken: String) {
        context.tokenDataStore.edit {
            it[ACCESS_TOKEN] = accessToken
            it[REFRESH_TOKEN] = refreshToken
        }
        cachedAccessToken = accessToken
        loaded = true
    }

    suspend fun saveAccessToken(accessToken: String) {
        context.tokenDataStore.edit { it[ACCESS_TOKEN] = accessToken }
        cachedAccessToken = accessToken
        loaded = true
    }

    suspend fun clear() {
        context.tokenDataStore.edit { it.clear() }
        cachedAccessToken = null
        loaded = true
    }

    private suspend fun load() {
        cachedAccessToken = context.tokenDataStore.data.first()[ACCESS_TOKEN]
        loaded = true
    }

    private companion object {
        val ACCESS_TOKEN = stringPreferencesKey("access_token")
        val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
    }
}
