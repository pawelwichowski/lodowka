package com.reypablo.lodowka.data.repositories

import android.content.Context
import com.reypablo.lodowka.data.local.dao.RecipeDao
import com.reypablo.lodowka.data.local.entities.RecipeEntity
import com.reypablo.lodowka.data.remote.api.IngredientFilterRequest
import com.reypablo.lodowka.data.remote.api.RecipeApiService
import com.reypablo.lodowka.data.remote.dto.RecipeDto
import com.reypablo.lodowka.domain.models.Recipe
import com.reypablo.lodowka.domain.models.RecipeIngredient
import com.reypablo.lodowka.domain.repositories.RecipeRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecipeRepositoryImpl @Inject constructor(
    private val apiService: RecipeApiService,
    private val recipeDao: RecipeDao,
    @ApplicationContext private val context: Context
) : RecipeRepository {

    override suspend fun getRecipes(ingredients: List<String>): Result<List<Recipe>> {
        return try {
            val request = IngredientFilterRequest(
                available = ingredients,
                limit = 50
            )
            val resp = apiService.getRecipesByAvailableIngredients(request)

            if (resp.isSuccessful) {
                val recipes = resp.body()?.map { it.toDomain() } ?: emptyList()
                cacheRecipes(recipes)
                Result.success(recipes)
            } else {
                Result.failure(Exception("API Error: ${resp.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getRecipeById(id: String): Result<Recipe> {
        return try {
            val cached = recipeDao.getById(id)
            if (cached != null) {
                return Result.success(cached.toDomain())
            }
            
            // If not in cache, fetch all and find
            val allRecipes = getRecipes(emptyList()).getOrElse { return Result.failure(it) }
            val recipe = allRecipes.find { it.id == id }
            
            if (recipe != null) {
                recipeDao.insert(recipe.toEntity())
                Result.success(recipe)
            } else {
                Result.failure(Exception("Recipe not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getCachedRecipes(): Flow<List<Recipe>> {
        return recipeDao.getAll().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun cacheRecipes(recipes: List<Recipe>) {
        recipeDao.insertAll(recipes.map { it.toEntity() })
    }

    private fun RecipeDto.toDomain(): Recipe {
        return Recipe(
            id = id,
            name = name,
            description = description,
            imageUrl = imageUrl,
            ingredients = ingredients.map { RecipeIngredient(it.name, it.quantity, it.unit) },
            instructions = instructions
        )
    }

    private fun Recipe.toEntity(): RecipeEntity {
        return RecipeEntity(
            id = id,
            name = name,
            description = description,
            imageUrl = imageUrl,
            ingredients = ingredients,
            instructions = instructions
        )
    }

    private fun RecipeEntity.toDomain(): Recipe {
        return Recipe(
            id = id,
            name = name,
            description = description,
            imageUrl = imageUrl,
            ingredients = ingredients,
            instructions = instructions
        )
    }
}