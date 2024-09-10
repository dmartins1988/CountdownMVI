package com.example.countdownmvi.domain

import android.os.CountDownTimer
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class CountdownTimerRepositoryImpl : CountdownTimerRepository {

    private lateinit var countDownTimer: CountDownTimer
    private var currentTime = 60000L

    override val countdownFlow: Flow<Long> = callbackFlow {
        countDownTimer = object : CountDownTimer(currentTime, 1000L) {
            override fun onTick(millisUntilFinished: Long) {
                currentTime = millisUntilFinished
                trySend(currentTime)
            }

            override fun onFinish() {
                trySend(0)
                cancel()
            }
        }

        awaitClose {
            countDownTimer.cancel()
        }
    }

    override fun startCountdown() {
        if (this::countDownTimer.isInitialized) {
            countDownTimer.start()
        }
    }

    override fun pauseCountdown() {
        if (this::countDownTimer.isInitialized) {
            countDownTimer.cancel()
        }
    }

    override fun resetCountdown() {
        currentTime = 60000L
    }
}