package com.reypablo.lodowka.presentation.screens.recipe_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reypablo.lodowka.domain.repositories.FridgeRepository
import com.reypablo.lodowka.domain.repositories.RecipeRepository
import com.reypablo.lodowka.domain.repositories.ShoppingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeDetailViewModel @Inject constructor(
    private val recipeRepository: RecipeRepository,
    private val fridgeRepository: FridgeRepository,
    private val shoppingRepository: ShoppingRepository
) : ViewModel() {

    private val _state = MutableStateFlow<RecipeDetailState>(RecipeDetailState.Loading)
    val state: StateFlow<RecipeDetailState> = _state.asStateFlow()

    fun onEvent(event: RecipeDetailEvent) {
        when (event) {
            is RecipeDetailEvent.LoadRecipe -> loadRecipe(event.recipeId)
            RecipeDetailEvent.AddToShoppingList -> addToShoppingList()
            RecipeDetailEvent.AddToFridge -> addToFridge()
        }
    }

    private fun loadRecipe(recipeId: String) {
        viewModelScope.launch {
            _state.value = RecipeDetailState.Loading
            recipeRepository.getRecipeById(recipeId)
                .onSuccess { recipe ->
                    _state.value = RecipeDetailState.Success(recipe)
                }
                .onFailure { error ->
                    _state.value = RecipeDetailState.Error(error.message ?: "Unknown error")
                }
        }
    }

    private fun addToShoppingList() {
        viewModelScope.launch {
            val currentState = _state.value
            if (currentState is RecipeDetailState.Success) {
                val ingredients = currentState.recipe.ingredients.map {
                    com.reypablo.lodowka.domain.models.Ingredient(
                        name = it.name,
                        quantity = it.quantity,
                        unit = it.unit
                    )
                }
                ingredients.forEach { ingredient ->
                    shoppingRepository.addItem(ingredient)
                }
            }
        }
    }

    private fun addToFridge() {
        viewModelScope.launch {
            val currentState = _state.value
            if (currentState is RecipeDetailState.Success) {
                val ingredients = currentState.recipe.ingredients.map {
                    com.reypablo.lodowka.domain.models.Ingredient(
                        name = it.name,
                        quantity = it.quantity,
                        unit = it.unit
                    )
                }
                ingredients.forEach { ingredient ->
                    fridgeRepository.addIngredient(ingredient)
                }
            }
        }
    }
}