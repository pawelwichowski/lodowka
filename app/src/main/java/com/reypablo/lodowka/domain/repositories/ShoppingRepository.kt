package com.reypablo.lodowka.domain.repositories

import com.reypablo.lodowka.domain.models.ShoppingItem
import kotlinx.coroutines.flow.Flow

interface ShoppingRepository {
    fun getShoppingList(): Flow<List<ShoppingItem>>
    suspend fun addItem(item: ShoppingItem)
    suspend fun updateItem(item: ShoppingItem)
    suspend fun deleteItem(item: ShoppingItem)
    suspend fun toggleItemChecked(itemId: String, isChecked: Boolean)
    suspend fun clearCompletedItems()
}
