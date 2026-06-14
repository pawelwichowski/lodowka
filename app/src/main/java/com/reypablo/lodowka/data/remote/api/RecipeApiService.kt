package com.reypablo.lodowka.data.remote.api

import com.reypablo.lodowka.data.remote.dto.RecipeDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * API Service for the custom recipe API.
 * 
 * Endpoint: /recipes/by-available-ingredients
 * Returns recipes ranked by how many ingredients are NOT covered by available list.
 * Optionally filters by required ingredients.
 */
interface RecipeApiService {
    
    @POST("recipes/by-available-ingredients")
    suspend fun getRecipesByAvailableIngredients(
        @Body request: IngredientFilterRequest,
        @Query("limit") limit: Int = 50
    ): Response<List<RecipeDto>>
    
    @POST("recipes/by-ingredients")
    suspend fun getRecipesByIngredients(
        @Body request: IngredientFilterRequestV2
    ): Response<List<RecipeDto>>
}

/**
 * Request for /recipes/by-available-ingredients
 */
data class IngredientFilterRequest(
    val available: List<String>,
    val required: List<String> = emptyList(),
    val limit: Int = 10
)

/**
 * Request for /recipes/by-ingredients
 */
data class IngredientFilterRequestV2(
    val required: List<String>,
    val allowed: List<String>
)
