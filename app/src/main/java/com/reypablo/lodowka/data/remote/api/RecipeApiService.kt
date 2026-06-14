package com.reypablo.lodowka.data.remote.api

import com.reypablo.lodowka.data.remote.dto.RecipeRequest
import com.reypablo.lodowka.data.remote.dto.RecipeResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface RecipeApiService {
    @POST("recipes/search")
    suspend fun searchRecipes(
        @Body request: RecipeRequest,
        @retrofit2.http.Header("Authorization") token: String
    ): Response<RecipeResponse>

    @GET("recipes/{id}")
    suspend fun getRecipeDetails(
        @Path("id") id: String,
        @retrofit2.http.Header("Authorization") token: String
    ): Response<RecipeDto>
}
