package com.reypablo.lodowka.data.repositories

import com.reypablo.lodowka.data.local.dao.HistoryDao
import com.reypablo.lodowka.data.local.entities.HistoryEntryEntity
import com.reypablo.lodowka.domain.models.HistoryEntry
import com.reypablo.lodowka.domain.repositories.HistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HistoryRepositoryImpl @Inject constructor(
    private val historyDao: HistoryDao
) : HistoryRepository {

    override fun getHistory(): Flow<List<HistoryEntry>> {
        return historyDao.getAll().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun addToHistory(entry: HistoryEntry) {
        historyDao.insert(entry.toEntity())
    }

    override suspend fun removeFromHistory(entryId: String) {
        val id = entryId.toIntOrNull() ?: return
        historyDao.delete(id)
    }

    override suspend fun clearHistory() {
        historyDao.clearAll()
    }

    private fun HistoryEntryEntity.toDomain(): HistoryEntry {
        return HistoryEntry(
            id = id.toString(),
            recipeId = recipeId,
            recipeName = recipeName,
            recipeImageUrl = recipeImageUrl,
            madeAt = madeAt
        )
    }

    private fun HistoryEntry.toEntity(): HistoryEntryEntity {
        return HistoryEntryEntity(
            id = id?.toIntOrNull() ?: 0,
            recipeId = recipeId,
            recipeName = recipeName,
            recipeImageUrl = recipeImageUrl,
            madeAt = madeAt
        )
    }
}
