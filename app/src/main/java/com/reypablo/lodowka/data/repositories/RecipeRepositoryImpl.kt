package com.reypablo.lodowka.data.repositories

import android.content.Context
import com.reypablo.lodowka.data.local.dao.RecipeDao
import com.reypablo.lodowka.data.local.entities.RecipeEntity
import com.reypablo.lodowka.data.remote.api.IngredientFilterRequest
import com.reypablo.lodowka.data.remote.api.RecipeApiService
import com.reypablo.lodowka.data.remote.dto.RecipeDto
import com.reypablo.lodowka.domain.models.Recipe
import com.reypablo.lodowka.domain.models.RecipeIngredient
import com.reypablo.lodowka.domain.models.RecipeWithMissing
import com.reypablo.lodowka.domain.repositories.FridgeRepository
import com.reypablo.lodowka.domain.repositories.RecipeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecipeRepositoryImpl @Inject constructor(
    private val apiService: RecipeApiService,
    private val recipeDao: RecipeDao,
    private val fridgeRepository: FridgeRepository,
    @ApplicationContext private val context: Context
) : RecipeRepository {

    override suspend fun searchRecipes(
        query: String?,
        ingredients: List<String>?,
        cuisine: String?,
        maxTime: Int?,
        limit: Int
    ): Result<List<Recipe>> {
        return try {
            val availableIngredients = ingredients ?: getFridgeIngredients()
            
            if (availableIngredients.isEmpty()) {
                return Result.success(emptyList())
            }
            
            val request = IngredientFilterRequest(
                available = availableIngredients,
                limit = limit
            )
            
            val resp = apiService.getRecipesByAvailableIngredients(request, limit)
            
            if (resp.isSuccessful) {
                val recipes = resp.body()?.map { it.toDomain() } ?: emptyList()
                cacheRecipes(recipes)
                Result.success(recipes)
            } else {
                Result.failure(Exception("API Error: " + resp.code()))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getRecipesWithMissingIngredients(
        availableIngredients: List<String>,
        maxMissing: Int
    ): Result<List<RecipeWithMissing>> {
        return try {
            val request = IngredientFilterRequest(
                available = availableIngredients,
                limit = 100
            )
            
            val resp = apiService.getRecipesByAvailableIngredients(request, 100)
            
            if (resp.isSuccessful) {
                val allRecipes = resp.body()?.map { it.toDomain() } ?: emptyList()
                
                val result = allRecipes.map { recipe ->
                    val available = recipe.ingredients.filter { ing ->
                        availableIngredients.contains(ing.name, ignoreCase = true)
                    }
                    val missing = recipe.ingredients.filter { ing ->
                        !availableIngredients.contains(ing.name, ignoreCase = true)
                    }
                    
                    if (missing.size <= maxMissing) {
                        RecipeWithMissing(
                            recipe = recipe,
                            missingIngredients = missing,
                            availableIngredients = available
                        )
                    } else {
                        null
                    }
                }.filterNotNull()
                
                Result.success(result)
            } else {
                Result.failure(Exception("API Error: " + resp.code()))
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
            
            val allRecipes = searchRecipes(limit = 100).getOrElse { return Result.failure(it) }
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

    private suspend fun getFridgeIngredients(): List<String> {
        return fridgeRepository.getAllIngredients().first().map { it.name }
    }

    private fun RecipeDto.toDomain(): Recipe {
        return Recipe(
            id = id,
            name = name,
            description = description,
            imageUrl = imageUrl,
            ingredients = ingredients.map { ing ->
                RecipeIngredient(ing.name, ing.quantity, ing.unit)
            },
            instructions = instructions,
            cuisine = null,
            prepTimeMinutes = null,
            difficulty = null,
            servings = null,
            rating = null,
            source = "Custom API"
        )
    }

    private fun Recipe.toEntity(): RecipeEntity {
        return RecipeEntity(
            id = id,
            name = name,
            description = description,
            imageUrl = imageUrl,
            cuisine = cuisine,
            prepTimeMinutes = prepTimeMinutes,
            difficulty = difficulty,
            servings = servings,
            rating = rating,
            source = source
        )
    }

    private fun RecipeEntity.toDomain(): Recipe {
        return Recipe(
            id = id,
            name = name,
            description = description,
            imageUrl = imageUrl,
            ingredients = emptyList(),
            instructions = emptyList(),
            cuisine = cuisine,
            prepTimeMinutes = prepTimeMinutes,
            difficulty = difficulty,
            servings = servings,
            rating = rating,
            source = source
        )
    }
}
