package com.reypablo.lodowka.domain.repositories

import com.reypablo.lodowka.domain.models.Recipe
import kotlinx.coroutines.flow.Flow

interface RecipeRepository {
    suspend fun getRecipes(ingredients: List<String>): Result<List<Recipe>>
    suspend fun getRecipeById(id: String): Result<Recipe>
    suspend fun getCachedRecipes(): Flow<List<Recipe>>
    suspend fun cacheRecipes(recipes: List<Recipe>)
}
