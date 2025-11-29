package com.tutorial.rockty.domain.model

data class PaginationState<T>(
    val items: List<T> = emptyList(),
    val cursor: String? = null,
    val hasMore: Boolean = true,
    val isLoading: Boolean = false,
    val error: Throwable? = null
)
