package com.reypablo.lodowka.presentation.screens.shopping_list

import com.reypablo.lodowka.domain.models.ShoppingItem

sealed class ShoppingListEvent {
    object LoadShoppingList : ShoppingListEvent()
    data class AddItem(val item: ShoppingItem) : ShoppingListEvent()
    data class DeleteItem(val item: ShoppingItem) : ShoppingListEvent()
    data class ToggleChecked(val item: ShoppingItem) : ShoppingListEvent()
    object ClearAll : ShoppingListEvent()
}