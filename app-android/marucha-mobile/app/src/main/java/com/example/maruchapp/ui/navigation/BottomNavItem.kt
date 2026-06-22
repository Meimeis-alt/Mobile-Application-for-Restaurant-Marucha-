package com.example.maruchapp.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector
) {
    data object Home : BottomNavItem(
        route = "home",
        label = "Inicio",
        icon = Icons.Outlined.Home
    )

    data object Orders : BottomNavItem(
        route = "orders",
        label = "Pedidos",
        icon = Icons.Outlined.List
    )

    data object Profile : BottomNavItem(
        route = "profile",
        label = "Perfil",
        icon = Icons.Outlined.Person
    )
}