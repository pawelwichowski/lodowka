package com.reypablo.lodowka.domain.models

data class ShoppingItem(
    val id: String? = null,
    val name: String,
    val quantity: Double? = null,
    val unit: String? = null,
    val isChecked: Boolean = false,
    val addedAt: Long = System.currentTimeMillis()
)
