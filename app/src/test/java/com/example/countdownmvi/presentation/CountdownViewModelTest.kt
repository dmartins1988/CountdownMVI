package com.example.countdownmvi.presentation

import app.cash.turbine.test
import com.example.countdownmvi.domain.CountdownTimerRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CountdownViewModelTest {

    private lateinit var viewModel: CountdownViewModel
    private lateinit var repository: CountdownTimerRepository
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        repository = mockk(relaxed = true)
        viewModel = CountdownViewModel(repository)
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `dispatch Start intent starts the timer and updates state`() = runTest {
        // Given
        val countdownFlow = flowOf(60000L, 59000L, 58000L)
        coEvery { repository.countdownFlow } returns countdownFlow

        // When
        viewModel.dispatchIntent(CountDownIntent.Start)

        // Then
        viewModel.state.test {
            assertEquals(CountdownState(timeLeft = 60000L, isRunning = false), awaitItem()) //initial state
            assertEquals(CountdownState(timeLeft = 60000L, isRunning = true), awaitItem())
            assertEquals(CountdownState(timeLeft = 59000L, isRunning = true), awaitItem())
            assertEquals(CountdownState(timeLeft = 58000L, isRunning = true), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        coVerify { repository.startCountdown() }
    }

    @Test
    fun `dispatch Pause intent pauses the timer and updates state`() = runTest {
        // Given the timer is running
        viewModel.dispatchIntent(CountDownIntent.Start)
        coEvery { repository.pauseCountdown() } answers { /* Simulate pausing */ }

        // When
        viewModel.dispatchIntent(CountDownIntent.Pause)

        // Then
        viewModel.state.test {
            assertEquals(CountdownState(isRunning = false), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        coVerify { repository.pauseCountdown() }
    }

    @Test
    fun `dispatch Reset intent resets the timer and updates state`() = runTest {
        // Given the timer is running
        viewModel.dispatchIntent(CountDownIntent.Start)

        // When
        viewModel.dispatchIntent(CountDownIntent.Reset)

        // Then
        viewModel.state.test {
            assertEquals(CountdownState(timeLeft = 60000L, isRunning = false), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        coVerify { repository.resetCountdown() }
    }
}