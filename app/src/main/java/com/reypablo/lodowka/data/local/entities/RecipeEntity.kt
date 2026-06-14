package com.reypablo.lodowka.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.reypablo.lodowka.domain.models.RecipeIngredient

@Entity(tableName = "recipes")
data class RecipeEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String? = null,
    val imageUrl: String? = null,
    val ingredients: List<RecipeIngredient> = emptyList(),
    val instructions: List<String> = emptyList(),
    val lastUpdated: Long = System.currentTimeMillis()
)