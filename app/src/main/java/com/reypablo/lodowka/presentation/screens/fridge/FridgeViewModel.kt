package com.reypablo.lodowka.presentation.screens.fridge

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reypablo.lodowka.domain.models.Ingredient
import com.reypablo.lodowka.domain.repositories.FridgeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FridgeViewModel @Inject constructor(
    private val fridgeRepository: FridgeRepository
) : ViewModel() {

    private val _state = MutableStateFlow(FridgeState())
    val state: StateFlow<FridgeState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            fridgeRepository.getAllIngredients().collect { ingredients ->
                _state.update { it.copy(ingredients = ingredients) }
            }
        }
    }

    fun onEvent(event: FridgeEvent) {
        when (event) {
            is FridgeEvent.SearchQueryChanged -> {
                _state.update { it.copy(searchQuery = event.query) }
                filterIngredients()
            }
            FridgeEvent.Search -> filterIngredients()
            FridgeEvent.ToggleSearch -> {
                _state.update { it.copy(isSearching = !it.isSearching) }
            }
            FridgeEvent.ShowAddDialog -> {
                _state.update { it.copy(showAddDialog = true) }
            }
            FridgeEvent.HideAddDialog -> {
                _state.update { it.copy(showAddDialog = false, editingIngredient = null) }
            }
            is FridgeEvent.EditIngredient -> {
                _state.update { it.copy(showAddDialog = true, editingIngredient = event.ingredient) }
            }
            is FridgeEvent.SaveIngredient -> {
                viewModelScope.launch {
                    fridgeRepository.addIngredient(event.ingredient)
                    _state.update { it.copy(showAddDialog = false, editingIngredient = null) }
                }
            }
            is FridgeEvent.DeleteIngredient -> {
                viewModelScope.launch {
                    fridgeRepository.deleteIngredient(event.ingredient)
                }
            }
        }
    }

    private fun filterIngredients() {
        val query = _state.value.searchQuery.lowercase()
        if (query.isBlank()) {
            _state.update { it.copy(filteredIngredients = it.ingredients) }
        } else {
            _state.update {
                it.copy(filteredIngredients = it.ingredients.filter { ingredient ->
                    ingredient.name.lowercase().contains(query)
                })
            }
        }
    }
}
