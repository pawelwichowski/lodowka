package com.reypablo.lodowka.domain.usecases

import com.reypablo.lodowka.domain.models.Recipe
import com.reypablo.lodowka.domain.repositories.FridgeRepository
import com.reypablo.lodowka.domain.repositories.RecipeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetRecipesUseCase @Inject constructor(
    private val recipeRepository: RecipeRepository,
    private val fridgeRepository: FridgeRepository
) {
    operator fun invoke(): Flow<Result<List<Recipe>>> = flow {
        val ingredients = fridgeRepository.getAllIngredients().first().map { it.name }
        if (ingredients.isEmpty()) {
            emit(Result.success(emptyList()))
            return@flow
        }
        emit(recipeRepository.getRecipes(ingredients))
    }
}
