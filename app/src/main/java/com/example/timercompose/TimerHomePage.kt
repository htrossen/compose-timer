package com.example.timercompose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.timercompose.ui.theme.TimerComposeTheme
import androidx.compose.runtime.remember
import androidx.compose.material.Icon
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.MutableState
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun TimerHomePage(
    startTimer: (Triple<Int, Int, Int>) -> Unit
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text(text = "Set your timer") }) },
        content = {
            SetTime(startTimer)
        }
    )
}

@Composable
fun SetTime(startTimer: (Triple<Int, Int, Int>) -> Unit) {
    val hours = remember { mutableStateOf(0) }
    val minutes = remember { mutableStateOf(0) }
    val seconds = remember { mutableStateOf(0) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 192.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TimeSelector(hours, 23, "Hours")
            Text(
                text = " : "
            )
            TimeSelector(minutes, 59, "Minutes")
//            Text(
//                text = " : "
//            )
//            TimeSelector(seconds, 59, "Seconds")
        }

        FloatingActionButton(
            onClick = { startTimer(Triple(hours.value, minutes.value, seconds.value)) },
            modifier = Modifier.padding(top = 192.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.PlayArrow,
                contentDescription = "Start timer",
                Modifier
                    .size(32.dp)
            )
        }
    }
}

@Composable
fun TimeSelector(currentVal: MutableState<Int>, maxValue: Int, type: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val canIncrease = currentVal.value < maxValue
        val canDecrease = currentVal.value > 0

        Text(text = type)

        Icon(
            imageVector = Icons.Filled.ArrowDropDown,
            tint = if (canIncrease) Color.Black else Color.Gray,
            contentDescription = if (canIncrease) "Increase $type" else "Increase button disabled, $type cannot be greater than $maxValue",
            modifier = if (canIncrease) {
                Modifier
                    .rotate(180f)
                    .size(44.dp)
                    .clickable { currentVal.value++ }
            } else {
                Modifier
                    .rotate(180f)
                    .size(44.dp)
            }

        )
        Text(
            text = "${currentVal.value}",
            modifier = Modifier.padding(8.dp)
        )
        Icon(
            imageVector = Icons.Filled.ArrowDropDown,
            tint = if (canDecrease) Color.Black else Color.Gray,
            contentDescription = if (canDecrease) "Decrease $type" else "Decrease button disabled, $type cannot be less than 0",
            modifier = if (canDecrease) {
                Modifier
                    .size(44.dp)
                    .clickable { currentVal.value-- }
                } else {
                    Modifier
                        .size(44.dp)
            }
        )
    }
}

@Preview
@Composable
fun SetTimePreview() {
    TimerComposeTheme {
        TimerHomePage({})
    }
}