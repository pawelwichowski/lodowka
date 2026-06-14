package com.reypablo.lodowka.presentation.screens.recipe_detail

import com.reypablo.lodowka.domain.models.Recipe

sealed class RecipeDetailState {
    object Loading : RecipeDetailState()
    data class Success(val recipe: Recipe) : RecipeDetailState()
    data class Error(val message: String) : RecipeDetailState()
}