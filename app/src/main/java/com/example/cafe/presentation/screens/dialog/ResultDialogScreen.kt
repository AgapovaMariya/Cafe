package com.example.cafe.presentation.screens.dialog

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
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
fun ResultDialogScreen(
    isSuccess: Boolean,
    orderedDrinkName: String = "Закатный эликсир",
    onBackToCafe: () -> Unit = {},
    onRestart: () -> Unit = {},
    isFlowResult: Boolean = false,
    isFinal: Boolean = false
) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val context = LocalContext.current

    val backgroundRes = if (isFlowResult) {
        if (isLandscape) R.drawable.flow_l else R.drawable.flow_p
    } else {
        if (isLandscape) R.drawable.lady_l else R.drawable.lady_p
    }

    var dialogLines by remember { mutableStateOf<List<String>>(emptyList()) }
    var currentIndex by rememberSaveable { mutableStateOf(0) }
    var isLoaded by remember { mutableStateOf(false) }

    val dialogId = when {
        isFinal && isSuccess -> "result_success_end"
        isFinal && !isSuccess -> "result_fail_end"
        isFlowResult && isSuccess -> "flow_result_success"
        isFlowResult && !isSuccess -> "flow_result_fail"
        !isFlowResult && isSuccess -> "result_success"
        else -> "result_fail"
    }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            val repository = DialogRepository(context)
            val lines = repository.loadDialogFromAssets(dialogId)
            val processedLines = lines.map { line ->
                line.replace("{drinkName}", orderedDrinkName)
            }
            withContext(Dispatchers.Main) {
                dialogLines = processedLines
                isLoaded = true
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
            if (isFinal) {
                onRestart()
            } else {
                if (isSuccess) onBackToCafe() else onRestart()
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