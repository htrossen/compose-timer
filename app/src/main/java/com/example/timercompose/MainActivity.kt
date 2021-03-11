package com.example.timercompose

import android.os.Bundle
import android.os.CountDownTimer
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NamedNavArgument
import com.example.timercompose.ui.theme.TimerComposeTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigate
import androidx.navigation.compose.rememberNavController

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TimerComposeTheme {
                val navController = rememberNavController()
                NavHost(navController, startDestination = "home") {
                    composable("home") {
                        TimerHomePage(
                            startTimer = { time ->
                                navController.navigate(route = "countdown?hours=${time.first}&minutes=${time.second}&seconds=${time.third}")
                            }
                        )
                    }
                    composable("countdown?hours={hours}&minutes={minutes}&seconds={seconds}") {
                        CountDownTimerPage(
                            it.arguments?.getString("hours")?.toInt() ?: 0,
                            it.arguments?.getString("minutes")?.toInt() ?: 0,
                            it.arguments?.getString("seconds")?.toInt() ?: 0,
                            reset = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TimerComposeTheme {
        TimerHomePage({})
    }
}