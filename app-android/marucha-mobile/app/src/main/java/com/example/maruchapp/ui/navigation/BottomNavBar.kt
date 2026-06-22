package com.example.maruchapp.ui.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavBar(
    navController: NavHostController
) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Orders,
        BottomNavItem.Profile
    )

    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    when (item.route) {
                        BottomNavItem.Home.route -> {
                            navController.navigate(BottomNavItem.Home.route) {
                                popUpTo(BottomNavItem.Home.route) {
                                    inclusive = false
                                }
                                launchSingleTop = true
                            }
                        }

                        BottomNavItem.Orders.route -> {
                            navController.navigate(BottomNavItem.Orders.route) {
                                popUpTo(BottomNavItem.Home.route) {
                                    inclusive = false
                                }
                                launchSingleTop = true
                            }
                        }

                        BottomNavItem.Profile.route -> {
                            navController.navigate(BottomNavItem.Profile.route) {
                                popUpTo(BottomNavItem.Home.route) {
                                    inclusive = false
                                }
                                launchSingleTop = true
                            }
                        }
                    }
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label
                    )
                },
                label = {
                    Text(item.label)
                }
            )
        }
    }
}