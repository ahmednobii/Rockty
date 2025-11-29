package com.tutorial.rockty.ui.detail

import com.tutorial.rockty.domain.model.Launch

data class LaunchDetailUiState(
    val launch: Launch? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed interface LaunchDetailIntent {
    data class Refresh(val id: String) : LaunchDetailIntent
    data object OnBackClicked : LaunchDetailIntent
}

sealed interface LaunchDetailEffect {
    data object NavigateBack : LaunchDetailEffect
}
