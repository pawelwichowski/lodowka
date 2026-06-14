package com.reypablo.lodowka.presentation.screens.recipes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reypablo.lodowka.domain.models.RecipeFilters
import com.reypablo.lodowka.domain.models.RecipeWithMissing
import com.reypablo.lodowka.domain.usecases.FilterRecipesUseCase
import com.reypablo.lodowka.domain.usecases.GetRecipesByIngredientsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipesViewModel @Inject constructor(
    private val getRecipesByIngredientsUseCase: GetRecipesByIngredientsUseCase,
    private val filterRecipesUseCase: FilterRecipesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(RecipesState())
    val state: StateFlow<RecipesState> = _state.asStateFlow()

    init {
        loadRecipes()
    }

    fun onEvent(event: RecipesEvent) {
        when (event) {
            RecipesEvent.Refresh -> loadRecipes()
            is RecipesEvent.SelectTab -> {
                _state.update { it.copy(selectedTab = event.tab) }
                loadRecipes()
            }
            is RecipesEvent.FilterChanged -> {
                _state.update { it.copy(filters = event.filters) }
                filterRecipes()
            }
        }
    }

    private fun loadRecipes() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            getRecipesByIngredientsUseCase(
                maxMissing = if (_state.value.selectedTab == 1) 2 else Int.MAX_VALUE
            ).collect { result ->
                when {
                    result.isSuccess -> {
                        val recipes = result.getOrNull() ?: emptyList()
                        _state.update {
                            it.copy(
                                recipes = recipes,
                                isLoading = false
                            )
                        }
                        filterRecipes()
                    }
                    result.isFailure -> {
                        _state.update {
                            it.copy(
                                error = result.exceptionOrNull()?.message ?: "Error loading recipes",
                                isLoading = false
                            )
                        }
                    }
                }
            }
        }
    }

    private fun filterRecipes() {
        val recipes = _state.value.recipes.map { it.recipe }
        val filtered = filterRecipesUseCase(recipes, _state.value.filters)

        val filteredWithMissing = _state.value.recipes.filter { recipeWithMissing ->
            filtered.contains(recipeWithMissing.recipe)
        }

        _state.update { it.copy(recipes = filteredWithMissing) }
    }
}
