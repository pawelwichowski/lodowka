package com.reypablo.lodowka.domain.models

data class Recipe(
    val id: String,
    val name: String,
    val description: String? = null,
    val imageUrl: String? = null,
    val ingredients: List<RecipeIngredient>,
    val instructions: List<String>
)
