package com.reypablo.lodowka.presentation.screens.fridge

import com.reypablo.lodowka.domain.models.Ingredient

sealed class FridgeEvent {
    data class SearchQueryChanged(val query: String) : FridgeEvent()
    object Search : FridgeEvent()
    object ToggleSearch : FridgeEvent()
    object ShowAddDialog : FridgeEvent()
    object HideAddDialog : FridgeEvent()
    data class EditIngredient(val ingredient: Ingredient) : FridgeEvent()
    data class SaveIngredient(val ingredient: Ingredient) : FridgeEvent()
    data class DeleteIngredient(val ingredient: Ingredient) : FridgeEvent()
}
