package com.reypablo.lodowka.presentation.screens.history

import com.reypablo.lodowka.domain.models.HistoryEntry

sealed class HistoryState {
    object Loading : HistoryState()
    data class Success(val entries: List<HistoryEntry>) : HistoryState()
    data class Error(val message: String) : HistoryState()
}