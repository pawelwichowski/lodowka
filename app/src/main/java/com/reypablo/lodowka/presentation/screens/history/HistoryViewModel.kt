package com.reypablo.lodowka.presentation.screens.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reypablo.lodowka.domain.repositories.HistoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val historyRepository: HistoryRepository
) : ViewModel() {

    private val _state = MutableStateFlow<HistoryState>(HistoryState.Loading)
    val state: StateFlow<HistoryState> = _state.asStateFlow()

    init {
        loadHistory()
    }

    fun onEvent(event: HistoryEvent) {
        when (event) {
            HistoryEvent.LoadHistory -> loadHistory()
            is HistoryEvent.DeleteEntry -> deleteEntry(event.entryId)
        }
    }

    private fun loadHistory() {
        _state.value = HistoryState.Loading
        historyRepository.getAllHistory()
            .onEach { entries ->
                _state.value = HistoryState.Success(entries)
            }
            .launchIn(viewModelScope)
    }

    private fun deleteEntry(entryId: String) {
        // Implement delete logic if needed
    }
}