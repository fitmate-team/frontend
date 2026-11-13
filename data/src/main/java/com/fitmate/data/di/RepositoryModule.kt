package com.fitmate.data.di

import com.fitmate.data.repository.AuthRepositoryImpl
import com.fitmate.data.repository.CatalogRepositoryImpl
import com.fitmate.data.repository.MemberRepositoryImpl
import com.fitmate.data.repository.RoutineRepositoryImpl
import com.fitmate.data.repository.WorkoutRecordRepositoryImpl
import com.fitmate.domain.repository.AuthRepository
import com.fitmate.domain.repository.CatalogRepository
import com.fitmate.domain.repository.MemberRepository
import com.fitmate.domain.repository.RoutineRepository
import com.fitmate.domain.repository.WorkoutRecordRepository
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

    @Binds
    @Singleton
    abstract fun bindRoutineRepository(impl: RoutineRepositoryImpl): RoutineRepository

    @Binds
    @Singleton
    abstract fun bindMemberRepository(impl: MemberRepositoryImpl): MemberRepository

    @Binds
    @Singleton
    abstract fun bindWorkoutRecordRepository(impl: WorkoutRecordRepositoryImpl): WorkoutRecordRepository
}
