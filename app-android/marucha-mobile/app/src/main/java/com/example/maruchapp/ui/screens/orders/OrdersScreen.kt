package com.example.maruchapp.ui.screens.orders

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.maruchapp.data.model.OrderSummaryDto
import com.example.maruchapp.data.remote.RetrofitClient

@Composable
fun OrdersScreen(
    onOrderClick: (Int) -> Unit = {}
) {
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var orders by remember { mutableStateOf<List<OrderSummaryDto>>(emptyList()) }

    LaunchedEffect(Unit) {
        try {
            val response = RetrofitClient.orderApiService.getUserOrders(userId = 1)

            if (response.isSuccessful && response.body()?.success == true) {
                orders = response.body()?.data ?: emptyList()
                errorMessage = null
            } else {
                errorMessage = response.body()?.message ?: "No se pudieron obtener los pedidos"
            }
        } catch (e: Exception) {
            errorMessage = "Error: ${e.message}"
        } finally {
            isLoading = false
        }
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

        orders.isEmpty() -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Aún no tienes pedidos",
                    style = MaterialTheme.typography.headlineSmall
                )
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
                        text = "Mis pedidos",
                        style = MaterialTheme.typography.headlineMedium
                    )
                }

                items(orders) { order ->
                    OrderCard(
                        order = order,
                        onClick = { onOrderClick(order.id_pedido) }
                    )
                }
            }
        }
    }
}

@Composable
private fun OrderCard(
    order: OrderSummaryDto,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        onClick = onClick
    )  {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = order.numero_pedido,
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = "Estado: ${order.estado_nombre}",
                modifier = Modifier.padding(top = 8.dp)
            )

            Text(
                text = "Total: S/ ${order.total}",
                modifier = Modifier.padding(top = 4.dp)
            )

            Text(
                text = "Pago: ${order.metodo_pago_nombre}",
                modifier = Modifier.padding(top = 4.dp)
            )

            if (!order.observaciones.isNullOrBlank()) {
                Text(
                    text = "Obs: ${order.observaciones}",
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            if (!order.fecha_pedido.isNullOrBlank()) {
                Text(
                    text = "Fecha: ${order.fecha_pedido}",
                    modifier = Modifier.padding(top = 8.dp),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}