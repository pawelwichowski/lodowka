package com.reypablo.lodowka.domain.models

data class HistoryEntry(
    val id: String? = null,
    val recipeId: String,
    val recipeName: String,
    val recipeImageUrl: String? = null,
    val madeAt: Long = System.currentTimeMillis()
)
