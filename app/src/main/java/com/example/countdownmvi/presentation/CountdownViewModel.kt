package com.example.countdownmvi.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.countdownmvi.domain.CountdownTimerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CountdownViewModel @Inject constructor(
    private val repository: CountdownTimerRepository
) : ViewModel() {

    private val _state = MutableStateFlow(CountdownState())
    val state: StateFlow<CountdownState> = _state.asStateFlow()

    fun dispatchIntent(intent: CountDownIntent) {
        when (intent) {
            CountDownIntent.Pause -> pauseTimer()
            CountDownIntent.Reset -> resetTimer()
            CountDownIntent.Start -> startTimer()
        }
    }

    private fun startTimer() {
        viewModelScope.launch {
            repository.countdownFlow.collect { timeLeft ->
                _state.update { it.copy(timeLeft = timeLeft, isRunning = true) }
            }
        }
        repository.startCountdown()
    }

    private fun resetTimer() {
        repository.resetCountdown()
        _state.update { it.copy(isRunning = false) }
    }

    private fun pauseTimer() {
        repository.pauseCountdown()
        _state.update { it.copy(isRunning = false) }
    }
}