package com.reypablo.lodowka.data.local.database

import androidx.room.TypeConverter
import com.reypablo.lodowka.domain.models.RecipeIngredient
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.lang.reflect.ParameterizedType

class Converters {
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
    
    @TypeConverter
    fun fromRecipeIngredientList(ingredients: List<RecipeIngredient>?): String? {
        val type: ParameterizedType = Types.newParameterizedType(
            List::class.java,
            RecipeIngredient::class.java
        )
        val adapter = moshi.adapter<List<RecipeIngredient>>(type)
        return ingredients?.let { adapter.toJson(it) }
    }

    @TypeConverter
    fun toRecipeIngredientList(json: String?): List<RecipeIngredient>? {
        val type: ParameterizedType = Types.newParameterizedType(
            List::class.java,
            RecipeIngredient::class.java
        )
        val adapter = moshi.adapter<List<RecipeIngredient>>(type)
        return json?.let { adapter.fromJson(it) }
    }

    @TypeConverter
    fun fromStringList(list: List<String>?): String? {
        val type: ParameterizedType = Types.newParameterizedType(
            List::class.java,
            String::class.java
        )
        val adapter = moshi.adapter<List<String>>(type)
        return list?.let { adapter.toJson(it) }
    }

    @TypeConverter
    fun toStringList(json: String?): List<String>? {
        val type: ParameterizedType = Types.newParameterizedType(
            List::class.java,
            String::class.java
        )
        val adapter = moshi.adapter<List<String>>(type)
        return json?.let { adapter.fromJson(it) }
    }
}