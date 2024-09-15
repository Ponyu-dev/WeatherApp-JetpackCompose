package com.ponyu.weather.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally

private const val DEFAULT_OFFSET_X = 1000
private const val DEFAULT_DURATION_SLIDE = 600
private const val DEFAULT_DURATION_FADE = 1000

private fun slideInHorizontallyFadeIn(
    offset: Int
) : EnterTransition {
    return slideInHorizontally(
        initialOffsetX = { offset },
        animationSpec = tween(durationMillis = DEFAULT_DURATION_SLIDE, easing = FastOutSlowInEasing)
    ) + fadeIn(
        animationSpec = tween(durationMillis = DEFAULT_DURATION_FADE, easing = FastOutSlowInEasing)
    )
}

private fun slideOutHorizontallyFadeOut(
    offset: Int
) : ExitTransition {
    return slideOutHorizontally(
        targetOffsetX = { offset },
        animationSpec = tween(durationMillis = DEFAULT_DURATION_SLIDE, easing = FastOutSlowInEasing)
    ) + fadeOut(
        animationSpec = tween(durationMillis = DEFAULT_DURATION_FADE, easing = FastOutSlowInEasing)
    )
}

internal fun getEnterTransition(
    currentScreen: String,
    targetScreen: String
): EnterTransition =
    when (currentScreen) {
        BottomNavItem.Home.route -> when (targetScreen) {
            BottomNavItem.Favorites.route -> slideInHorizontallyFadeIn(DEFAULT_OFFSET_X)
            BottomNavItem.Forecast.route -> slideInHorizontallyFadeIn(-DEFAULT_OFFSET_X)
            else -> fadeIn()
        }
        BottomNavItem.Favorites.route -> when (targetScreen) {
            BottomNavItem.Home.route -> slideInHorizontallyFadeIn(-DEFAULT_OFFSET_X)
            BottomNavItem.Forecast.route -> slideInHorizontallyFadeIn(-DEFAULT_OFFSET_X)
            else -> fadeIn()
        }
        BottomNavItem.Forecast.route -> when (targetScreen) {
            BottomNavItem.Home.route -> slideInHorizontallyFadeIn(DEFAULT_OFFSET_X)
            BottomNavItem.Favorites.route -> slideInHorizontallyFadeIn(DEFAULT_OFFSET_X)
            else -> fadeIn()
        }
        else -> fadeIn()
    }

internal fun getExitTransition(
    currentScreen: String,
    targetScreen: String
): ExitTransition =
    when (currentScreen) {
        BottomNavItem.Home.route -> when (targetScreen) {
            BottomNavItem.Favorites.route -> slideOutHorizontallyFadeOut(DEFAULT_OFFSET_X)
            BottomNavItem.Forecast.route -> slideOutHorizontallyFadeOut(-DEFAULT_OFFSET_X)
            else -> fadeOut()
        }
        BottomNavItem.Favorites.route -> when (targetScreen) {
            BottomNavItem.Home.route -> slideOutHorizontallyFadeOut(-DEFAULT_OFFSET_X)
            BottomNavItem.Forecast.route -> slideOutHorizontallyFadeOut(-DEFAULT_OFFSET_X)
            else -> fadeOut()
        }
        BottomNavItem.Forecast.route -> when (targetScreen) {
            BottomNavItem.Home.route -> slideOutHorizontallyFadeOut(DEFAULT_OFFSET_X)
            BottomNavItem.Favorites.route -> slideOutHorizontallyFadeOut(DEFAULT_OFFSET_X)
            else -> fadeOut()
        }
        else -> fadeOut()
    }