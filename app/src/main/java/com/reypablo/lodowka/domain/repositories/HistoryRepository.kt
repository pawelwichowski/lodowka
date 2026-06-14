package com.reypablo.lodowka.domain.repositories

import com.reypablo.lodowka.domain.models.HistoryEntry
import kotlinx.coroutines.flow.Flow

interface HistoryRepository {
    fun getHistory(): Flow<List<HistoryEntry>>
    suspend fun addToHistory(entry: HistoryEntry)
    suspend fun removeFromHistory(entryId: String)
    suspend fun clearHistory()
}
