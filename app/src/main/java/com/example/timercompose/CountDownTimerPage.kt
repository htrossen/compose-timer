package com.example.timercompose

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.timercompose.ui.theme.TimerComposeTheme
import androidx.compose.runtime.remember
import java.util.Timer
import java.util.TimerTask
import androidx.compose.runtime.State
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.LinearEasing
import androidx.compose.material.MaterialTheme
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.ticker
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.ui.graphics.Color


@Composable
fun CountDownTimerPage(
    hours: Int,
    minutes: Int,
    seconds: Int,
    reset: () -> Unit
) {
    var secondsProgress = remember { mutableStateOf(1f) }
    var secondsRemaining = remember { mutableStateOf( seconds) }
    var minutesProgress = remember { mutableStateOf(1f) }
    var minutesRemaining = remember { mutableStateOf( minutes) }
    var hoursProgress = remember { mutableStateOf(1f) }
    var hoursRemaining = remember { mutableStateOf( hours) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Counting down!") },
                navigationIcon = { Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Back button",
                    modifier = Modifier
                        .clickable { reset() }
                        .padding(8.dp)
                )}
            )},
        content = {
            Countdown(hoursRemaining, hoursProgress, minutesRemaining, minutesProgress, secondsRemaining, secondsProgress)
        }
    )
}

@Composable
fun Countdown(
    hours: MutableState<Int>,
    hoursProgress: MutableState<Float>,
    minutes: MutableState<Int>,
    minutesProgress: MutableState<Float>,
    seconds: MutableState<Int>,
    secondsProgress: MutableState<Float>
) {
    val hideHours = hours.value == 0
    val hideMinutes = minutes.value == 0

    val scope = rememberCoroutineScope()

    var secondsChannel: ReceiveChannel<Unit> = ticker(
        delayMillis = 250, // tick every 1/4 of a second
        initialDelayMillis = 250 // Wait a 1/4 of a second for the first tick
    )

    scope.launch {
        for (event in secondsChannel) {
            secondsProgress.value -= .25f
            if (secondsProgress.value == 0f) {
                secondsProgress.value = 1f
                minutesProgress.value -= 1f/60
                hoursProgress.value -= 1f/3600

                if (seconds.value != 0) {
                    seconds.value -= 1
                } else if (seconds.value == 0 && minutes.value > 0) { // decrease minutes
                    minutesProgress.value = 1f
                    minutes.value -= 1
                    seconds.value = 59

                } else if (seconds.value == 0 && hours.value > 0) { // decrease hours
                    hoursProgress.value = 1f
                    minutesProgress.value = 1f
                    hours.value -= 1
                    minutes.value = 59
                    seconds.value = 59
                } else {
                    secondsChannel.cancel()
                }
            }
        }
    }

    // Seconds
    AnimatedProgress(
        progress = secondsProgress,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 64.dp, end = 64.dp, top = 96.dp),
        color = MaterialTheme.colors.secondary
    )

    // Minutes
    if (!hideMinutes) {
        AnimatedProgress(
            progress = minutesProgress,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 48.dp, end = 48.dp, top = 80.dp),
            color = MaterialTheme.colors.primary
        )
    }

    // Hours
    if (!hideHours) {
        AnimatedProgress(
            progress = hoursProgress,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 32.dp, end = 32.dp, top = 64.dp),
            color = MaterialTheme.colors.primaryVariant
        )
    }

    Column(
        modifier = Modifier.fillMaxWidth().padding(top = 192.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(text = "${hours.value} : ${minutes.value} : ${seconds.value}")

        FloatingActionButton(
            onClick = { secondsChannel.cancel() }, //timer.cancel()
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_pause),
                contentDescription = "Pause timer",
                Modifier
                    .size(32.dp)
            )
        }
    }
}

@Composable
fun AnimatedProgress(
    progress: MutableState<Float>,
    modifier: Modifier,
    color: Color,
) {
    CircularProgressIndicator(
        progress = progress.value,
        modifier = modifier,
        color = color
    )
}

@Preview
@Composable
fun CountDownTimerPagePreview() {
    TimerComposeTheme {
        CountDownTimerPage(0, 0, 30, {})
    }
}