package com.tutorial.rockty.domain.usecase

import com.tutorial.rockty.domain.model.Launch
import com.tutorial.rockty.domain.repository.LaunchRepository

class GetLaunchDetailsUseCase(private val repository: LaunchRepository) {
     suspend operator fun invoke(id: String): Result<Launch> {
        return repository.getLaunch(id)
    }
}
