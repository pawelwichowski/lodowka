package com.reypablo.lodowka.presentation.screens.recipe_detail

sealed class RecipeDetailEvent {
    data class LoadRecipe(val recipeId: String) : RecipeDetailEvent()
    object AddToShoppingList : RecipeDetailEvent()
    object AddToFridge : RecipeDetailEvent()
}