package com.reypablo.lodowka.data.repositories

import com.reypablo.lodowka.data.local.dao.ShoppingDao
import com.reypablo.lodowka.data.local.entities.ShoppingItemEntity
import com.reypablo.lodowka.domain.models.ShoppingItem
import com.reypablo.lodowka.domain.repositories.ShoppingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShoppingRepositoryImpl @Inject constructor(
    private val shoppingDao: ShoppingDao
) : ShoppingRepository {

    override fun getShoppingList(): Flow<List<ShoppingItem>> {
        return shoppingDao.getAll().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun addItem(item: ShoppingItem) {
        shoppingDao.insert(item.toEntity())
    }

    override suspend fun updateItem(item: ShoppingItem) {
        shoppingDao.update(item.toEntity())
    }

    override suspend fun deleteItem(item: ShoppingItem) {
        shoppingDao.delete(item.toEntity())
    }

    override suspend fun toggleItemChecked(itemId: String, isChecked: Boolean) {
        val id = itemId.toIntOrNull() ?: return
        shoppingDao.toggleChecked(id, isChecked)
    }

    override suspend fun clearCompletedItems() {
        shoppingDao.clearCompleted()
    }

    private fun ShoppingItemEntity.toDomain(): ShoppingItem {
        return ShoppingItem(
            id = id.toString(),
            name = name,
            quantity = quantity,
            unit = unit,
            isChecked = isChecked,
            addedAt = addedAt
        )
    }

    private fun ShoppingItem.toEntity(): ShoppingItemEntity {
        return ShoppingItemEntity(
            id = id?.toIntOrNull() ?: 0,
            name = name,
            quantity = quantity,
            unit = unit,
            isChecked = isChecked,
            addedAt = addedAt
        )
    }
}
