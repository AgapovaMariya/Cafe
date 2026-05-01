package com.example.cafe.presentation.screens.dialog

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Text
import androidx.compose.ui.text.font.FontWeight
import com.example.cafe.R
import com.example.cafe.data.AppDatabase
import com.example.cafe.data.DialogProgress
import kotlinx.coroutines.launch

@Composable
fun LadyDialogScreen(
    onBackToCafe: () -> Unit = {},
    onNextScreen: () -> Unit = {}
) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val backgroundRes = if (isLandscape) R.drawable.lady_l else R.drawable.lady_p

    // Текст диалога (простой список)
    val dialogLines = listOf(
        "Привет, стажёр!",
        "Я леди-вампир.",
        "Сегодня отличная погода для кровавого кофе!",
        "Сделай мне Латте с кровью A+",
        "Справишься?"
    )

    // Загружаем сохранённый индекс из Room
    var currentIndex by remember { mutableStateOf(0) }
    var isLoaded by remember { mutableStateOf(false) }

    // Загружаем из Room
    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            val db = AppDatabase.getInstance(context)
            val saved = db.dialogDao().get("lady_dialog")
            currentIndex = saved?.currentLine ?: 0
            isLoaded = true
        }
    }

// Сохраняем в Room
    LaunchedEffect(currentIndex) {
        if (isLoaded) {
            withContext(Dispatchers.IO) {
                val db = AppDatabase.getInstance(context)
                db.dialogDao().save(DialogProgress("lady_dialog", currentIndex))
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

        if (isLoaded) {
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