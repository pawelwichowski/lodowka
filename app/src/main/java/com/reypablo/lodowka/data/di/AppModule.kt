package com.reypablo.lodowka.data.di

import android.content.Context
import androidx.room.Room
import com.reypablo.lodowka.data.local.dao.FridgeDao
import com.reypablo.lodowka.data.local.dao.HistoryDao
import com.reypablo.lodowka.data.local.dao.RecipeDao
import com.reypablo.lodowka.data.local.dao.ShoppingDao
import com.reypablo.lodowka.data.local.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "lodowka_db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideFridgeDao(database: AppDatabase): FridgeDao = database.fridgeDao()

    @Provides
    @Singleton
    fun provideRecipeDao(database: AppDatabase): RecipeDao = database.recipeDao()

    @Provides
    @Singleton
    fun provideShoppingDao(database: AppDatabase): ShoppingDao = database.shoppingDao()

    @Provides
    @Singleton
    fun provideHistoryDao(database: AppDatabase): HistoryDao = database.historyDao()
}
