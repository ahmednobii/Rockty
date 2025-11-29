package com.tutorial.rockty.ui.detail

import androidx.lifecycle.SavedStateHandle
import com.tutorial.rockty.domain.model.Launch
import com.tutorial.rockty.domain.repository.LaunchRepository
import com.tutorial.rockty.domain.usecase.GetLaunchDetailsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

@OptIn(ExperimentalCoroutinesApi::class)
class LaunchDetailViewModelTest {

    private lateinit var repository: LaunchRepository
    private lateinit var getLaunchDetailsUseCase: GetLaunchDetailsUseCase
    private lateinit var viewModel: LaunchDetailViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = mock(LaunchRepository::class.java)
        getLaunchDetailsUseCase = GetLaunchDetailsUseCase(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
    
    private fun createViewModel(id: String) {
        val realHandle = SavedStateHandle(mapOf("id" to id))
        viewModel = LaunchDetailViewModel(realHandle, getLaunchDetailsUseCase)
    }

    @Test
    fun `init loads launch details successfully`() = runTest {
        val id = "101"
        val expectedLaunch = Launch(id, "Site A", null, null)
        
        `when`(repository.getLaunch(anyString())).thenReturn(Result.success(expectedLaunch))

        createViewModel(id)
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(expectedLaunch, state.launch)
        assertFalse(state.isLoading)
        assertEquals(null, state.error)
        
        val captor = ArgumentCaptor.forClass(String::class.java)
        verify(repository).getLaunch(captor.capture() ?: "")
        
        assertNotNull(captor.value)
    }

    @Test
    fun `init handles error state`() = runTest {
        val id = "999"
        val errorMsg = "Network Error"
        `when`(repository.getLaunch(anyString())).thenReturn(Result.failure(Exception(errorMsg)))

        createViewModel(id)
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(null, state.launch)
        assertFalse(state.isLoading)
        assertEquals(errorMsg, state.error)
        
        verify(repository).getLaunch(anyString())
    }

    @Test
    fun `OnBackClicked emits NavigateBack effect`() = runTest {
        val id = "101"
        `when`(repository.getLaunch(anyString())).thenReturn(Result.success(Launch(id, "Site", null, null)))
        createViewModel(id)
        testDispatcher.scheduler.advanceUntilIdle()

        val effects = mutableListOf<LaunchDetailEffect>()
        val job = launch { viewModel.effect.collect { effects.add(it) } }

        viewModel.handleIntent(LaunchDetailIntent.OnBackClicked)
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(LaunchDetailEffect.NavigateBack, effects.first())
        job.cancel()
    }
    
    @Test
    fun `Refresh intent reloads data`() = runTest {
        val id = "101"
        val launchA = Launch(id, "Site A", null, null)
        val launchB = Launch(id, "Site B", null, null)
        
        `when`(repository.getLaunch(anyString()))
            .thenReturn(Result.success(launchA))
            .thenReturn(Result.success(launchB))

        createViewModel(id)
        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals(launchA, viewModel.uiState.value.launch)

        viewModel.handleIntent(LaunchDetailIntent.Refresh(id))
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(launchB, viewModel.uiState.value.launch)
    }
}
