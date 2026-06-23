package com.example.maruchapp.ui.screens.checkout

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.maruchapp.data.local.SessionManager
import com.example.maruchapp.data.model.AddressDto
import com.example.maruchapp.data.model.CartItemDto
import com.example.maruchapp.data.model.OrderCreateRequest
import com.example.maruchapp.data.remote.RetrofitClient
import kotlinx.coroutines.launch

@Composable
fun CheckoutScreen(
    onBack: () -> Unit = {},
    onOrderCreated: () -> Unit = {}
) {
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    val userId = sessionManager.getUserId()

    var isLoading by remember { mutableStateOf(true) }
    var isSubmitting by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    var cartItems by remember { mutableStateOf<List<CartItemDto>>(emptyList()) }
    var total by remember { mutableStateOf(0.0) }
    var addresses by remember { mutableStateOf<List<AddressDto>>(emptyList()) }

    var selectedAddressId by remember { mutableStateOf<Int?>(null) }
    var selectedPaymentMethodId by remember { mutableStateOf(1) }
    var observaciones by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()

    LaunchedEffect(userId) {
        try {
            val cartResponse = RetrofitClient.cartApiService.getActiveCart(userId = userId)
            val addressResponse = RetrofitClient.addressApiService.getUserAddresses(userId = userId)

            if (cartResponse.isSuccessful && cartResponse.body()?.success == true) {
                val cartData = cartResponse.body()?.data
                cartItems = cartData?.items ?: emptyList()
                total = cartData?.total ?: 0.0
            } else {
                errorMessage = cartResponse.body()?.message ?: "No se pudo obtener el carrito"
            }

            if (addressResponse.isSuccessful && addressResponse.body()?.success == true) {
                addresses = addressResponse.body()?.data ?: emptyList()

                selectedAddressId =
                    addresses.firstOrNull { it.esPrincipal == 1 }?.idDireccion
                        ?: addresses.firstOrNull()?.idDireccion
            } else if (errorMessage == null) {
                errorMessage = addressResponse.body()?.message ?: "No se pudieron obtener las direcciones"
            }

        } catch (e: Exception) {
            errorMessage = "Error: ${e.message}"
        } finally {
            isLoading = false
        }
    }

    fun confirmOrder() {
        val addressId = selectedAddressId
        if (addressId == null) {
            Toast.makeText(context, "Selecciona una dirección", Toast.LENGTH_SHORT).show()
            return
        }

        scope.launch {
            try {
                isSubmitting = true

                val response = RetrofitClient.orderApiService.createOrder(
                    userId = userId,
                    request = OrderCreateRequest(
                        id_direccion = addressId,
                        id_metodo_pago = selectedPaymentMethodId,
                        observaciones = observaciones.ifBlank { null }
                    )
                )

                if (response.isSuccessful && response.body()?.success == true) {
                    val orderNumber = response.body()?.data?.order?.numero_pedido

                    Toast.makeText(
                        context,
                        if (!orderNumber.isNullOrBlank()) {
                            "Pedido $orderNumber realizado correctamente"
                        } else {
                            "Pedido realizado correctamente"
                        },
                        Toast.LENGTH_SHORT
                    ).show()

                    onOrderCreated()
                } else {
                    Toast.makeText(
                        context,
                        response.body()?.message ?: "No se pudo crear el pedido",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(
                    context,
                    "Error: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            } finally {
                isSubmitting = false
            }
        }
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
                        color = MaterialTheme.colorScheme.error
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
                    Text("No hay productos en el carrito")
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        Text(
                            text = "Checkout",
                            style = MaterialTheme.typography.headlineMedium
                        )
                    }

                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "Resumen del pedido",
                                    style = MaterialTheme.typography.titleMedium
                                )

                                cartItems.forEach { item ->
                                    Text(
                                        text = "${item.platillo_nombre} x${item.cantidad} - S/ ${item.subtotal}",
                                        modifier = Modifier.padding(top = 8.dp)
                                    )
                                }

                                Text(
                                    text = "Total: S/ $total",
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier.padding(top = 12.dp)
                                )
                            }
                        }
                    }

                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "Selecciona una dirección",
                                    style = MaterialTheme.typography.titleMedium
                                )

                                if (addresses.isEmpty()) {
                                    Text(
                                        text = "No tienes direcciones registradas",
                                        modifier = Modifier.padding(top = 8.dp)
                                    )
                                } else {
                                    addresses.forEach { address ->
                                        Row(
                                            modifier = Modifier.padding(top = 8.dp)
                                        ) {
                                            RadioButton(
                                                selected = selectedAddressId == address.idDireccion,
                                                onClick = {
                                                    selectedAddressId = address.idDireccion
                                                }
                                            )

                                            Column(
                                                modifier = Modifier.padding(top = 10.dp)
                                            ) {
                                                Text(address.alias ?: "Dirección")
                                                Text(address.direccionTexto)

                                                if (!address.referencia.isNullOrBlank()) {
                                                    Text(address.referencia)
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "Método de pago",
                                    style = MaterialTheme.typography.titleMedium
                                )

                                PaymentOption(
                                    title = "Yape",
                                    selected = selectedPaymentMethodId == 1,
                                    onClick = { selectedPaymentMethodId = 1 }
                                )

                                PaymentOption(
                                    title = "Efectivo",
                                    selected = selectedPaymentMethodId == 2,
                                    onClick = { selectedPaymentMethodId = 2 }
                                )

                                PaymentOption(
                                    title = "Tarjeta",
                                    selected = selectedPaymentMethodId == 3,
                                    onClick = { selectedPaymentMethodId = 3 }
                                )
                            }
                        }
                    }

                    item {
                        OutlinedTextField(
                            value = observaciones,
                            onValueChange = { observaciones = it },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("Observaciones") },
                            placeholder = { Text("Ej: sin cebolla, llamar al llegar, etc.") }
                        )
                    }

                    item {
                        Button(
                            onClick = { confirmOrder() },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = !isSubmitting && addresses.isNotEmpty()
                        ) {
                            Text(if (isSubmitting) "Procesando..." else "Confirmar pedido")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PaymentOption(
    title: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier.padding(top = 8.dp)
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick
        )
        Text(
            text = title,
            modifier = Modifier.padding(top = 12.dp)
        )
    }
}