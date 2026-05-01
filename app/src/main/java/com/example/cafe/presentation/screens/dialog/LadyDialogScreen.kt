package com.example.cafe.presentation.screens.dialog

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cafe.R
import com.example.cafe.data.repository.DialogRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun LadyDialogScreen(
    onBackToCafe: () -> Unit = {},
    onNextScreen: () -> Unit = {}
) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val context = LocalContext.current

    val backgroundRes = if (isLandscape) R.drawable.lady_l else R.drawable.lady_p

    var dialogLines by remember { mutableStateOf<List<String>>(emptyList()) }
    var currentIndex by remember { mutableStateOf(0) }
    var isLoaded by remember { mutableStateOf(false) }

    // Загружаем диалог из JSON и сбрасываем прогресс
    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            val repository = DialogRepository(context)

            // 1. Загружаем диалог из JSON
            val lines = repository.loadDialogFromAssets("lady_dialog")

            // 2. Сбрасываем прогресс при запуске
            repository.resetProgress("lady_dialog")

            withContext(Dispatchers.Main) {
                dialogLines = lines
                currentIndex = 0
                isLoaded = true
            }
        }
    }

    // Сохраняем прогресс в процессе диалога
    LaunchedEffect(currentIndex) {
        if (isLoaded && dialogLines.isNotEmpty()) {
            withContext(Dispatchers.IO) {
                val repository = DialogRepository(context)
                repository.saveProgress("lady_dialog", currentIndex)
            }
        }
    }

    val portraitTextX = 50.dp
    val portraitTextY = 620.dp
    val portraitTextWidth = 330.dp
    val landscapeTextX = 150.dp
    val landscapeTextY = 275.dp
    val landscapeTextWidth = 330.dp

    val onScreenTap = {
        if (currentIndex < dialogLines.size - 1) {
            currentIndex++
        } else {
            onNextScreen()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .pointerInput(Unit) {
                detectTapGestures(onTap = { onScreenTap() })
            }
    ) {
        Image(
            painter = painterResource(id = backgroundRes),
            contentDescription = "Lady dialog background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Fit
        )

        if (isLoaded && dialogLines.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .offset(
                        x = if (isLandscape) landscapeTextX else portraitTextX,
                        y = if (isLandscape) landscapeTextY else portraitTextY
                    )
                    .width(if (isLandscape) landscapeTextWidth else portraitTextWidth)
                    .wrapContentHeight()
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = dialogLines[currentIndex],
                    color = Color.Black,
                    fontSize = if (isLandscape) 24.sp else 24.sp,
                    fontWeight = FontWeight.Normal,
                    lineHeight = 20.sp
                )
            }
        }
    }
}