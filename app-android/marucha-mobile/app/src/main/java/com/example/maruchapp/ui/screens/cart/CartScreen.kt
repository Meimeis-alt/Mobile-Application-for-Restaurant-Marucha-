package com.example.maruchapp.ui.screens.cart

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.maruchapp.data.model.CartItemDto
import com.example.maruchapp.data.model.CartUpdateRequest
import com.example.maruchapp.data.remote.RetrofitClient
import kotlinx.coroutines.launch

@Composable
fun CartScreen(
    onBack: () -> Unit = {},
    onContinueCheckout: () -> Unit = {}
) {
    var isLoading by remember { mutableStateOf(true) }
    var isUpdating by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var cartItems by remember { mutableStateOf<List<CartItemDto>>(emptyList()) }
    var total by remember { mutableStateOf(0.0) }

    val scope = rememberCoroutineScope()

    suspend fun loadCart() {
        try {
            isLoading = true
            val response = RetrofitClient.cartApiService.getActiveCart(userId = 1)

            if (response.isSuccessful && response.body()?.success == true) {
                val cartData = response.body()?.data
                cartItems = cartData?.items ?: emptyList()
                total = cartData?.total ?: 0.0
                errorMessage = null
            } else {
                errorMessage = response.body()?.message ?: "No se pudo obtener el carrito"
            }
        } catch (e: Exception) {
            errorMessage = "Error: ${e.message}"
        } finally {
            isLoading = false
        }
    }

    fun increaseItem(item: CartItemDto) {
        scope.launch {
            try {
                isUpdating = true
                RetrofitClient.cartApiService.updateCartItem(
                    cartItemId = item.id_carrito_detalle,
                    request = CartUpdateRequest(cantidad = item.cantidad + 1)
                )
                loadCart()
            } catch (e: Exception) {
                errorMessage = "Error al actualizar: ${e.message}"
            } finally {
                isUpdating = false
            }
        }
    }

    fun decreaseItem(item: CartItemDto) {
        scope.launch {
            try {
                isUpdating = true

                if (item.cantidad <= 1) {
                    RetrofitClient.cartApiService.deleteCartItem(item.id_carrito_detalle)
                } else {
                    RetrofitClient.cartApiService.updateCartItem(
                        cartItemId = item.id_carrito_detalle,
                        request = CartUpdateRequest(cantidad = item.cantidad - 1)
                    )
                }

                loadCart()
            } catch (e: Exception) {
                errorMessage = "Error al actualizar: ${e.message}"
            } finally {
                isUpdating = false
            }
        }
    }

    fun deleteItem(item: CartItemDto) {
        scope.launch {
            try {
                isUpdating = true
                RetrofitClient.cartApiService.deleteCartItem(item.id_carrito_detalle)
                loadCart()
            } catch (e: Exception) {
                errorMessage = "Error al eliminar: ${e.message}"
            } finally {
                isUpdating = false
            }
        }
    }

    LaunchedEffect(Unit) {
        loadCart()
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TextButton(
            onClick = onBack,
            modifier = Modifier.padding(start = 8.dp, top = 8.dp)
        ) {
            Text("← Volver")
        }

        when {
            isLoading -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            errorMessage != null -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = errorMessage ?: "Error desconocido",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            cartItems.isEmpty() -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Tu carrito está vacío",
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        Text(
                            text = "Mi carrito",
                            style = MaterialTheme.typography.headlineMedium
                        )
                    }

                    items(cartItems) { item ->
                        CartItemCard(
                            item = item,
                            isUpdating = isUpdating,
                            onIncrease = { increaseItem(item) },
                            onDecrease = { decreaseItem(item) },
                            onDelete = { deleteItem(item) }
                        )
                    }
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Total: S/ $total",
                            style = MaterialTheme.typography.titleLarge
                        )

                        Button(
                            onClick = onContinueCheckout,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 12.dp),
                            enabled = !isUpdating
                        ) {
                            Text("Continuar")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CartItemCard(
    item: CartItemDto,
    isUpdating: Boolean,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = item.platillo_nombre,
                style = MaterialTheme.typography.titleMedium
            )

            if (!item.platillo_descripcion.isNullOrBlank()) {
                Text(
                    text = item.platillo_descripcion,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 6.dp)
                )
            }

            Text(
                text = "Precio unitario: S/ ${item.precio_unitario}",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 8.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Cantidad: ${item.cantidad}",
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = "S/ ${item.subtotal}",
                    style = MaterialTheme.typography.titleSmall
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onDecrease,
                    enabled = !isUpdating
                ) {
                    Text("-")
                }

                OutlinedButton(
                    onClick = onIncrease,
                    enabled = !isUpdating
                ) {
                    Text("+")
                }

                OutlinedButton(
                    onClick = onDelete,
                    enabled = !isUpdating
                ) {
                    Text("Eliminar")
                }
            }
        }
    }
}