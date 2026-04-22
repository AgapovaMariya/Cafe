package com.example.cafe.presentation.screens.cafe

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Text
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import com.example.cafe.R
import com.example.cafe.repository.WeatherRepository
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope

@Composable
fun CafeScreen(
    onBackToStart: () -> Unit = {}  // временно, потом заменим на переход к диалогу
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
    var temperature by remember { mutableStateOf<Double?>(null) }

    // Загружаем погоду при первом появлении экрана
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            val weather = weatherRepository.getWeather()
            if (weather != null) {
                temperature = weather.currentWeather.temperature
                weatherText = "🌡️ ${weather.currentWeather.temperature}°C  |  💨 ${weather.currentWeather.windSpeed} км/ч"
            } else {
                weatherText = "❌ Не удалось загрузить погоду"
            }
        }
    }

    // Координаты для виджета погоды
    val portraitWeatherX = 132.dp
    val portraitWeatherY = 240.dp
    val landscapeWeatherX = 385.dp
    val landscapeWeatherY = 90.dp

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

        // Виджет погоды
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

        // Позже здесь добавим персонажа со знаком над головой
        // И кнопку для перехода к диалогу
    }
}