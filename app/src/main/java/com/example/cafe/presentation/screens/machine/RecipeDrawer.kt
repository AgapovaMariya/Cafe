package com.example.cafe.presentation.screens.machine

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cafe.data.repository.CoffeeRecipeRepository

@Composable
fun RecipeDrawerContent(
    onCloseDrawer: () -> Unit
) {
    ModalDrawerSheet(
        modifier = Modifier
            .width(280.dp)
            .background(Color(0xFF1a1a2e))
    ) {
        Text(
            text = "📜 Рецепты кофе",
            fontSize = 24.sp,
            color = Color.Black,
            modifier = Modifier.padding(16.dp)
        )

        Divider(color = Color.Gray)

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(CoffeeRecipeRepository.recipes) { recipe ->
                RecipeCard(recipe = recipe)
            }
        }
    }
}

@Composable
fun RecipeCard(recipe: com.example.cafe.domain.models.CoffeeRecipe) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF16213e)
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = "☕ ${recipe.name}",
                fontSize = 16.sp,
                color = Color(0xFFe94560)
            )
            Text(
                text = "Основа: ${if (recipe.base == "latte") "Латте" else "Капучино"}",
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.7f)
            )
            Text(
                text = "Кровь: ${recipe.blood}",
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.7f)
            )
        }
    }
}