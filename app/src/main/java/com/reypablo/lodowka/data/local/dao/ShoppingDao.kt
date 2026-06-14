package com.reypablo.lodowka.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.reypablo.lodowka.data.local.entities.ShoppingItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ShoppingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: ShoppingItemEntity)

    @Update
    suspend fun update(item: ShoppingItemEntity)

    @Delete
    suspend fun delete(item: ShoppingItemEntity)

    @Query("SELECT * FROM shopping_items WHERE isChecked = 0 ORDER BY addedAt DESC")
    fun getActiveItems(): Flow<List<ShoppingItemEntity>>

    @Query("SELECT * FROM shopping_items WHERE isChecked = 1 ORDER BY addedAt DESC")
    fun getCompletedItems(): Flow<List<ShoppingItemEntity>>

    @Query("SELECT * FROM shopping_items ORDER BY addedAt DESC")
    fun getAll(): Flow<List<ShoppingItemEntity>>

    @Query("UPDATE shopping_items SET isChecked = :isChecked WHERE id = :id")
    suspend fun toggleChecked(id: Int, isChecked: Boolean)

    @Query("DELETE FROM shopping_items WHERE isChecked = 1")
    suspend fun clearCompleted()

    @Query("DELETE FROM shopping_items")
    suspend fun clearAll()
}
