package com.reypablo.lodowka.data.repositories

import com.reypablo.lodowka.data.local.dao.FridgeDao
import com.reypablo.lodowka.data.local.entities.FridgeIngredientEntity
import com.reypablo.lodowka.domain.models.Ingredient
import com.reypablo.lodowka.domain.repositories.FridgeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FridgeRepositoryImpl @Inject constructor(
    private val fridgeDao: FridgeDao
) : FridgeRepository {

    override fun getAllIngredients(): Flow<List<Ingredient>> {
        return fridgeDao.getAll().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun addIngredient(ingredient: Ingredient) {
        fridgeDao.insert(ingredient.toEntity())
    }

    override suspend fun updateIngredient(ingredient: Ingredient) {
        fridgeDao.update(ingredient.toEntity())
    }

    override suspend fun deleteIngredient(ingredient: Ingredient) {
        fridgeDao.delete(ingredient.toEntity())
    }

    override fun searchIngredients(query: String): Flow<List<Ingredient>> {
        return fridgeDao.search("%$query%").map { entities ->
            entities.map { it.toDomain() }
        }
    }

    private fun FridgeIngredientEntity.toDomain(): Ingredient {
        return Ingredient(
            id = id.toString(),
            name = name,
            quantity = quantity,
            unit = unit,
            category = category
        )
    }

    private fun Ingredient.toEntity(): FridgeIngredientEntity {
        return FridgeIngredientEntity(
            id = id?.toIntOrNull() ?: 0,
            name = name,
            quantity = quantity,
            unit = unit,
            category = category
        )
    }
}
