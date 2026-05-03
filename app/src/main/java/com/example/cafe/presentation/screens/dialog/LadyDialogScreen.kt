package com.example.cafe.presentation.screens.dialog

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
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
import com.example.cafe.domain.usecases.LoadDialogUseCase
import com.example.cafe.domain.usecases.SaveDialogProgressUseCase

@Composable
fun LadyDialogScreen(
    onBackToCafe: () -> Unit = {},
    onNextScreen: () -> Unit = {},
    resetProgress: Boolean = false
) {
    val context = LocalContext.current

    val viewModel: DialogViewModel = remember {
        val repository = DialogRepository(context)
        val loadUseCase = LoadDialogUseCase(repository)
        val saveUseCase = SaveDialogProgressUseCase(repository)
        DialogViewModel(loadUseCase, saveUseCase)
    }

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    val backgroundRes = if (isLandscape) R.drawable.lady_l else R.drawable.lady_p
    val dialogLines by viewModel.dialogLines.observeAsState(emptyList())
    val currentIndex by viewModel.currentIndex.observeAsState(0)
    val isLoading by viewModel.isLoading.observeAsState(true)
    val isComplete by viewModel.isComplete.observeAsState(false)

    LaunchedEffect(Unit) {
        if (resetProgress) {
            viewModel.resetProgress("lady_dialog")  // сбрасываем прогресс в Room
        }
        viewModel.loadDialog("lady_dialog", resetProgress)  // загружаем диалог
    }

    LaunchedEffect(isComplete) {
        if (isComplete) {
            onNextScreen()
        }
    }

    val portraitTextX = 50.dp
    val portraitTextY = 620.dp
    val portraitTextWidth = 330.dp
    val landscapeTextX = 150.dp
    val landscapeTextY = 275.dp
    val landscapeTextWidth = 330.dp

    val onScreenTap = {
        if (!isLoading) {
            viewModel.nextLine()
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

        if (!isLoading && dialogLines.isNotEmpty()) {
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