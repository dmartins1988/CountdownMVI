package com.example.countdownmvi.presentation

data class CountdownState(
    val timeLeft: Long = 60000L,
    val isRunning: Boolean = false,
) {
    val displayTime: Int = (timeLeft / 1000L).toInt()

    val progressPercentage: Float = displayTime / 60.toFloat()
}
