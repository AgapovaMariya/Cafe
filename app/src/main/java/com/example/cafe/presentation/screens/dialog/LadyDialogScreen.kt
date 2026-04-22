package com.example.cafe.presentation.screens.dialog

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Text
import androidx.compose.ui.text.font.FontWeight
import com.example.cafe.R

@Composable
fun LadyDialogScreen(
    onBackToCafe: () -> Unit = {},
    onNextScreen: () -> Unit = {}
) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    val backgroundRes = if (isLandscape) {
        R.drawable.lady_l
    } else {
        R.drawable.lady_p
    }

    // Список диалогов (построчно)
    val dialogLines = remember {
        listOf(
            "Привет, стажёр!",
            "Я леди-вампир.",
            "Сегодня отличная погода для кровавого кофе!",
            "Сделай мне Латте с кровью A+",
            "Справишься?"
        )
    }

    // Текущий индекс отображаемого текста
    var currentIndex by remember { mutableStateOf(0) }

    // Координаты для текста
    val portraitTextX = 50.dp
    val portraitTextY = 620.dp
    val portraitTextWidth = 330.dp  // ширина текстового поля

    val landscapeTextX = 150.dp
    val landscapeTextY = 275.dp
    val landscapeTextWidth = 330.dp  // ширина текстового поля

    // Обработка нажатия на экран
    val onScreenTap = {
        if (currentIndex < dialogLines.size - 1) {
            currentIndex++  // следующий текст
        } else {
            onNextScreen()  // диалог закончен, переходим к MachineScreen
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { onScreenTap() }
                )
            }
    ) {
        // Фон диалога
        Image(
            painter = painterResource(id = backgroundRes),
            contentDescription = "Lady dialog background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Fit
        )

        // Текст диалога (чёрный, в указанной области)
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