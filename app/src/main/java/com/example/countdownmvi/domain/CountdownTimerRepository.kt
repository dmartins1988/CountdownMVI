package com.example.countdownmvi.domain

import kotlinx.coroutines.flow.Flow


interface CountdownTimerRepository {
    val countdownFlow: Flow<Long>
    fun startCountdown()
    fun pauseCountdown()
    fun resetCountdown()
}