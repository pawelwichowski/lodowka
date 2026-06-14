package com.reypablo.lodowka.presentation.screens.history

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun HistoryScreen(
    state: HistoryState,
    onEvent: (HistoryEvent) -> Unit,
    onRecipeClick: (String) -> Unit,
    onNavigateBack: () -> Unit
) {
    Text("History Screen")
}
