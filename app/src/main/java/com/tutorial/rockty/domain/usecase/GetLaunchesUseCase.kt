package com.tutorial.rockty.domain.usecase

import com.tutorial.rockty.domain.model.Launch
import com.tutorial.rockty.domain.pagination.PaginatedResult
import com.tutorial.rockty.domain.repository.LaunchRepository


class GetLaunchesUseCase(private val repository: LaunchRepository) {
    suspend operator fun invoke(pageSize: Int, cursor: String?): Result<PaginatedResult<Launch>> {
        return repository.fetchLaunches(pageSize,cursor)
    }
}
