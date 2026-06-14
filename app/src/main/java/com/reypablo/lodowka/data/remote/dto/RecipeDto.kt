package com.reypablo.lodowka.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RecipeDto(
    @Json(name = "id") val id: String,
    @Json(name = "name") val name: String,
    @Json(name = "description") val description: String? = null,
    @Json(name = "imageUrl") val imageUrl: String? = null,
    @Json(name = "ingredients") val ingredients: List<IngredientDto>,
    @Json(name = "instructions") val instructions: List<String>
)

@JsonClass(generateAdapter = true)
data class IngredientDto(
    @Json(name = "name") val name: String,
    @Json(name = "quantity") val quantity: Double,
    @Json(name = "unit") val unit: String
)

@JsonClass(generateAdapter = true)
data class RecipeRequest(
    @Json(name = "ingredients") val ingredients: List<String>
)

@JsonClass(generateAdapter = true)
data class RecipeResponse(
    @Json(name = "recipes") val recipes: List<RecipeDto>
)
