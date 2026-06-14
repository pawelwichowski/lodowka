package com.reypablo.lodowka.presentation.screens.history

sealed class HistoryEvent {
    object LoadHistory : HistoryEvent()
    data class DeleteEntry(val entryId: String) : HistoryEvent()
}