package com.example.cafe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.runtime.saveable.rememberSaveable
import com.example.cafe.presentation.screens.cafe.CafeScreen
import com.example.cafe.presentation.screens.start.StartScreen
import com.example.cafe.ui.theme.CafeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CafeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    // rememberSaveable сохраняет значение при повороте экрана
    var currentScreen by rememberSaveable { mutableStateOf("start") }

    when (currentScreen) {
        "start" -> StartScreen(
            onStartClick = { currentScreen = "cafe" },
            onExitClick = { android.os.Process.killProcess(android.os.Process.myPid()) }
        )
        "cafe" -> CafeScreen(
            onBackToStart = { currentScreen = "start" }
        )
    }
}