package com.tutorial.rockty.domain.pagination

import com.tutorial.rockty.domain.model.PaginationState
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class PaginatedResult<T>(
    val items: List<T>,
    val cursor: String? = null,
    val hasMore: Boolean
)

class PaginationManager<T>(
    private val fetcher: suspend (pageSize: Int, cursor: String?) -> Result<PaginatedResult<T>>
) {
    private val _state = MutableStateFlow(PaginationState<T>())
    val state: StateFlow<PaginationState<T>> = _state.asStateFlow()
    
    private val mutex = Mutex()

    suspend fun loadNextPage(pageSize: Int = 20) {
        val shouldLoad = mutex.withLock {
            val currentState = _state.value
            if (currentState.isLoading || !currentState.hasMore) {
                false
            } else {
                _state.update { it.copy(isLoading = true, error = null) }
                true
            }
        }

        if (!shouldLoad) return

        performLoad(pageSize)
    }

    private suspend fun performLoad(pageSize: Int) {
        val cursorToLoad = _state.value.cursor

        fetcher(pageSize, cursorToLoad)
            .onSuccess { result ->
                if (result == null) {
                    // Should not happen if Result<T> wrapper is respected, but Mockito might return null for the wrapper itself
                    _state.update { it.copy(isLoading = false, error = NullPointerException("PaginatedResult was null")) }
                    return
                }
                
                _state.update { state ->
                    state.copy(
                        isLoading = false,
                        items = state.items + result.items,
                        cursor = result.cursor,
                        hasMore = result.hasMore
                    )
                }
            }
            .onFailure { error ->
                _state.update { it.copy(isLoading = false, error = error) }
            }
    }

    fun reset() {
        _state.value = PaginationState()
    }
}
