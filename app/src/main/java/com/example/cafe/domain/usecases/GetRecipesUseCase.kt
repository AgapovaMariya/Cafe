package com.example.cafe.domain.usecases

import com.example.cafe.data.repository.CoffeeRecipeRepository
import com.example.cafe.domain.models.CoffeeRecipe

class GetRecipesUseCase {
    operator fun invoke(): List<CoffeeRecipe> {
        return CoffeeRecipeRepository.recipes
    }
}