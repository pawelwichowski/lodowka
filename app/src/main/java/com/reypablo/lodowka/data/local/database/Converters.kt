package com.reypablo.lodowka.data.local.database

import androidx.room.TypeConverter
import com.reypablo.lodowka.domain.models.RecipeIngredient
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class Converters {
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
    
    private val recipeIngredientListType = Types.newParameterizedType(
        List::class.java, 
        RecipeIngredient::class.java
    )
    private val recipeIngredientListAdapter = moshi.adapter(recipeIngredientListType)
    
    private val stringListType = Types.newParameterizedType(
        List::class.java, 
        String::class.java
    )
    private val stringListAdapter = moshi.adapter(stringListType)

    @TypeConverter
    fun fromRecipeIngredientList(ingredients: List<RecipeIngredient>?): String? {
        return ingredients?.let { recipeIngredientListAdapter.toJson(it) }
    }

    @TypeConverter
    fun toRecipeIngredientList(json: String?): List<RecipeIngredient>? {
        return json?.let { recipeIngredientListAdapter.fromJson(it) }
    }

    @TypeConverter
    fun fromStringList(list: List<String>?): String? {
        return list?.let { stringListAdapter.toJson(it) }
    }

    @TypeConverter
    fun toStringList(json: String?): List<String>? {
        return json?.let { stringListAdapter.fromJson(it) }
    }
}