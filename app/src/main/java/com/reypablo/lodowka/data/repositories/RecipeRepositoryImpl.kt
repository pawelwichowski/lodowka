package com.reypablo.lodowka.data.repositories

import android.content.Context
import com.reypablo.lodowka.data.local.dao.RecipeDao
import com.reypablo.lodowka.data.local.entities.RecipeEntity
import com.reypablo.lodowka.data.remote.api.RecipeApiService
import com.reypablo.lodowka.data.remote.dto.RecipeDto
import com.reypablo.lodowka.data.remote.dto.RecipeRequest
import com.reypablo.lodowka.domain.models.Recipe
import com.reypablo.lodowka.domain.models.RecipeIngredient
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

    override suspend fun getRecipes(ingredients: List<String>): Result<List<Recipe>> {
        return try {
            val request = RecipeRequest(ingredients)
            val token = "Bearer YOUR_API_TOKEN"
            val response = apiService.searchRecipes(request, token)
            if (response.isSuccessful) {
                val body = response.body()
                val recipes = body?.recipes?.map { it.toDomain() } ?: emptyList()
                cacheRecipes(recipes)
                Result.success(recipes)
            } else {
                Result.failure(Exception("API Error"))
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
            val token = "Bearer YOUR_API_TOKEN"
            val response = apiService.getRecipeDetails(id, token)
            if (response.isSuccessful) {
                val body = response.body()
                val recipe = body?.toDomain() ?: throw Exception("Empty response")
                recipeDao.insert(recipe.toEntity())
                Result.success(recipe)
            } else {
                Result.failure(Exception("API Error"))
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
            ingredients = ingredients.map { ing ->
                RecipeIngredient(ing.name, ing.quantity, ing.unit)
            },
            instructions = instructions
        )
    }

    private fun Recipe.toEntity(): RecipeEntity {
        return RecipeEntity(
            id = id,
            name = name,
            description = description,
            imageUrl = imageUrl
        )
    }

    private fun RecipeEntity.toDomain(): Recipe {
        return Recipe(
            id = id,
            name = name,
            description = description,
            imageUrl = imageUrl,
            ingredients = emptyList(),
            instructions = emptyList()
        )
    }
}
