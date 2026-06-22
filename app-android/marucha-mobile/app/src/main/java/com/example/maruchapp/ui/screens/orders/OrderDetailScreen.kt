package com.example.maruchapp.ui.screens.orders

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.maruchapp.data.model.OrderDetailData
import com.example.maruchapp.data.remote.RetrofitClient

@Composable
fun OrderDetailScreen(
    orderId: Int,
    onBack: () -> Unit = {}
) {
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var orderDetail by remember { mutableStateOf<OrderDetailData?>(null) }

    LaunchedEffect(orderId) {
        try {
            val response = RetrofitClient.orderApiService.getOrderById(orderId)

            if (response.isSuccessful && response.body()?.success == true) {
                orderDetail = response.body()?.data
                errorMessage = null
            } else {
                errorMessage = response.body()?.message ?: "No se pudo obtener el pedido"
            }
        } catch (e: Exception) {
            errorMessage = "Error: ${e.message}"
        } finally {
            isLoading = false
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
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

            orderDetail == null -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("No se encontró información del pedido")
                }
            }

            else -> {
                val detail = orderDetail!!

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        Text(
                            text = "Detalle del pedido",
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
                                    text = detail.order.numero_pedido,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    text = "Estado: ${detail.order.estado_nombre}",
                                    modifier = Modifier.padding(top = 8.dp)
                                )
                                Text(
                                    text = "Pago: ${detail.order.metodo_pago_nombre}",
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                                Text(
                                    text = "Subtotal: S/ ${detail.order.subtotal}",
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                                Text(
                                    text = "Total: S/ ${detail.order.total}",
                                    modifier = Modifier.padding(top = 4.dp)
                                )

                                if (!detail.order.observaciones.isNullOrBlank()) {
                                    Text(
                                        text = "Observaciones: ${detail.order.observaciones}",
                                        modifier = Modifier.padding(top = 8.dp)
                                    )
                                }

                                if (!detail.order.fecha_pedido.isNullOrBlank()) {
                                    Text(
                                        text = "Fecha: ${detail.order.fecha_pedido}",
                                        modifier = Modifier.padding(top = 8.dp),
                                        style = MaterialTheme.typography.bodySmall
                                    )
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
                                    text = "Dirección de entrega",
                                    style = MaterialTheme.typography.titleMedium
                                )

                                Text(
                                    text = detail.address.alias ?: "Dirección",
                                    modifier = Modifier.padding(top = 8.dp)
                                )

                                Text(
                                    text = detail.address.direccion_texto,
                                    modifier = Modifier.padding(top = 4.dp)
                                )

                                if (!detail.address.referencia.isNullOrBlank()) {
                                    Text(
                                        text = "Referencia: ${detail.address.referencia}",
                                        modifier = Modifier.padding(top = 4.dp)
                                    )
                                }
                            }
                        }
                    }

                    item {
                        Text(
                            text = "Productos",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }

                    items(detail.details.size) { index ->
                        val item = detail.details[index]

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = item.platillo_nombre,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    text = "Cantidad: ${item.cantidad}",
                                    modifier = Modifier.padding(top = 6.dp)
                                )
                                Text(
                                    text = "Precio unitario: S/ ${item.precio_unitario}",
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                                Text(
                                    text = "Subtotal: S/ ${item.subtotal}",
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}