package com.reypablo.lodowka.presentation.screens.shopping_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reypablo.lodowka.domain.models.ShoppingItem
import com.reypablo.lodowka.domain.repositories.ShoppingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ShoppingListViewModel @Inject constructor(
    private val shoppingRepository: ShoppingRepository
) : ViewModel() {

    private val _state = MutableStateFlow<ShoppingListState>(ShoppingListState.Loading)
    val state: StateFlow<ShoppingListState> = _state.asStateFlow()

    init {
        loadShoppingList()
    }

    fun onEvent(event: ShoppingListEvent) {
        when (event) {
            ShoppingListEvent.LoadShoppingList -> loadShoppingList()
            is ShoppingListEvent.AddItem -> addItem(event.item)
            is ShoppingListEvent.DeleteItem -> deleteItem(event.item)
            is ShoppingListEvent.ToggleChecked -> toggleChecked(event.item)
            ShoppingListEvent.ClearAll -> clearAll()
        }
    }

    private fun loadShoppingList() {
        _state.value = ShoppingListState.Loading
        shoppingRepository.getShoppingList()
            .onEach { items ->
                _state.value = ShoppingListState.Success(items)
            }
            .launchIn(viewModelScope)
    }

    private fun addItem(item: ShoppingItem) {
        viewModelScope.launch {
            shoppingRepository.addItem(item)
        }
    }

    private fun deleteItem(item: ShoppingItem) {
        viewModelScope.launch {
            shoppingRepository.deleteItem(item)
        }
    }

    private fun toggleChecked(item: ShoppingItem) {
        viewModelScope.launch {
            item.id?.let { id ->
                shoppingRepository.toggleItemChecked(id, !item.isChecked)
            }
        }
    }

    private fun clearAll() {
        viewModelScope.launch {
            // Note: Repository has clearCompletedItems(), not clearAll()
            // For now, we'll use clearCompletedItems()
            shoppingRepository.clearCompletedItems()
        }
    }
}