package com.reypablo.lodowka.presentation.screens.fridge

import com.reypablo.lodowka.domain.models.Ingredient

data class FridgeState(
    val ingredients: List<Ingredient> = emptyList(),
    val filteredIngredients: List<Ingredient> = emptyList(),
    val searchQuery: String = "",
    val isSearching: Boolean = false,
    val showAddDialog: Boolean = false,
    val editingIngredient: Ingredient? = null
)
