package com.example.cafe.domain.usecases

import com.example.cafe.data.repository.CoffeeRecipeRepository

class CheckCoffeeUseCase {
    operator fun invoke(selectedBase: String, selectedBlood: String, targetBase: String, targetBlood: String): Boolean {
        return selectedBase == targetBase && selectedBlood == targetBlood
    }

    fun getRecipeName(base: String, blood: String): String {
        return CoffeeRecipeRepository.getRecipeName(base, blood)
    }
}