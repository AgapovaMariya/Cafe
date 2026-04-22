package com.example.cafe.presentation.screens.start

import android.content.res.Configuration
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.cafe.R

@Composable
fun StartScreen(
    onStartClick: () -> Unit = {},
    onExitClick: () -> Unit = { android.os.Process.killProcess(android.os.Process.myPid()) }
) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    val backgroundRes = if (isLandscape) {
        R.drawable.start_l
    } else {
        R.drawable.start_p
    }

    // Координаты для портретной ориентации (360x640)
    val portraitStartX = 47.dp
    val portraitStartY = 504.dp
    val portraitExitX = 63.dp
    val portraitExitY = 602.dp

    // Координаты для ландшафтной ориентации (640x360)
    val landscapeStartX = 308.dp
    val landscapeStartY = 191.dp
    val landscapeExitX = 324.dp
    val landscapeExitY = 285.dp

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Image(
            painter = painterResource(id = backgroundRes),
            contentDescription = "Start screen background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Fit
        )

        // Кнопка "Начать" с анимацией
        val startButtonPainter = painterResource(id = R.drawable.nach_button)
        val startButtonIntrinsicSize = startButtonPainter.intrinsicSize

        AnimatedButton(
            onClick = onStartClick,
            xOffset = if (isLandscape) landscapeStartX else portraitStartX,
            yOffset = if (isLandscape) landscapeStartY else portraitStartY,
            width = startButtonIntrinsicSize.width.dp,
            height = startButtonIntrinsicSize.height.dp,
            painter = startButtonPainter,
            contentDescription = "Start button"
        )

        // Кнопка "Выход" с анимацией
        val exitButtonPainter = painterResource(id = R.drawable.out_button)
        val exitButtonIntrinsicSize = exitButtonPainter.intrinsicSize

        AnimatedButton(
            onClick = onExitClick,
            xOffset = if (isLandscape) landscapeExitX else portraitExitX,
            yOffset = if (isLandscape) landscapeExitY else portraitExitY,
            width = exitButtonIntrinsicSize.width.dp,
            height = exitButtonIntrinsicSize.height.dp,
            painter = exitButtonPainter,
            contentDescription = "Exit button"
        )
    }
}

@Composable
fun AnimatedButton(
    onClick: () -> Unit,
    xOffset: androidx.compose.ui.unit.Dp,
    yOffset: androidx.compose.ui.unit.Dp,
    width: androidx.compose.ui.unit.Dp,
    height: androidx.compose.ui.unit.Dp,
    painter: androidx.compose.ui.graphics.painter.Painter,
    contentDescription: String?
) {
    var isPressed by remember { mutableStateOf(false) }

    // Анимация масштаба при нажатии
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.92f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "button_scale"
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
                        tryAwaitRelease()  // Ждём, пока палец оторвётся
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