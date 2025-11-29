package com.tutorial.rockty.data.repository

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Optional
import com.tutorial.rockty.data.mapper.toDomain
import com.tutorial.rockty.di.IoDispatcher
import com.tutorial.rockty.domain.model.Launch
import com.tutorial.rockty.domain.pagination.PaginatedResult
import com.tutorial.rockty.domain.repository.LaunchRepository
import com.tutorial.rockty.graphql.LaunchDetailsQuery
import com.tutorial.rockty.graphql.LaunchesQuery
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class LaunchRepositoryImpl @Inject constructor(
    private val apolloClient: ApolloClient,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : LaunchRepository {

    override suspend fun getLaunch(id: String): Result<Launch> = withContext(dispatcher) {
        runCatching {
            // Network Request
            val response = apolloClient.query(LaunchDetailsQuery(id)).execute()
            
            check(!response.hasErrors()) {
                response.errors?.firstOrNull()?.message ?: "Unknown GraphQL error"
            }

            val data = checkNotNull(response.data?.launch) {
                "Launch with id $id not found"
            }

            data.toDomain()
        }.onFailure { e ->
            if (e is CancellationException) throw e
        }
    }

    override suspend fun fetchLaunches(
        pageSize: Int,
        cursor: String?
    ): Result<PaginatedResult<Launch>> = withContext(dispatcher) {
        runCatching {
            val response = apolloClient.query(
                LaunchesQuery(
                    pageSize = pageSize,
                    after = Optional.presentIfNotNull(cursor)
                )
            ).execute()

            check(!response.hasErrors()) {
                response.errors?.firstOrNull()?.message ?: "Unknown GraphQL error"
            }

            val data = checkNotNull(response.data?.launches) {
                "No data returned from server"
            }

            data.toDomain()
        }.onFailure { e ->
            if (e is CancellationException) throw e
        }
    }
}
