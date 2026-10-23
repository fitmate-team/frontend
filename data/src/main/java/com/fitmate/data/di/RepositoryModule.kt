package com.fitmate.data.di

import com.fitmate.data.repository.AuthRepositoryImpl
import com.fitmate.data.repository.CatalogRepositoryImpl
import com.fitmate.domain.repository.AuthRepository
import com.fitmate.domain.repository.CatalogRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindCatalogRepository(impl: CatalogRepositoryImpl): CatalogRepository
}
