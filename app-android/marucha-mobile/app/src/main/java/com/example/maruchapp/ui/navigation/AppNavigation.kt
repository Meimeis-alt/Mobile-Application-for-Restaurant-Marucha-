package com.example.maruchapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.maruchapp.ui.screens.auth.LoginScreen
import com.example.maruchapp.ui.screens.home.HomeScreen
import com.example.maruchapp.ui.screens.profile.ProfileScreen
import com.example.maruchapp.ui.screens.splash.SplashScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppDestinations.SPLASH
    ) {
        composable(AppDestinations.SPLASH) {
            SplashScreen(
                onNavigateToLogin = {
                    navController.navigate(AppDestinations.LOGIN) {
                        popUpTo(AppDestinations.SPLASH) { inclusive = true }
                    }
                }
            )
        }

        composable(AppDestinations.LOGIN) {
                LoginScreen(
                    onLoginSuccess = {
                        navController.navigate(AppDestinations.HOME) {
                            popUpTo(AppDestinations.LOGIN) { inclusive = true }
                        }
                    }
                )
            }

        composable(AppDestinations.HOME) {
            HomeScreen(
                onGoToProfile = {
                    navController.navigate(AppDestinations.PROFILE)
                }
            )
        }

        composable(AppDestinations.PROFILE) {
            ProfileScreen()
        }
    }
}