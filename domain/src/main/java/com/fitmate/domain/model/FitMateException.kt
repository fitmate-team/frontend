package com.fitmate.domain.model

// 서버가 내려준 에러({ code, message }) 또는 네트워크 오류. message는 사용자에게 그대로 보여줄 수 있는 문구
class FitMateException(
    val code: String?,
    override val message: String,
    cause: Throwable? = null
) : Exception(message, cause)
