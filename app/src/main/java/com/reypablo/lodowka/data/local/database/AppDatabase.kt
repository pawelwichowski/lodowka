package com.reypablo.lodowka.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.reypablo.lodowka.data.local.dao.FridgeDao
import com.reypablo.lodowka.data.local.dao.HistoryDao
import com.reypablo.lodowka.data.local.dao.RecipeDao
import com.reypablo.lodowka.data.local.dao.ShoppingDao
import com.reypablo.lodowka.data.local.entities.FridgeIngredientEntity
import com.reypablo.lodowka.data.local.entities.HistoryEntryEntity
import com.reypablo.lodowka.data.local.entities.RecipeEntity
import com.reypablo.lodowka.data.local.entities.ShoppingItemEntity

@Database(
    entities = [
        FridgeIngredientEntity::class,
        RecipeEntity::class,
        ShoppingItemEntity::class,
        HistoryEntryEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun fridgeDao(): FridgeDao
    abstract fun recipeDao(): RecipeDao
    abstract fun shoppingDao(): ShoppingDao
    abstract fun historyDao(): HistoryDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "lodowka_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}