package com.reypablo.lodowka.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fridge_ingredients")
data class FridgeIngredientEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val quantity: Double? = null,
    val unit: String? = null,
    val category: String? = null,
    val addedAt: Long = System.currentTimeMillis()
)
