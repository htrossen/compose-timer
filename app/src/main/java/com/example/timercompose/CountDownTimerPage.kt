package com.example.timercompose

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.timercompose.ui.theme.TimerComposeTheme
import androidx.compose.runtime.remember
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope

@Composable
fun CountDownTimerPage(
    hours: Int,
    minutes: Int,
    seconds: Int,
    reset: () -> Unit
) {

    var ticker by remember { mutableStateOf(Ticker(hours, minutes, seconds))}
    val scope = rememberCoroutineScope()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Counting down!") },
                navigationIcon = { Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Back button",
                    modifier = Modifier
                        .clickable {
                            ticker.pause()
                            reset()
                        }
                        .padding(8.dp)
                )}
            )},
        content = {
            scope.launch { ticker.start() }
            Countdown(ticker = ticker, scope = scope)
        }
    )
}

@Composable
fun Countdown(
    ticker: Ticker,
    scope: CoroutineScope
) {
    val hideHours = ticker.hoursRemaining == 0
    val hideMinutes = hideHours && ticker.minutesRemaining == 0

    // Seconds
    AnimatedProgress(
        progress = ticker.secondsProgress,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 64.dp, end = 64.dp, top = 96.dp),
        color = MaterialTheme.colors.secondary
    )

    // Minutes
    if (!hideMinutes) {
        AnimatedProgress(
            progress = ticker.minutesProgress,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 48.dp, end = 48.dp, top = 80.dp),
            color = MaterialTheme.colors.primary
        )
    }

    // Hours
    if (!hideHours) {
        AnimatedProgress(
            progress = ticker.hoursProgress,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 32.dp, end = 32.dp, top = 64.dp),
            color = MaterialTheme.colors.primaryVariant
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 192.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "${ticker.hoursRemaining} : ${ticker.minutesRemaining} : ${ticker.secondsRemaining}"
        )

        FloatingActionButton(
            onClick = {
                if (ticker.isPaused) {
                    scope.launch { ticker.start() }
                } else { ticker.pause() } },
            modifier = Modifier.padding(top = 32.dp)
        ) {
            if (ticker.isPaused) {
                Icon(
                    imageVector = Icons.Filled.PlayArrow,
                    contentDescription = "Start timer",
                    Modifier
                        .size(32.dp)
                )
            } else {
                Icon(
                    painter = painterResource(id = R.drawable.ic_pause),
                    contentDescription = "Pause timer",
                    Modifier
                        .size(32.dp)
                )
            }
        }
    }
}

@Composable
fun AnimatedProgress(
    progress: Float,
    modifier: Modifier,
    color: Color,
) {
    CircularProgressIndicator(
        progress = progress,
        modifier = modifier,
        color = color
    )
}

@Preview
@Composable
fun CountDownTimerPagePreview() {
    TimerComposeTheme {
        CountDownTimerPage(0, 1, 0, {})
    }
}