package com.reypablo.lodowka.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.reypablo.lodowka.presentation.screens.fridge.FridgeScreen
import com.reypablo.lodowka.presentation.screens.fridge.FridgeViewModel
import com.reypablo.lodowka.presentation.screens.history.HistoryScreen
import com.reypablo.lodowka.presentation.screens.history.HistoryViewModel
import com.reypablo.lodowka.presentation.screens.recipe_detail.RecipeDetailScreen
import com.reypablo.lodowka.presentation.screens.recipe_detail.RecipeDetailViewModel
import com.reypablo.lodowka.presentation.screens.recipes.RecipesScreen
import com.reypablo.lodowka.presentation.screens.recipes.RecipesViewModel
import com.reypablo.lodowka.presentation.screens.shopping_list.ShoppingListScreen
import com.reypablo.lodowka.presentation.screens.shopping_list.ShoppingListViewModel

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    
    NavHost(
        navController = navController,
        startDestination = Screen.Fridge.route
    ) {
        composable(Screen.Fridge.route) {
            val viewModel: FridgeViewModel = hiltViewModel()
            FridgeScreen(
                state = viewModel.state.collectAsState().value,
                onEvent = viewModel::onEvent,
                onNavigateToRecipes = { navController.navigate(Screen.Recipes.route) }
            )
        }

        composable(Screen.Recipes.route) {
            val viewModel: RecipesViewModel = hiltViewModel()
            RecipesScreen(
                state = viewModel.state.collectAsState().value,
                onEvent = viewModel::onEvent,
                onRecipeClick = { recipeId ->
                    navController.navigate(Screen.RecipeDetail.createRoute(recipeId))
                },
                onNavigateToShoppingList = { navController.navigate(Screen.ShoppingList.route) },
                onNavigateToHistory = { navController.navigate(Screen.History.route) }
            )
        }

        composable(
            route = Screen.RecipeDetail.route,
            arguments = listOf(navArgument("recipeId") { type = NavType.StringType })
        ) { backStackEntry ->
            val recipeId = backStackEntry.arguments?.getString("recipeId") ?: ""
            val viewModel: RecipeDetailViewModel = hiltViewModel()
            RecipeDetailScreen(
                recipeId = recipeId,
                state = viewModel.state.collectAsState().value,
                onEvent = viewModel::onEvent,
                onAddToShoppingList = { navController.navigate(Screen.ShoppingList.route) },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.ShoppingList.route) {
            val viewModel: ShoppingListViewModel = hiltViewModel()
            ShoppingListScreen(
                state = viewModel.state.collectAsState().value,
                onEvent = viewModel::onEvent,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.History.route) {
            val viewModel: HistoryViewModel = hiltViewModel()
            HistoryScreen(
                state = viewModel.state.collectAsState().value,
                onEvent = viewModel::onEvent,
                onRecipeClick = { recipeId ->
                    navController.navigate(Screen.RecipeDetail.createRoute(recipeId))
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
