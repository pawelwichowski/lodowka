package com.reypablo.lodowka.domain.usecases

import com.reypablo.lodowka.domain.models.HistoryEntry
import com.reypablo.lodowka.domain.models.Recipe
import com.reypablo.lodowka.domain.repositories.HistoryRepository
import javax.inject.Inject

class AddToHistoryUseCase @Inject constructor(
    private val historyRepository: HistoryRepository
) {
    suspend operator fun invoke(recipe: Recipe): Result<Unit> {
        return try {
            historyRepository.addToHistory(
                HistoryEntry(
                    recipeId = recipe.id,
                    recipeName = recipe.name,
                    recipeImageUrl = recipe.imageUrl
                )
            )
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
