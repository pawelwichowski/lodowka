package com.reypablo.lodowka.domain.repositories

import com.reypablo.lodowka.domain.models.Ingredient
import kotlinx.coroutines.flow.Flow

interface FridgeRepository {
    fun getAllIngredients(): Flow<List<Ingredient>>
    suspend fun addIngredient(ingredient: Ingredient)
    suspend fun updateIngredient(ingredient: Ingredient)
    suspend fun deleteIngredient(ingredient: Ingredient)
    fun searchIngredients(query: String): Flow<List<Ingredient>>
}
