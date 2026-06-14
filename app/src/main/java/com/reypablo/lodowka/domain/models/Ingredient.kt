package com.reypablo.lodowka.domain.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

data class Ingredient(
    val id: String? = null,
    val name: String,
    val quantity: Double? = null,
    val unit: String? = null,
    val category: String? = null
)

@JsonClass(generateAdapter = true)
data class RecipeIngredient(
    @Json(name = "name") val name: String,
    @Json(name = "quantity") val quantity: Double,
    @Json(name = "unit") val unit: String
)