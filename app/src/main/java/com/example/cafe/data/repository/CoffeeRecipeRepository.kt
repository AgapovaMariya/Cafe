package com.example.cafe.data.repository

import com.example.cafe.domain.models.CoffeeRecipe

object CoffeeRecipeRepository {
    val recipes = listOf(
        CoffeeRecipe(1, "Красный бархат", "latte", "A+", "Нежный латте с кровью A+"),
        CoffeeRecipe(2, "Лунная симфония", "latte", "A-", "Таинственный вкус с кровью A-"),
        CoffeeRecipe(3, "Золотой рассвет", "latte", "B+", "Солнечный B+"),
        CoffeeRecipe(4, "Королевская кровь", "cappuccino", "A+", "Пышная пенка с A+"),
        CoffeeRecipe(5, "Полночный ритуал", "cappuccino", "A-", "Для полуночных вампиров"),
        CoffeeRecipe(6, "Закатный эликсир", "cappuccino", "B+", "Тёплый оттенок B+")
    )

    fun getRecipeByBaseAndBlood(base: String, blood: String): CoffeeRecipe? {
        return recipes.find { it.base == base && it.blood == blood }
    }

    fun getRecipeName(base: String, blood: String): String {
        return getRecipeByBaseAndBlood(base, blood)?.name ?: "Неизвестный рецепт"
    }
}