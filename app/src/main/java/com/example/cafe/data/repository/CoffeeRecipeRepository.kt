package com.example.cafe.data.repository

import com.example.cafe.domain.models.CoffeeRecipe

object CoffeeRecipeRepository {
    val recipes = listOf(
        CoffeeRecipe(1, "Красный бархат", "latte", "A+", "Нежный латте с кровью A+"),
        CoffeeRecipe(2, "Лунная симфония", "latte", "A-", "Таинственный вкус с A-"),
        CoffeeRecipe(3, "Золотой рассвет", "latte", "B+", "Солнечный B+"),
        CoffeeRecipe(4, "Королевская кровь", "cappuccino", "A+", "Пышная пенка с A+"),
        CoffeeRecipe(5, "Полночный ритуал", "cappuccino", "A-", "Для полуночных вампиров"),
        CoffeeRecipe(6, "Закатный эликсир", "cappuccino", "B+", "Тёплый оттенок B+")
    )

    // Функция нормализации группы крови для сравнения
    private fun normalizeBlood(blood: String): String = when (blood) {
        "A-" -> "C"
        else -> blood
    }

    fun getRecipeByBaseAndBlood(base: String, blood: String): CoffeeRecipe? {
        val normalizedBlood = normalizeBlood(blood)
        return recipes.find {
            it.base == base && normalizeBlood(it.blood) == normalizedBlood
        }
    }

    fun getRecipeName(base: String, blood: String): String {
        return getRecipeByBaseAndBlood(base, blood)?.name ?: "Неизвестный рецепт"
    }
}