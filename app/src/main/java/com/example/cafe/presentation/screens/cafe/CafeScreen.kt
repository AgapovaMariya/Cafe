package com.example.cafe.presentation.screens.cafe

import android.content.res.Configuration
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
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
import com.example.cafe.repository.WeatherRepository
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope

@Composable
fun CafeScreen(
    onBackToStart: () -> Unit = {},
    onNavigateToLadyDialog: () -> Unit = {}  // переход к диалогу леди
) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    val backgroundRes = if (isLandscape) {
        R.drawable.cafe_l
    } else {
        R.drawable.cafe_p
    }

    // Погода
    val weatherRepository = remember { WeatherRepository() }
    val coroutineScope = rememberCoroutineScope()
    var weatherText by remember { mutableStateOf("...") }

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            val weather = weatherRepository.getWeather()
            if (weather != null) {
                weatherText = "🌡️ ${weather.currentWeather.temperature}°C  |  💨 ${weather.currentWeather.windSpeed} км/ч"
            } else {
                weatherText = "За окном непонятно..."
            }
        }
    }

    // Координаты для виджета погоды
    val portraitWeatherX = 132.dp
    val portraitWeatherY = 240.dp
    val landscapeWeatherX = 385.dp
    val landscapeWeatherY = 90.dp

    // Координаты для персонажа lady_small
    val portraitLadyX = 140.dp
    val portraitLadyY = 500.dp
    val landscapeLadyX = 375.dp
    val landscapeLadyY = 235.dp

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Фон кафе
        Image(
            painter = painterResource(id = backgroundRes),
            contentDescription = "Cafe background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Fit
        )

        // Виджет погоды (только для экрана кафе)
        Box(
            modifier = Modifier
                .offset(
                    x = if (isLandscape) landscapeWeatherX else portraitWeatherX,
                    y = if (isLandscape) landscapeWeatherY else portraitWeatherY
                )
                .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Text(
                text = weatherText,
                color = Color.White,
                fontSize = if (isLandscape) 12.sp else 14.sp,
                fontWeight = FontWeight.Medium
            )
        }

        // Персонаж lady_small с анимацией при нажатии
        val ladyPainter = painterResource(id = R.drawable.lady_small)
        val ladyIntrinsicSize = ladyPainter.intrinsicSize

        AnimatedCharacter(
            onClick = onNavigateToLadyDialog,
            xOffset = if (isLandscape) landscapeLadyX else portraitLadyX,
            yOffset = if (isLandscape) landscapeLadyY else portraitLadyY,
            width = ladyIntrinsicSize.width.dp,
            height = ladyIntrinsicSize.height.dp,
            painter = ladyPainter,
            contentDescription = "Lady character"
        )
    }
}

@Composable
fun AnimatedCharacter(
    onClick: () -> Unit,
    xOffset: androidx.compose.ui.unit.Dp,
    yOffset: androidx.compose.ui.unit.Dp,
    width: androidx.compose.ui.unit.Dp,
    height: androidx.compose.ui.unit.Dp,
    painter: androidx.compose.ui.graphics.painter.Painter,
    contentDescription: String?
) {
    var isPressed by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.92f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "character_scale"
    )

    Box(
        modifier = Modifier
            .offset(x = xOffset, y = yOffset)
            .size(width = width, height = height)
            .scale(scale)
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isPressed = true
                        tryAwaitRelease()
                        isPressed = false
                    },
                    onTap = { onClick() }
                )
            }
    ) {
        Image(
            painter = painter,
            contentDescription = contentDescription,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )
    }
}