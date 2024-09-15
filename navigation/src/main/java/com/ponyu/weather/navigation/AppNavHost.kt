package com.ponyu.weather.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ponyu.weather.feature.splash.SplashRoute

object NavRoutes {
    const val SPLASH = "splash"
    const val HOME = "home"
}

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = NavRoutes.SPLASH,
        modifier = modifier
    ) {
        // Splash Screen
        composable(NavRoutes.SPLASH) {
            SplashRoute {
                navController.navigate(NavRoutes.HOME) {
                    popUpTo(NavRoutes.SPLASH) { inclusive = true }
                }
            }
        }

        // Home Screen with BottomNavigation
        composable(NavRoutes.HOME) {
            BottomScreen()
        }
    }
}