package com.reypablo.lodowka.presentation.screens.shopping_list

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun ShoppingListScreen(
    state: ShoppingListState,
    onEvent: (ShoppingListEvent) -> Unit,
    onNavigateBack: () -> Unit
) {
    Text("Shopping List Screen")
}
