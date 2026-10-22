package com.fitmate.data.remote

import com.fitmate.data.remote.dto.ErrorResponseDto
import com.fitmate.domain.model.FitMateException
import kotlinx.coroutines.CancellationException
import kotlinx.serialization.json.Json
import retrofit2.HttpException
import java.io.IOException

internal val FitMateJson = Json {
    ignoreUnknownKeys = true
    explicitNulls = false
}

private const val NETWORK_ERROR_MESSAGE = "네트워크 연결을 확인해주세요."
private const val UNKNOWN_ERROR_MESSAGE = "잠시 후 다시 시도해주세요."

// API 호출 결과를 Result로 감싼다. 실패는 사용자에게 보여줄 문구를 담은 FitMateException으로 바꾼다
internal suspend fun <T> apiCall(block: suspend () -> T): Result<T> =
    try {
        Result.success(block())
    } catch (e: CancellationException) {
        throw e
    } catch (e: HttpException) {
        val error = e.response()?.errorBody()?.string()?.let {
            runCatching { FitMateJson.decodeFromString<ErrorResponseDto>(it) }.getOrNull()
        }
        Result.failure(FitMateException(error?.code, error?.message ?: UNKNOWN_ERROR_MESSAGE, e))
    } catch (e: IOException) {
        Result.failure(FitMateException(null, NETWORK_ERROR_MESSAGE, e))
    } catch (e: Exception) {
        Result.failure(FitMateException(null, UNKNOWN_ERROR_MESSAGE, e))
    }
