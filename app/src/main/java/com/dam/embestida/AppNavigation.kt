package com.dam.embestida


import android.util.Log
import androidx.compose.animation.core.EaseInQuart
import androidx.compose.animation.core.EaseOutQuart
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

sealed class AppScreen(val route: String) {
    data object HomeScreen : AppScreen("home_screen")
    data object GameScreen : AppScreen("game_screen")
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    Log.d(
        "AppNavigation",
        "Current route: ${navController.currentBackStackEntryAsState().value?.destination?.route}"
    )

    NavHost(navController = navController,
        startDestination = AppScreen.HomeScreen.route,
        enterTransition = {
            fadeIn(animationSpec = tween(700, easing = EaseInQuart))
        },
        exitTransition = {
            fadeOut(animationSpec = tween(700, easing = EaseOutQuart))
        }
    ) {
        composable(AppScreen.HomeScreen.route) {
            HomeScreen(navController)
        }
        composable(AppScreen.GameScreen.route) {
            GameScreen(navController)
        }
    }
}