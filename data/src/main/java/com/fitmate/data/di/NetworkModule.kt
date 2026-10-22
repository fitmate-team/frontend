package com.fitmate.data.di

import com.fitmate.data.BuildConfig
import com.fitmate.data.remote.AuthInterceptor
import com.fitmate.data.remote.FitMateJson
import com.fitmate.data.remote.TokenAuthenticator
import com.fitmate.data.remote.api.AuthApi
import com.fitmate.data.remote.api.CatalogApi
import com.fitmate.data.remote.api.TokenApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

// 토큰 재발급 전용 (인증 헤더 / Authenticator 없음)
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class TokenClient

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private fun baseClientBuilder() = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .addInterceptor(
            HttpLoggingInterceptor().apply {
                level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
            }
        )

    private fun retrofit(client: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .client(client)
        .addConverterFactory(FitMateJson.asConverterFactory("application/json".toMediaType()))
        .build()

    @Provides
    @Singleton
    @TokenClient
    fun provideTokenRetrofit(): Retrofit = retrofit(baseClientBuilder().build())

    @Provides
    @Singleton
    fun provideTokenApi(@TokenClient retrofit: Retrofit): TokenApi = retrofit.create(TokenApi::class.java)

    @Provides
    @Singleton
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor,
        tokenAuthenticator: TokenAuthenticator
    ): OkHttpClient = baseClientBuilder()
        .addInterceptor(authInterceptor)
        .authenticator(tokenAuthenticator)
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit = retrofit(client)

    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi = retrofit.create(AuthApi::class.java)

    @Provides
    @Singleton
    fun provideCatalogApi(retrofit: Retrofit): CatalogApi = retrofit.create(CatalogApi::class.java)
}
