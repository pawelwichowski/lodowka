package com.reypablo.lodowka.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history_entries")
data class HistoryEntryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val recipeId: String,
    val recipeName: String,
    val recipeImageUrl: String? = null,
    val madeAt: Long = System.currentTimeMillis()
)
