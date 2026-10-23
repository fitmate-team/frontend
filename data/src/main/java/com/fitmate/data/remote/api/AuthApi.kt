package com.fitmate.data.remote.api

import com.fitmate.data.remote.dto.LoginIdCheckResponseDto
import com.fitmate.data.remote.dto.LoginRequestDto
import com.fitmate.data.remote.dto.LoginResponseDto
import com.fitmate.data.remote.dto.SignUpRequestDto
import com.fitmate.data.remote.dto.SignUpResponseDto
import com.fitmate.data.remote.dto.TokenReissueRequestDto
import com.fitmate.data.remote.dto.TokenReissueResponseDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthApi {

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequestDto): LoginResponseDto

    @GET("api/members/id-check")
    suspend fun checkLoginId(@Query("loginId") loginId: String): LoginIdCheckResponseDto

    @POST("api/members/signup")
    suspend fun signUp(@Body request: SignUpRequestDto): SignUpResponseDto
}

// 토큰 재발급은 OkHttp Authenticator 안에서 동기로 호출하고, 인증 헤더가 붙지 않는 별도 클라이언트를 쓴다
interface TokenApi {

    @POST("api/auth/reissue")
    fun reissue(@Body request: TokenReissueRequestDto): Call<TokenReissueResponseDto>
}
