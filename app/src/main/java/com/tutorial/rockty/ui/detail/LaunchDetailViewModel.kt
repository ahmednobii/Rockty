package com.tutorial.rockty.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.tutorial.rockty.domain.usecase.GetLaunchDetailsUseCase
import com.tutorial.rockty.navigation.LaunchDetailRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LaunchDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getLaunchDetailsUseCase: GetLaunchDetailsUseCase
) : ViewModel() {

    private val launchId = savedStateHandle.toRoute<LaunchDetailRoute>().id

    private val _uiState = MutableStateFlow(LaunchDetailUiState())
    val uiState: StateFlow<LaunchDetailUiState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<LaunchDetailEffect>()
    val effect = _effect.asSharedFlow()

    init {
        handleIntent(LaunchDetailIntent.Refresh(launchId))
    }

    fun handleIntent(intent: LaunchDetailIntent) {
        when (intent) {
            is LaunchDetailIntent.Refresh -> fetchLaunchDetails(intent.id)
            is LaunchDetailIntent.OnBackClicked -> {
                viewModelScope.launch {
                    _effect.emit(LaunchDetailEffect.NavigateBack)
                }
            }
        }
    }

    private fun fetchLaunchDetails(id: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            getLaunchDetailsUseCase(id)
                .onSuccess { launch ->
                    _uiState.update { 
                        it.copy(launch = launch, isLoading = false, error = null) 
                    }
                }
                .onFailure { error ->
                    val errorMessage = error.message ?: "Unknown error"
                    _uiState.update { 
                        it.copy(isLoading = false, error = errorMessage) 
                    }
                }
        }
    }
}
