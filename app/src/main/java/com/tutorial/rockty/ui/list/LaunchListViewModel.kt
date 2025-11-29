package com.tutorial.rockty.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tutorial.rockty.domain.pagination.PaginationManager
import com.tutorial.rockty.domain.usecase.GetLaunchesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LaunchListViewModel @Inject constructor(
    private val getLaunchesUseCase: GetLaunchesUseCase
) : ViewModel() {

    private val paginationManager = PaginationManager { pageSize, cursor ->
        getLaunchesUseCase(pageSize, cursor)
    }

    val state = paginationManager.state

    private val _effect = MutableSharedFlow<LaunchListEffect>()
    val effect = _effect.asSharedFlow()

    init {
        handleIntent(LaunchListIntent.LoadMore)
    }

    fun handleIntent(intent: LaunchListIntent) {
        when (intent) {
            is LaunchListIntent.LoadMore -> loadMore()
            is LaunchListIntent.Refresh -> {
                viewModelScope.launch {
                    paginationManager.reset()
                    loadMore()
                }
            }
            is LaunchListIntent.OnLaunchClicked -> {
                viewModelScope.launch {
                    _effect.emit(LaunchListEffect.NavigateToDetail(intent.id))
                }
            }
        }
    }

    private fun loadMore() {
        viewModelScope.launch {
            paginationManager.loadNextPage()
        }
    }
}
