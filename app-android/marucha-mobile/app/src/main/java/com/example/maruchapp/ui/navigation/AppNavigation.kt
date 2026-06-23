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
import com.example.maruchapp.data.local.SessionManager
import com.example.maruchapp.data.model.CartAddRequest
import com.example.maruchapp.data.remote.RetrofitClient
import com.example.maruchapp.ui.screens.auth.LoginScreen
import com.example.maruchapp.ui.screens.auth.RegisterScreen
import com.example.maruchapp.ui.screens.cart.CartScreen
import com.example.maruchapp.ui.screens.checkout.CheckoutScreen
import com.example.maruchapp.ui.screens.home.HomeScreen
import com.example.maruchapp.ui.screens.orders.OrderDetailScreen
import com.example.maruchapp.ui.screens.orders.OrdersScreen
import com.example.maruchapp.ui.screens.product.ProductDetailScreen
import com.example.maruchapp.ui.screens.profile.AddressesScreen
import com.example.maruchapp.ui.screens.profile.PaymentMethodsScreen
import com.example.maruchapp.ui.screens.profile.ProfileScreen
import com.example.maruchapp.ui.screens.splash.SplashScreen
import kotlinx.coroutines.launch

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

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
                },
                onNavigateToMain = {
                    navController.navigate("main") {
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
                },
                onGoToRegister = {
                    navController.navigate("register")
                }
            )
        }

        composable("register") {
            RegisterScreen(
                onBackToLogin = {
                    navController.popBackStack()
                }
            )
        }

        composable("main") {
            MainScreenContainer(rootNavController = navController)
        }
    }
}

@Composable
private fun MainScreenContainer(
    rootNavController: NavHostController
) {
    val bottomNavController = rememberNavController()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val session = SessionManager(context)

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
                ProfileScreen(
                    onBackClick = {
                        bottomNavController.popBackStack()
                    },
                    onAddressesClick = {
                        bottomNavController.navigate("addresses")
                    },
                    onPaymentMethodsClick = {
                        bottomNavController.navigate("payment_methods")
                    },
                    onOrdersClick = {
                        bottomNavController.navigate(BottomNavItem.Orders.route)
                    },
                    onSettingsClick = {
                    },
                    onLogoutClick = {
                        session.clearSession()

                        rootNavController.navigate("login") {
                            popUpTo("main") { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
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
                                        userId = session.getUserId(),
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

            composable("addresses") {
                AddressesScreen(
                    onBackClick = { bottomNavController.popBackStack() }
                )
            }

            composable("payment_methods") {
                PaymentMethodsScreen(
                    onBackClick = { bottomNavController.popBackStack() }
                )
            }
        }
    }
}