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
private const val DEFAULT_DURATION = 800

fun defaultEnterTransition(
    targetScreen: String
): EnterTransition {
    return when (targetScreen) {
        BottomNavItem.Home.route -> fadeIn(
            animationSpec = tween(durationMillis = DEFAULT_DURATION, easing = FastOutSlowInEasing)
        )
        BottomNavItem.Favorites.route ->
            slideInHorizontally(
                initialOffsetX = { -DEFAULT_OFFSET_X },
                animationSpec = tween(durationMillis = DEFAULT_DURATION, easing = FastOutSlowInEasing)
            ) + fadeIn(
                animationSpec = tween(durationMillis = DEFAULT_DURATION, easing = FastOutSlowInEasing)
            )
        BottomNavItem.Forecast.route -> slideInHorizontally(
                initialOffsetX = { DEFAULT_OFFSET_X },
                animationSpec = tween(durationMillis = DEFAULT_DURATION, easing = FastOutSlowInEasing)
            ) + fadeIn(
                animationSpec = tween(durationMillis = DEFAULT_DURATION, easing = FastOutSlowInEasing)
            )
        else -> fadeIn(
            animationSpec = tween(durationMillis = DEFAULT_DURATION, easing = FastOutSlowInEasing)
        )
    }
}

fun defaultExitTransition(
    targetScreen: String
): ExitTransition {
    return when (targetScreen) {
        BottomNavItem.Home.route -> slideOutHorizontally(
                targetOffsetX = { -DEFAULT_OFFSET_X },
                animationSpec = tween(durationMillis = DEFAULT_DURATION, easing = FastOutSlowInEasing)
            ) + fadeOut(
                animationSpec = tween(durationMillis = DEFAULT_DURATION, easing = FastOutSlowInEasing)
        )
        BottomNavItem.Favorites.route -> slideOutHorizontally(
                targetOffsetX = { -DEFAULT_OFFSET_X },
                animationSpec = tween(durationMillis = DEFAULT_DURATION, easing = FastOutSlowInEasing)
            ) + fadeOut(
                animationSpec = tween(durationMillis = DEFAULT_DURATION, easing = FastOutSlowInEasing)
        )
        BottomNavItem.Forecast.route -> slideOutHorizontally(
            targetOffsetX = { DEFAULT_OFFSET_X },
            animationSpec = tween(durationMillis = DEFAULT_DURATION, easing = FastOutSlowInEasing)
        ) + fadeOut(
            animationSpec = tween(durationMillis = DEFAULT_DURATION, easing = FastOutSlowInEasing)
        )
        else -> fadeOut(
            animationSpec = tween(durationMillis = DEFAULT_DURATION, easing = FastOutSlowInEasing)
        )
    }
}

fun defaultPopEnterTransition(): EnterTransition {
    return slideInHorizontally() + fadeIn()
}

fun defaultPopExitTransition(): ExitTransition {
    return slideOutHorizontally() + fadeOut()
}