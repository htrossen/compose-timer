package com.example.timercompose

import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.ticker
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class Ticker(
    val hours: Int,
    val minutes: Int,
    val seconds: Int
) {

    var isPaused by mutableStateOf(true)
    var secondsProgress by mutableStateOf(1f)
    var secondsRemaining by mutableStateOf( seconds)
    var minutesProgress by mutableStateOf(1f)
    var minutesRemaining by mutableStateOf( minutes)
    var hoursProgress by mutableStateOf(1f)
    var hoursRemaining by mutableStateOf( hours)

    private lateinit var secondsChannel: ReceiveChannel<Unit>

    suspend fun start() {

        secondsChannel = ticker(
            delayMillis = 250, // tick every 1/4 of a second
            initialDelayMillis = 250 // Wait a 1/4 of a second for the first tick
        )

        isPaused = false

        for (event in secondsChannel) {
            secondsProgress -= .25f
            if (secondsProgress == 0f) {
                secondsProgress = 1f
                minutesProgress -= 1f/60
                hoursProgress -= 1f/3600

                if (secondsRemaining != 0) { // decrease seconds
                    secondsRemaining -= 1
                } else if (secondsRemaining == 0 && minutesRemaining > 0) { // decrease minutes
                    minutesProgress = 1f
                    minutesRemaining -= 1
                    secondsRemaining = 59

                } else if (secondsRemaining == 0 && hoursRemaining > 0) { // decrease hours
                    hoursProgress = 1f
                    minutesProgress = 1f
                    hoursRemaining -= 1
                    minutesRemaining = 59
                    secondsRemaining = 59
                } else {
                    secondsChannel.cancel()
                }
            }
        }

    }

    fun pause() {
        isPaused = true
        secondsChannel.cancel()
    }
}