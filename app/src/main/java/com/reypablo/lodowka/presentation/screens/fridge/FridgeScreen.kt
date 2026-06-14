package com.reypablo.lodowka.presentation.screens.fridge

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Kitchen
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.reypablo.lodowka.domain.models.Ingredient
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FridgeScreen(
    state: FridgeState,
    onEvent: (FridgeEvent) -> Unit,
    onNavigateToRecipes: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Fridge") },
                actions = {
                    IconButton(onClick = { onEvent(FridgeEvent.ToggleSearch) }) {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { onEvent(FridgeEvent.ShowAddDialog) }) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        },
        snackbarHost = { SnackbarHost(snackBarHostState) }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
            if (state.isSearching) {
                OutlinedTextField(
                    value = state.searchQuery,
                    onValueChange = { onEvent(FridgeEvent.SearchQueryChanged(it)) },
                    label = { Text("Search...") },
                    modifier = Modifier.fillMaxWidth().padding(16.dp)
                )
            }

            if (state.filteredIngredients.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Kitchen,
                        contentDescription = "Empty",
                        modifier = Modifier.size(120.dp)
                    )
                    Text("Your fridge is empty")
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(state.filteredIngredients.size) { index ->
                        val item = state.filteredIngredients[index]
                        IngredientCard(
                            ingredient = item,
                            onEdit = { onEvent(FridgeEvent.EditIngredient(item)) },
                            onDelete = {
                                scope.launch {
                                    onEvent(FridgeEvent.DeleteIngredient(item))
                                }
                            }
                        )
                        if (index < state.filteredIngredients.size - 1) {
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
        }

        if (state.ingredients.isNotEmpty()) {
            Button(
                onClick = onNavigateToRecipes,
                modifier = Modifier.padding(16.dp).fillMaxWidth().height(56.dp)
            ) {
                Text("Find recipes")
            }
        }

        if (state.showAddDialog) {
            AddIngredientDialog(
                ingredient = state.editingIngredient,
                onDismiss = { onEvent(FridgeEvent.HideAddDialog) },
                onConfirm = { onEvent(FridgeEvent.SaveIngredient(it)); onEvent(FridgeEvent.HideAddDialog) }
            )
        }
    }
}

@Composable
private fun IngredientCard(
    ingredient: Ingredient,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(ingredient.name)
            }
            IconButton(onClick = onEdit) { Icon(Icons.Default.Edit, null) }
            IconButton(onClick = onDelete) { Icon(Icons.Default.Delete, null) }
        }
    }
}

@Composable
private fun AddIngredientDialog(
    ingredient: Ingredient?,
    onDismiss: () -> Unit,
    onConfirm: (Ingredient) -> Unit
) {
    var name by remember { mutableStateOf(ingredient?.name ?: "") }
    var quantity by remember { mutableStateOf(ingredient?.quantity?.toString() ?: "") }
    var unit by remember { mutableStateOf(ingredient?.unit ?: "") }

    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add ingredient") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = quantity,
                    onValueChange = { quantity = it },
                    label = { Text("Quantity") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                OutlinedTextField(
                    value = unit,
                    onValueChange = { unit = it },
                    label = { Text("Unit") }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(Ingredient(name = name, quantity = quantity.toDoubleOrNull(), unit = unit)) },
                enabled = name.isNotBlank()
            ) { Text("Save") }
        },
        dismissButton = { androidx.compose.material3.TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}
