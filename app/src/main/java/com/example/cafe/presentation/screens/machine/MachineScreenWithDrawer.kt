package com.example.cafe.presentation.screens.machine

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MachineScreenWithDrawer(
    targetBase: String = "latte",
    targetBlood: String = "A+",
    onBackToCafe: () -> Unit = {},
    onComplete: (isCorrect: Boolean) -> Unit = {}
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var isFirstRender by remember { mutableStateOf(true) }

    // Только при первом рендере закрываем без анимации
    LaunchedEffect(Unit) {
        if (isFirstRender) {
            drawerState.close()
            isFirstRender = false
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            RecipeDrawerContent(
                onCloseDrawer = {
                    scope.launch { drawerState.close() }
                }
            )
        }
    ) {
        MachineScreen(
            targetBase = targetBase,
            targetBlood = targetBlood,
            onBackToCafe = onBackToCafe,
            onComplete = onComplete,
            onOpenDrawer = {
                scope.launch { drawerState.open() }
            }
        )
    }
}