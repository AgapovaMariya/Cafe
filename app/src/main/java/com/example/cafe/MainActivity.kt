package com.example.cafe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
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
                    StartScreen(
                        onStartClick = { /* Пока ничего */ },
                        onExitClick = { android.os.Process.killProcess(android.os.Process.myPid()) }
                    )
                }
            }
        }
    }
}