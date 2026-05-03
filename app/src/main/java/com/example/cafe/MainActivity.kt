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
import com.example.cafe.presentation.screens.dialog.FlowDialogScreen
import com.example.cafe.presentation.screens.dialog.LadyDialogScreen
import com.example.cafe.presentation.screens.dialog.ResultDialogScreen
import com.example.cafe.presentation.screens.machine.MachineScreenWithDrawer
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
    var currentScreen by rememberSaveable { mutableStateOf("start") }

    var targetBase by rememberSaveable { mutableStateOf("cappuccino") }
    var targetBlood by rememberSaveable { mutableStateOf("B+") }
    var targetDrinkName by rememberSaveable { mutableStateOf("Закатный эликсир") }
    var gameResult by rememberSaveable { mutableStateOf(false) }
    var shouldResetDialog by rememberSaveable { mutableStateOf(false) }
    var isLadyVisible by rememberSaveable { mutableStateOf(true) }  // ← кто в кафе

    when (currentScreen) {
        "start" -> StartScreen(
            onStartClick = {
                shouldResetDialog = true
                isLadyVisible = true  // ← начинаем с леди
                currentScreen = "cafe"
            },
            onExitClick = { android.os.Process.killProcess(android.os.Process.myPid()) }
        )

        "cafe" -> CafeScreen(
            onBackToStart = { currentScreen = "start" },
            onNavigateToLadyDialog = { currentScreen = "lady_dialog" },
            onNavigateToFlowDialog = { currentScreen = "flow_dialog" },
            isLadyVisible = isLadyVisible
        )

        "lady_dialog" -> LadyDialogScreen(
            onBackToCafe = { currentScreen = "cafe" },
            onNextScreen = {
                targetBase = "cappuccino"
                targetBlood = "B+"
                targetDrinkName = "Закатный эликсир"
                currentScreen = "machine"
            },
            resetProgress = shouldResetDialog.also { shouldResetDialog = false }
        )

        "flow_dialog" -> FlowDialogScreen(
            onBackToCafe = { currentScreen = "cafe" },
            onNextScreen = { base, blood, drinkName ->
                targetBase = base
                targetBlood = blood
                targetDrinkName = drinkName
                currentScreen = "machine_flow"
            }
        )

        "machine" -> MachineScreenWithDrawer(
            targetBase = targetBase,
            targetBlood = targetBlood,
            onBackToCafe = { currentScreen = "cafe" },
            onComplete = { isCorrect ->
                gameResult = isCorrect
                currentScreen = "result"
            }
        )

        "machine_flow" -> MachineScreenWithDrawer(
            targetBase = targetBase,
            targetBlood = targetBlood,
            onBackToCafe = { currentScreen = "cafe" },
            onComplete = { isCorrect ->
                gameResult = isCorrect
                currentScreen = "result_flow"
            }
        )

        "result" -> ResultDialogScreen(
            isSuccess = gameResult,
            onBackToCafe = {
                if (gameResult) {
                    isLadyVisible = false  // ← убираем леди, показываем флоу
                }
                currentScreen = "cafe"
            },
            onRestart = {
                shouldResetDialog = true
                isLadyVisible = true
                currentScreen = "start"
            }
        )

        "result_flow" -> ResultDialogScreen(
            isSuccess = gameResult,
            onBackToCafe = { currentScreen = "cafe" },
            onRestart = {
                shouldResetDialog = true
                isLadyVisible = true
                currentScreen = "start"
            },
            isFlowResult = true  // ← используем flow_result_success/fail
        )
    }
}
