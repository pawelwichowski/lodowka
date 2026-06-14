package com.reypablo.lodowka.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shopping_items")
data class ShoppingItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val quantity: Double? = null,
    val unit: String? = null,
    val isChecked: Boolean = false,
    val addedAt: Long = System.currentTimeMillis()
)
