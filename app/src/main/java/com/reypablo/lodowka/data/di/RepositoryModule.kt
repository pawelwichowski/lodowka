package com.reypablo.lodowka.data.di

import android.content.Context
import com.reypablo.lodowka.data.local.dao.FridgeDao
import com.reypablo.lodowka.data.local.dao.HistoryDao
import com.reypablo.lodowka.data.local.dao.RecipeDao
import com.reypablo.lodowka.data.local.dao.ShoppingDao
import com.reypablo.lodowka.data.remote.api.RecipeApiService
import com.reypablo.lodowka.data.repositories.FridgeRepositoryImpl
import com.reypablo.lodowka.data.repositories.HistoryRepositoryImpl
import com.reypablo.lodowka.data.repositories.RecipeRepositoryImpl
import com.reypablo.lodowka.data.repositories.ShoppingRepositoryImpl
import com.reypablo.lodowka.domain.repositories.FridgeRepository
import com.reypablo.lodowka.domain.repositories.HistoryRepository
import com.reypablo.lodowka.domain.repositories.RecipeRepository
import com.reypablo.lodowka.domain.repositories.ShoppingRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideFridgeRepository(fridgeDao: FridgeDao): FridgeRepository = FridgeRepositoryImpl(fridgeDao)

    @Provides
    @Singleton
    fun provideRecipeRepository(
        apiService: RecipeApiService,
        recipeDao: RecipeDao,
        fridgeRepository: FridgeRepository,
        @ApplicationContext context: Context
    ): RecipeRepository = RecipeRepositoryImpl(apiService, recipeDao, fridgeRepository, context)

    @Provides
    @Singleton
    fun provideShoppingRepository(shoppingDao: ShoppingDao): ShoppingRepository = ShoppingRepositoryImpl(shoppingDao)

    @Provides
    @Singleton
    fun provideHistoryRepository(historyDao: HistoryDao): HistoryRepository = HistoryRepositoryImpl(historyDao)
}
