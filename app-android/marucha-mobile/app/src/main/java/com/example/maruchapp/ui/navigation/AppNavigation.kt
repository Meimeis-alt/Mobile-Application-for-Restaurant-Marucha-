package com.example.maruchapp.ui.navigation

import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.maruchapp.data.model.CartAddRequest
import com.example.maruchapp.data.remote.RetrofitClient
import com.example.maruchapp.ui.screens.auth.LoginScreen
import com.example.maruchapp.ui.screens.cart.CartScreen
import com.example.maruchapp.ui.screens.checkout.CheckoutScreen
import com.example.maruchapp.ui.screens.home.HomeScreen
import com.example.maruchapp.ui.screens.orders.OrderDetailScreen
import com.example.maruchapp.ui.screens.orders.OrdersScreen
import com.example.maruchapp.ui.screens.product.ProductDetailScreen
import com.example.maruchapp.ui.screens.profile.ProfileScreen
import com.example.maruchapp.ui.screens.splash.SplashScreen
import kotlinx.coroutines.launch

@Composable
fun AppNavigation(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        composable("splash") {
            SplashScreen(
                onNavigateToLogin = {
                    navController.navigate("login") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            )
        }

        composable("login") {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate("main") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        composable("main") {
            MainScreenContainer()
        }
    }
}

@Composable
private fun MainScreenContainer() {
    val bottomNavController = rememberNavController()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    Scaffold(
        bottomBar = {
            BottomNavBar(navController = bottomNavController)
        }
    ) { innerPadding ->
        NavHost(
            navController = bottomNavController,
            startDestination = BottomNavItem.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(BottomNavItem.Home.route) {
                HomeScreen(
                    onProductClick = { product ->
                        SelectedProductHolder.product = product
                        bottomNavController.navigate("product_detail")
                    },
                    onGoToCart = {
                        bottomNavController.navigate("cart")
                    }
                )
            }

            composable(BottomNavItem.Orders.route) {
                OrdersScreen(
                    onOrderClick = { orderId ->
                        bottomNavController.navigate("order_detail/$orderId")
                    }
                )
            }

            composable(BottomNavItem.Profile.route) {
                ProfileScreen()
            }

            composable("product_detail") {
                val selectedProduct = SelectedProductHolder.product

                if (selectedProduct != null) {
                    ProductDetailScreen(
                        product = selectedProduct,
                        onBack = {
                            bottomNavController.popBackStack()
                        },
                        onAddToCart = { quantity ->
                            scope.launch {
                                try {
                                    val request = CartAddRequest(
                                        id_platillo = selectedProduct.id_platillo,
                                        cantidad = quantity
                                    )

                                    val response = RetrofitClient.cartApiService.addItemToCart(
                                        userId = 1,
                                        request = request
                                    )

                                    if (response.isSuccessful && response.body()?.success == true) {
                                        Toast.makeText(
                                            context,
                                            "Producto agregado al carrito",
                                            Toast.LENGTH_SHORT
                                        ).show()

                                        bottomNavController.popBackStack()
                                    } else {
                                        Toast.makeText(
                                            context,
                                            response.body()?.message ?: "No se pudo agregar al carrito",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                } catch (e: Exception) {
                                    Toast.makeText(
                                        context,
                                        "Error: ${e.message}",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                        }
                    )
                }
            }

            composable("cart") {
                CartScreen(
                    onBack = {
                        bottomNavController.popBackStack()
                    },
                    onContinueCheckout = {
                        bottomNavController.navigate("checkout")
                    }
                )
            }
            composable("checkout") {
                CheckoutScreen(
                    onBack = {
                        bottomNavController.popBackStack()
                    },
                    onOrderCreated = {
                        bottomNavController.navigate(BottomNavItem.Orders.route) {
                            popUpTo(BottomNavItem.Home.route) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
            composable("order_detail/{orderId}") { backStackEntry ->
                val orderId = backStackEntry.arguments?.getString("orderId")?.toIntOrNull()

                if (orderId != null) {
                    OrderDetailScreen(
                        orderId = orderId,
                        onBack = {
                            bottomNavController.popBackStack()
                        }
                    )
                }
            }
        }
    }
}