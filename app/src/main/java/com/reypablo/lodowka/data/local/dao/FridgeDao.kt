package com.reypablo.lodowka.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.reypablo.lodowka.data.local.entities.FridgeIngredientEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FridgeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(ingredient: FridgeIngredientEntity)

    @Update
    suspend fun update(ingredient: FridgeIngredientEntity)

    @Delete
    suspend fun delete(ingredient: FridgeIngredientEntity)

    @Query("SELECT * FROM fridge_ingredients ORDER BY name ASC")
    fun getAll(): Flow<List<FridgeIngredientEntity>>

    @Query("SELECT * FROM fridge_ingredients WHERE name LIKE :query ORDER BY name ASC")
    fun search(query: String): Flow<List<FridgeIngredientEntity>>

    @Query("DELETE FROM fridge_ingredients")
    suspend fun clearAll()
}
