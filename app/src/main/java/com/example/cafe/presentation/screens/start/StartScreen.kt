package com.example.cafe.presentation.screens.start

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
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

        // Кнопка "Начать" - оригинальный размер
        val startButtonPainter = painterResource(id = R.drawable.nach_button)
        val startButtonIntrinsicSize = startButtonPainter.intrinsicSize

        Box(
            modifier = Modifier
                .offset(
                    x = if (isLandscape) landscapeStartX else portraitStartX,
                    y = if (isLandscape) landscapeStartY else portraitStartY
                )
                .size(
                    width = startButtonIntrinsicSize.width.dp,
                    height = startButtonIntrinsicSize.height.dp
                )
                .pointerInput(Unit) {
                    awaitPointerEventScope {
                        while (true) {
                            val event = awaitPointerEvent()
                            if (event.type == PointerEventType.Press) {
                                onStartClick()
                            }
                        }
                    }
                }
        ) {
            Image(
                painter = startButtonPainter,
                contentDescription = "Start button",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillBounds  // Растягиваем ровно под размер Box
            )
        }

        // Кнопка "Выход" - оригинальный размер
        val exitButtonPainter = painterResource(id = R.drawable.out_button)
        val exitButtonIntrinsicSize = exitButtonPainter.intrinsicSize

        Box(
            modifier = Modifier
                .offset(
                    x = if (isLandscape) landscapeExitX else portraitExitX,
                    y = if (isLandscape) landscapeExitY else portraitExitY
                )
                .size(
                    width = exitButtonIntrinsicSize.width.dp,
                    height = exitButtonIntrinsicSize.height.dp
                )
                .pointerInput(Unit) {
                    awaitPointerEventScope {
                        while (true) {
                            val event = awaitPointerEvent()
                            if (event.type == PointerEventType.Press) {
                                onExitClick()
                            }
                        }
                    }
                }
        ) {
            Image(
                painter = exitButtonPainter,
                contentDescription = "Exit button",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillBounds
            )
        }
    }
}