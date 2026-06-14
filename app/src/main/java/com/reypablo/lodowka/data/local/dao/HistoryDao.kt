package com.reypablo.lodowka.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.reypablo.lodowka.data.local.entities.HistoryEntryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: HistoryEntryEntity)

    @Query("SELECT * FROM history_entries ORDER BY madeAt DESC")
    fun getAll(): Flow<List<HistoryEntryEntity>>

    @Query("SELECT * FROM history_entries WHERE recipeId = :recipeId")
    suspend fun getByRecipeId(recipeId: String): HistoryEntryEntity?

    @Query("DELETE FROM history_entries WHERE id = :id")
    suspend fun delete(id: Int)

    @Query("DELETE FROM history_entries")
    suspend fun clearAll()
}
