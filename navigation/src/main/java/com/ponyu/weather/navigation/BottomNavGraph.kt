package com.ponyu.weather.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ponyu.weather.feature.favorites.FavoritesRoute
import com.ponyu.weather.feature.forecast.ForecastRoute
import com.ponyu.weather.feature.home.HomeRoute

internal sealed class BottomNavItem(val title: String, val icon: ImageVector, val route: String) {
    data object Home : BottomNavItem("Home", Icons.Default.Home, "home_tab")
    data object Favorites : BottomNavItem("Favorites", Icons.Default.Favorite, "favorites_tab")
    data object Forecast : BottomNavItem("Forecast", Icons.Default.Info, "forecast_tab")
}

@Composable
internal fun BottomNavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = BottomNavItem.Home.route
    ) {
        composable(
            BottomNavItem.Home.route,
            enterTransition = { getEnterTransition(BottomNavItem.Home.route, initialState.destination.route ?: BottomNavItem.Home.route) },
            exitTransition = { getExitTransition(BottomNavItem.Home.route, targetState.destination.route ?: BottomNavItem.Home.route) },
            popEnterTransition = { getEnterTransition(BottomNavItem.Home.route, initialState.destination.route ?: BottomNavItem.Home.route) },
            popExitTransition = { getExitTransition(BottomNavItem.Home.route, targetState.destination.route ?: BottomNavItem.Home.route) },
        ) {
            HomeRoute()
        }

        composable(
            BottomNavItem.Favorites.route,
            enterTransition = { getEnterTransition(BottomNavItem.Favorites.route, initialState.destination.route ?: BottomNavItem.Favorites.route) },
            exitTransition = { getExitTransition(BottomNavItem.Favorites.route, targetState.destination.route ?: BottomNavItem.Favorites.route) },
            popEnterTransition = { getEnterTransition(BottomNavItem.Favorites.route, initialState.destination.route ?: BottomNavItem.Favorites.route) },
            popExitTransition = { getExitTransition(BottomNavItem.Favorites.route, targetState.destination.route ?: BottomNavItem.Favorites.route) },
        ) {
            FavoritesRoute()
        }

        composable(
            BottomNavItem.Forecast.route,
            enterTransition = { getEnterTransition(BottomNavItem.Forecast.route, initialState.destination.route ?: BottomNavItem.Forecast.route) },
            exitTransition = { getExitTransition(BottomNavItem.Forecast.route,  targetState.destination.route ?: BottomNavItem.Forecast.route) },
            popEnterTransition = { getEnterTransition(BottomNavItem.Forecast.route, initialState.destination.route ?: BottomNavItem.Forecast.route) },
            popExitTransition = { getExitTransition(BottomNavItem.Forecast.route,  targetState.destination.route ?: BottomNavItem.Forecast.route) },
        ) {
            ForecastRoute()
        }
    }
}
