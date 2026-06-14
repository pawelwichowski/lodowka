package com.reypablo.lodowka.data.di

import com.reypablo.lodowka.domain.repositories.FridgeRepository
import com.reypablo.lodowka.domain.repositories.HistoryRepository
import com.reypablo.lodowka.domain.repositories.RecipeRepository
import com.reypablo.lodowka.domain.repositories.ShoppingRepository
import com.reypablo.lodowka.domain.usecases.AddToHistoryUseCase
import com.reypablo.lodowka.domain.usecases.AddToShoppingListUseCase
import com.reypablo.lodowka.domain.usecases.GetRecipesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Provides
    @Singleton
    fun provideGetRecipesUseCase(
        recipeRepository: RecipeRepository,
        fridgeRepository: FridgeRepository
    ): GetRecipesUseCase = GetRecipesUseCase(recipeRepository, fridgeRepository)

    @Provides
    @Singleton
    fun provideAddToShoppingListUseCase(
        shoppingRepository: ShoppingRepository
    ): AddToShoppingListUseCase = AddToShoppingListUseCase(shoppingRepository)

    @Provides
    @Singleton
    fun provideAddToHistoryUseCase(
        historyRepository: HistoryRepository
    ): AddToHistoryUseCase = AddToHistoryUseCase(historyRepository)
}
