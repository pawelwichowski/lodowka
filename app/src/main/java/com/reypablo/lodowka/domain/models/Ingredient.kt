package com.reypablo.lodowka.domain.models

data class Ingredient(
    val id: String? = null,
    val name: String,
    val quantity: Double? = null,
    val unit: String? = null,
    val category: String? = null
)

data class RecipeIngredient(
    val name: String,
    val quantity: Double,
    val unit: String
)
