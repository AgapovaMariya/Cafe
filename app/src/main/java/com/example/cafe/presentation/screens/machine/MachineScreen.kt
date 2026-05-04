package com.example.cafe.presentation.screens.machine

import android.content.res.Configuration
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cafe.R
import com.example.cafe.data.repository.CoffeeRecipeRepository

@Composable
fun MachineScreen(
    targetBase: String = "latte",
    targetBlood: String = "A+",
    onBackToCafe: () -> Unit = {},
    onComplete: (isCorrect: Boolean) -> Unit = {},
    onOpenDrawer: () -> Unit = {}
) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    val backgroundRes = if (isLandscape) R.drawable.machine_l else R.drawable.machine_p

    var step by rememberSaveable { mutableStateOf(0) }
    var selectedBase by rememberSaveable { mutableStateOf<String?>(null) }
    var selectedBlood by rememberSaveable { mutableStateOf<String?>(null) }

    val baseIngredients = listOf(
        Triple("latte", "Латте", R.drawable.l_pack),
        Triple("cappuccino", "Капучино", R.drawable.k_pack)
    )

    val bloodIngredients = listOf(
        Triple("A+", "Кровь A+", R.drawable.a_jar),
        Triple("A-", "Кровь A-", R.drawable.a2_jar),
        Triple("B+", "Кровь B+", R.drawable.b_jar)
    )

    val portraitRowY = 500.dp
    val landscapeRowY = 250.dp

    // Нормализуем целевые значения для сравнения
    val normalizedTargetBlood = CoffeeRecipeRepository.normalizeBlood(targetBlood)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Image(
            painter = painterResource(id = backgroundRes),
            contentDescription = "Machine background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Fit
        )

        IconButton(
            onClick = onOpenDrawer,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = "Меню рецептов",
                tint = Color.White
            )
        }

        Text(
            text = if (step == 0) "Выбери основу:" else "Выбери кровь:",
            color = Color.White,
            fontSize = if (isLandscape) 16.sp else 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = if (isLandscape) 20.dp else 50.dp)
                .background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(8.dp))
                .padding(horizontal = 16.dp, vertical = 8.dp)
        )

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .offset(
                    x = if (isLandscape) 130.dp else 20.dp,
                    y = if (isLandscape) landscapeRowY + 10.dp else portraitRowY
                )
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            val ingredients = if (step == 0) baseIngredients else bloodIngredients
            items(ingredients) { (id, name, imageRes) ->
                IngredientItem(
                    imageRes = imageRes,
                    name = name,
                    onClick = {
                        if (step == 0) {
                            selectedBase = id
                            step = 1
                        } else {
                            selectedBlood = id
                            // Нормализуем выбранную кровь и сравниваем с нормализованной целевой
                            val normalizedSelected = CoffeeRecipeRepository.normalizeBlood(id)
                            val isCorrect = selectedBase == targetBase && normalizedSelected == normalizedTargetBlood
                            onComplete(isCorrect)
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun IngredientItem(
    imageRes: Int,
    name: String,
    onClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.92f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "scale"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(100.dp)
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
            painter = painterResource(id = imageRes),
            contentDescription = name,
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White.copy(alpha = 0.2f))
                .padding(8.dp),
            contentScale = ContentScale.Fit
        )
        Text(
            text = name,
            color = Color.White,
            fontSize = 12.sp,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}