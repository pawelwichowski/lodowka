package com.reypablo.lodowka.presentation.screens.shopping_list

import com.reypablo.lodowka.domain.models.ShoppingItem

sealed class ShoppingListState {
    object Loading : ShoppingListState()
    data class Success(val items: List<ShoppingItem>) : ShoppingListState()
    data class Error(val message: String) : ShoppingListState()
}