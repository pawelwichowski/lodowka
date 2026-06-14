package com.reypablo.lodowka.presentation.navigation

sealed class Screen(val route: String) {
    object Fridge : Screen("fridge")
    object Recipes : Screen("recipes")
    object RecipeDetail : Screen("recipe_detail/{recipeId}") {
        fun createRoute(recipeId: String) = "recipe_detail/$recipeId"
    }
    object ShoppingList : Screen("shopping_list")
    object History : Screen("history")
}
