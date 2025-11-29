package com.tutorial.rockty.di

import com.tutorial.rockty.domain.repository.LaunchRepository
import com.tutorial.rockty.domain.usecase.GetLaunchDetailsUseCase
import com.tutorial.rockty.domain.usecase.GetLaunchesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DomainModule {
    @Provides
    @Singleton
    fun provideObserveLaunchesUseCase(repository: LaunchRepository): GetLaunchesUseCase {
        return GetLaunchesUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetLaunchDetailsUseCase(repository: LaunchRepository): GetLaunchDetailsUseCase {
        return GetLaunchDetailsUseCase(repository)
    }
}
