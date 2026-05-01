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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cafe.R
import com.example.cafe.data.repository.DialogRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun ResultDialogScreen(
    isSuccess: Boolean,  // true - приняли, false - завалил
    orderedDrinkName: String = "Закатный эликсир",
    onBackToCafe: () -> Unit = {},
    onRestart: () -> Unit = {}  // начать заново
) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    // Используем те же фоны lady_l и lady_p
    val backgroundRes = if (isLandscape) R.drawable.lady_l else R.drawable.lady_p

    // Текст результата
    val resultText = if (isSuccess) {
        listOf(
            "Ммм... ${orderedDrinkName}!",
            "Вкус потрясающий!",
            "Ты отлично справился, стажёр!",
            "Я готова доверить тебе",
            "нашу дорогую клиентку.",
            "Ну что, готов?"
        )
    } else {
        listOf(
            "Фу... Что это за ужас?!",
            "Это точно не ${orderedDrinkName}.",
            "Ты завалил стажировку!",
            "Придётся искать другого.",
            "Иди тренируйся ещё..."
        )
    }

    var currentIndex by remember { mutableStateOf(0) }
    var isLoaded by remember { mutableStateOf(false) }

    // Сброс индекса при первом открытии (не сохраняем прогресс)
    LaunchedEffect(Unit) {
        currentIndex = 0
        isLoaded = true
    }

    val portraitTextX = 50.dp
    val portraitTextY = 620.dp
    val portraitTextWidth = 330.dp
    val landscapeTextX = 150.dp
    val landscapeTextY = 275.dp
    val landscapeTextWidth = 330.dp

    val onScreenTap = {
        if (currentIndex < resultText.size - 1) {
            currentIndex++
        } else {
            if (isSuccess) {
                onBackToCafe()  // успех - идём к клиентке (пока назад в кафе)
            } else {
                onRestart()     // провал - перезапуск игры
            }
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
            contentDescription = "Result dialog background",
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
                    text = resultText[currentIndex],
                    color = Color.Black,
                    fontSize = if (isLandscape) 24.sp else 24.sp,
                    fontWeight = FontWeight.Normal,
                    lineHeight = 20.sp
                )
            }
        }
    }
}