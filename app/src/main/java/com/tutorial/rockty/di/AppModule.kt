package com.tutorial.rockty.di

import com.apollographql.apollo.ApolloClient
import com.tutorial.rockty.data.repository.LaunchRepositoryImpl
import com.tutorial.rockty.domain.repository.LaunchRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class IoDispatcher

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private const val GRAPHQL_URL = "https://apollo-fullstack-tutorial.herokuapp.com/graphql"

    @Provides
    @IoDispatcher
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @Singleton
    fun provideApolloClient(): ApolloClient {
        return ApolloClient.Builder()
            .serverUrl(GRAPHQL_URL)
            .build()
    }

    @Provides
    @Singleton
    fun provideLaunchRepository(
        apolloClient: ApolloClient,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): LaunchRepository {
        return LaunchRepositoryImpl(apolloClient, ioDispatcher)
    }
}
