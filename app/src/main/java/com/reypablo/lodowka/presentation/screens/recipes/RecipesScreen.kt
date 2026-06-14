package com.reypablo.lodowka.presentation.screens.recipes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipesScreen(
    state: RecipesState,
    onEvent: (RecipesEvent) -> Unit,
    onRecipeClick: (String) -> Unit,
    onNavigateToShoppingList: () -> Unit,
    onNavigateToHistory: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Recipes") },
                actions = {
                    IconButton(onClick = onNavigateToShoppingList) {
                        Icon(Icons.Default.ShoppingCart, contentDescription = "Shopping list")
                    }
                    IconButton(onClick = onNavigateToHistory) {
                        Icon(Icons.Default.History, contentDescription = "History")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            TabRow(selectedTabIndex = state.selectedTab) {
                Tab(
                    selected = state.selectedTab == 0,
                    onClick = { onEvent(RecipesEvent.SelectTab(0)) },
                    text = { Text("All") }
                )
                Tab(
                    selected = state.selectedTab == 1,
                    onClick = { onEvent(RecipesEvent.SelectTab(1)) },
                    text = { Text("With missing") }
                )
            }

            if (state.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (state.error != null) {
                ErrorContent(error = state.error, onRetry = { onEvent(RecipesEvent.Refresh) })
            } else {
                if (state.recipes.isEmpty()) {
                    EmptyRecipesContent()
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        items(state.recipes.size) { index ->
                            val recipeWithMissing = state.recipes[index]
                            RecipeCard(
                                recipe = recipeWithMissing.recipe,
                                missingCount = recipeWithMissing.missingIngredients.size,
                                onClick = { onRecipeClick(recipeWithMissing.recipe.id) }
                            )
                            if (index < state.recipes.size - 1) {
                                Spacer(modifier = Modifier.height(16.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RecipeCard(
    recipe: com.reypablo.lodowka.domain.models.Recipe,
    missingCount: Int,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = recipe.name, style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))
            if (missingCount > 0) {
                Text(
                    text = "Missing $missingCount ingredients",
                    color = MaterialTheme.colorScheme.error
                )
            } else {
                Text(
                    text = "All ingredients available",
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
private fun ErrorContent(error: String, onRetry: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                Icons.Default.Error,
                contentDescription = "Error",
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.error
            )
            Text(text = error)
            Button(onClick = onRetry) { Text("Try again") }
        }
    }
}

@Composable
private fun EmptyRecipesContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                Icons.Default.SearchOff,
                contentDescription = "No results",
                modifier = Modifier.size(120.dp),
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
            )
            Text("No recipes found")
        }
    }
}

@Composable
private fun IconButton(onClick: () -> Unit, content: @Composable () -> Unit) {
    androidx.compose.material3.IconButton(onClick = onClick) { content() }
}
