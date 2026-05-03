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
    var isLadyVisible by rememberSaveable { mutableStateOf(true) }

    // Флоу: 1=первый заказ, 2=второй заказ
    var flowOrderCount by rememberSaveable { mutableStateOf(1) }

    when (currentScreen) {
        "start" -> StartScreen(
            onStartClick = {
                shouldResetDialog = true
                isLadyVisible = true
                flowOrderCount = 1
                currentScreen = "cafe"
            },
            onExitClick = { android.os.Process.killProcess(android.os.Process.myPid()) }
        )

        "cafe" -> CafeScreen(
            onBackToStart = { currentScreen = "start" },
            onNavigateToLadyDialog = { currentScreen = "lady_dialog" },
            onNavigateToFlowDialog = {
                // НЕ сбрасываем flowOrderCount!
                currentScreen = "flow_dialog"
            },
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
            orderNumber = flowOrderCount,
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
                when (flowOrderCount) {
                    1 -> {
                        if (isCorrect) {
                            gameResult = true
                            currentScreen = "flow_result_first"  // спасибо, давай ещё
                        } else {
                            gameResult = false
                            currentScreen = "flow_result_fail"   // диалог провала Флоу
                        }
                    }
                    2 -> {
                        // Второй заказ – сразу финал без промежуточных диалогов
                        gameResult = isCorrect
                        currentScreen = "final_lady_result"
                    }
                }
            }
        )

        "result" -> ResultDialogScreen(
            isSuccess = gameResult,
            orderedDrinkName = targetDrinkName,
            onBackToCafe = {
                if (gameResult) isLadyVisible = false
                currentScreen = "cafe"
            },
            onRestart = {
                shouldResetDialog = true
                isLadyVisible = true
                flowOrderCount = 1
                currentScreen = "start"
            },
            isFlowResult = false,
            isFinal = false
        )

        "flow_result_first" -> ResultDialogScreen(
            isSuccess = true,
            orderedDrinkName = targetDrinkName,
            onBackToCafe = {
                flowOrderCount = 2
                currentScreen = "cafe"  // возврат к Флоу для второго заказа
            },
            onRestart = {
                shouldResetDialog = true
                isLadyVisible = true
                flowOrderCount = 1
                currentScreen = "start"
            },
            isFlowResult = true,
            isFinal = false
        )

        "flow_result_fail" -> ResultDialogScreen(
            isSuccess = false,
            orderedDrinkName = targetDrinkName,
            onBackToCafe = {
                currentScreen = "final_lady_result"
            },
            onRestart = {
                shouldResetDialog = true
                isLadyVisible = true
                flowOrderCount = 1
                currentScreen = "start"
            },
            isFlowResult = true,
            isFinal = false
        )

        "final_lady_result" -> ResultDialogScreen(
            isSuccess = gameResult,
            orderedDrinkName = targetDrinkName,
            onBackToCafe = {
                shouldResetDialog = true
                isLadyVisible = true
                flowOrderCount = 1
                currentScreen = "start"
            },
            onRestart = {
                shouldResetDialog = true
                isLadyVisible = true
                flowOrderCount = 1
                currentScreen = "start"
            },
            isFlowResult = false,
            isFinal = true
        )
    }
}