package com.example.cafe.domain.models

data class CoffeeRecipe(
    val id: Int,
    val name: String,           // "Красный бархат"
    val base: String,           // "latte" или "cappuccino"
    val blood: String,          // "A+", "A-", "B+"
    val description: String
)

data class Ingredient(
    val id: String,
    val name: String,
    val imageRes: Int,
    val type: IngredientType
)

enum class IngredientType {
    BASE, BLOOD
}