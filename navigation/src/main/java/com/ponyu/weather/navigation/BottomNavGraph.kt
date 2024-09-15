package com.ponyu.weather.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ponyu.weather.feature.favorites.FavoritesRoute
import com.ponyu.weather.feature.forecast.ForecastRoute
import com.ponyu.weather.feature.home.HomeRoute

sealed class BottomNavItem(val title: String, val icon: ImageVector, val route: String) {
    data object Home : BottomNavItem("Home", Icons.Default.Home, "home_tab")
    data object Favorites : BottomNavItem("Favorites", Icons.Default.Favorite, "favorites_tab")
    data object Forecast : BottomNavItem("Forecast", Icons.Default.Info, "forecast_tab")
}


@Composable
fun BottomNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = BottomNavItem.Home.route
    ) {
        composable(
            BottomNavItem.Home.route,
            enterTransition = { defaultEnterTransition(BottomNavItem.Home.route) },
            exitTransition = { defaultExitTransition(BottomNavItem.Home.route) },
            popEnterTransition = { defaultPopEnterTransition() },
            popExitTransition = { defaultPopExitTransition() },
        ) {
            HomeRoute()
        }

        composable(
            BottomNavItem.Favorites.route,
            enterTransition = { defaultEnterTransition(BottomNavItem.Favorites.route) },
            exitTransition = { defaultExitTransition(BottomNavItem.Favorites.route) },
            popEnterTransition = { defaultPopEnterTransition() },
            popExitTransition = { defaultPopExitTransition() },
        ) {
            FavoritesRoute()
        }

        composable(
            BottomNavItem.Forecast.route,
            enterTransition = { defaultEnterTransition(BottomNavItem.Forecast.route) },
            exitTransition = { defaultExitTransition(BottomNavItem.Forecast.route) },
            popEnterTransition = { defaultPopEnterTransition() },
            popExitTransition = { defaultPopExitTransition() },
        ) {
            ForecastRoute()
        }
    }
}
