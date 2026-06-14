package com.reypablo.lodowka.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipes")
data class RecipeEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String? = null,
    val imageUrl: String? = null,
    val lastUpdated: Long = System.currentTimeMillis()
)
