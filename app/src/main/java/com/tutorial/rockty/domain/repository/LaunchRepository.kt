package com.tutorial.rockty.domain.repository

import com.tutorial.rockty.domain.model.Launch
import com.tutorial.rockty.domain.pagination.PaginatedResult

interface LaunchRepository {
    suspend fun fetchLaunches(pageSize: Int, cursor: String?): Result<PaginatedResult<Launch>>
    suspend fun getLaunch(id: String): Result<Launch>
}
