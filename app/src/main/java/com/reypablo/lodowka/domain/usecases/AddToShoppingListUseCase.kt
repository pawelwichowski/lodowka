package com.reypablo.lodowka.domain.usecases

import com.reypablo.lodowka.domain.models.RecipeIngredient
import com.reypablo.lodowka.domain.models.ShoppingItem
import com.reypablo.lodowka.domain.repositories.ShoppingRepository
import javax.inject.Inject

class AddToShoppingListUseCase @Inject constructor(
    private val shoppingRepository: ShoppingRepository
) {
    suspend operator fun invoke(items: List<RecipeIngredient>, recipeId: String? = null): Result<Unit> {
        return try {
            items.forEach { item ->
                shoppingRepository.addItem(
                    ShoppingItem(
                        name = item.name,
                        quantity = item.quantity,
                        unit = item.unit
                    )
                )
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
