package com.tutorial.rockty.domain.pagination

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PaginationManagerTest {

    @Test
    fun `loadNextPage updates state with items on success`() = runTest {
        val expectedItems = listOf("Item 1", "Item 2")
        val fetcher: suspend (Int, String?) -> Result<PaginatedResult<String>> = { _, _ ->
            Result.success(PaginatedResult(items = expectedItems, cursor = "next_cursor", hasMore = true))
        }
        val manager = PaginationManager(fetcher)

        manager.loadNextPage()

        val state = manager.state.value
        assertEquals(expectedItems, state.items)
        assertEquals("next_cursor", state.cursor)
        assertTrue(state.hasMore)
        assertFalse(state.isLoading)
    }

    @Test
    fun `loadNextPage appends items to existing list`() = runTest {
        var callCount = 0
        val fetcher: suspend (Int, String?) -> Result<PaginatedResult<String>> = { _, _ ->
            callCount++
            if (callCount == 1) {
                Result.success(PaginatedResult(items = listOf("A"), cursor = "1", hasMore = true))
            } else {
                Result.success(PaginatedResult(items = listOf("B"), cursor = "2", hasMore = false))
            }
        }
        val manager = PaginationManager(fetcher)

        manager.loadNextPage() // Load page 1
        manager.loadNextPage() // Load page 2

        val state = manager.state.value
        assertEquals(listOf("A", "B"), state.items)
        assertFalse(state.hasMore)
    }

    @Test
    fun `loadNextPage handles error correctly`() = runTest {
        val error = Exception("Network error")
        val fetcher: suspend (Int, String?) -> Result<PaginatedResult<String>> = { _, _ ->
            Result.failure(error)
        }
        val manager = PaginationManager(fetcher)

        manager.loadNextPage()

        val state = manager.state.value
        assertTrue(state.items.isEmpty())
        assertEquals(error, state.error)
        assertFalse(state.isLoading)
    }

    @Test
    fun `loadNextPage prevents duplicate calls while loading`() = runTest {
        var callCount = 0
        val fetcher: suspend (Int, String?) -> Result<PaginatedResult<String>> = { _, _ ->
            delay(100) // Simulate network delay
            callCount++
            Result.success(PaginatedResult(items = listOf("Item"), hasMore = true))
        }
        val manager = PaginationManager(fetcher)

        val job1 = launch { manager.loadNextPage() }
        val job2 = launch { manager.loadNextPage() }

        job1.join()
        job2.join()

        assertEquals(1, callCount)
    }

    @Test
    fun `reset clears state`() = runTest {
        val fetcher: suspend (Int, String?) -> Result<PaginatedResult<String>> = { _, _ ->
            Result.success(PaginatedResult(items = listOf("A"), hasMore = true))
        }
        val manager = PaginationManager(fetcher)
        manager.loadNextPage()

        manager.reset()

        val state = manager.state.value
        assertTrue(state.items.isEmpty())
        assertEquals(null, state.cursor)
        assertTrue(state.hasMore)
    }
}
