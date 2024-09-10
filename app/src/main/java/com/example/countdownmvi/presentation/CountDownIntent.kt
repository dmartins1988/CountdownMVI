package com.example.countdownmvi.presentation

sealed class CountDownIntent {
    data object Start : CountDownIntent()
    data object Pause : CountDownIntent()
    data object Reset : CountDownIntent()
}