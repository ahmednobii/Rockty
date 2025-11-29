package com.tutorial.rockty.ui.list

import com.tutorial.rockty.domain.model.Launch
import com.tutorial.rockty.domain.pagination.PaginatedResult
import com.tutorial.rockty.domain.usecase.GetLaunchesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

@OptIn(ExperimentalCoroutinesApi::class)
class LaunchListViewModelTest {

    private lateinit var getLaunchesUseCase: GetLaunchesUseCase
    private lateinit var viewModel: LaunchListViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        getLaunchesUseCase = mock(GetLaunchesUseCase::class.java)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init triggers loadMore and updates state`() = runTest {
        val expectedItems = listOf(Launch("1", "Site", null, null))
        `when`(getLaunchesUseCase(20, null)).thenReturn(
            Result.success(PaginatedResult(expectedItems, "cursor", true))
        )

        viewModel = LaunchListViewModel(getLaunchesUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.state.first()
        assertEquals(expectedItems, state.items)
        assertEquals("cursor", state.cursor)
    }

    @Test
    fun `onLaunchClicked emits NavigateToDetail effect`() = runTest {
        `when`(getLaunchesUseCase(20, null)).thenReturn(Result.success(PaginatedResult(emptyList(), null, false)))
        viewModel = LaunchListViewModel(getLaunchesUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        val effects = mutableListOf<LaunchListEffect>()
        val job = launch {
            viewModel.effect.collect { effects.add(it) }
        }
        
        viewModel.handleIntent(LaunchListIntent.OnLaunchClicked("123"))
        testDispatcher.scheduler.advanceUntilIdle()
        
        assertEquals(LaunchListEffect.NavigateToDetail("123"), effects.first())
        job.cancel()
    }

    @Test
    fun `Refresh intent resets state and loads more`() = runTest {
        `when`(getLaunchesUseCase(20, null)).thenReturn(
            Result.success(PaginatedResult(listOf(Launch("1", "A", null, null)), "c1", true))
        )
        viewModel = LaunchListViewModel(getLaunchesUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(1, viewModel.state.value.items.size)

        `when`(getLaunchesUseCase(20, null)).thenReturn(
            Result.success(PaginatedResult(listOf(Launch("2", "B", null, null)), "c2", true))
        )

        viewModel.handleIntent(LaunchListIntent.Refresh)
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.state.value
        assertEquals(1, state.items.size)
        assertEquals("2", state.items[0].id)
        assertEquals("c2", state.cursor)
    }
}
