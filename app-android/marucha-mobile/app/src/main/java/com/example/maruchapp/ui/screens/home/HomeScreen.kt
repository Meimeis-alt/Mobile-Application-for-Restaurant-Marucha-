package com.example.maruchapp.ui.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.maruchapp.data.model.ProductDto
import com.example.maruchapp.data.remote.RetrofitClient
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    onProductClick: (ProductDto) -> Unit,
    onGoToCart: () -> Unit
) {
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var products by remember { mutableStateOf<List<ProductDto>>(emptyList()) }
    var selectedCategory by remember { mutableStateOf<String?>(null) }

    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        scope.launch {
            try {
                val response = RetrofitClient.productApiService.getProducts()

                if (response.isSuccessful && response.body()?.success == true) {
                    products = response.body()?.data ?: emptyList()
                    errorMessage = null
                } else {
                    errorMessage = response.body()?.message ?: "Error al obtener productos"
                }
            } catch (e: Exception) {
                errorMessage = "ERROR: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    val groupedProducts = remember(products) {
        products.groupBy { it.categoria_nombre.ifBlank { "Otros" } }
    }

    val categoryOrder = listOf(
        "Entradas",
        "Segundos",
        "Bebidas",
        "Postres"
    )

    val orderedCategories = remember(groupedProducts) {
        val existingCategories = groupedProducts.keys.toList()

        val ordered = categoryOrder.filter { groupedProducts.containsKey(it) }
        val remaining = existingCategories.filterNot { it in categoryOrder }.sorted()

        ordered + remaining
    }

    LaunchedEffect(orderedCategories) {
        if (selectedCategory == null && orderedCategories.isNotEmpty()) {
            selectedCategory = if (orderedCategories.contains("Entradas")) {
                "Entradas"
            } else {
                orderedCategories.first()
            }
        }
    }

    val visibleProducts = remember(groupedProducts, selectedCategory) {
        groupedProducts[selectedCategory].orEmpty()
    }

    when {
        isLoading -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.padding(24.dp)
                )
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

        products.isEmpty() -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "No hay productos disponibles",
                    style = MaterialTheme.typography.bodyLarge
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
                    Column {
                        Text(
                            text = "Carta Marucha",
                            style = MaterialTheme.typography.headlineMedium,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            orderedCategories.forEach { category ->
                                val isSelected = selectedCategory == category

                                if (isSelected) {
                                    Button(
                                        onClick = { selectedCategory = category }
                                    ) {
                                        Text(category)
                                    }
                                } else {
                                    OutlinedButton(
                                        onClick = { selectedCategory = category }
                                    ) {
                                        Text(category)
                                    }
                                }
                            }
                        }
                    }
                }

                item {
                    Text(
                        text = selectedCategory ?: "Productos",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                    )
                }

                if (visibleProducts.isEmpty()) {
                    item {
                        Text(
                            text = "No hay productos en esta categoría",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                } else {
                    items(visibleProducts) { product ->
                        ProductCard(
                            product = product,
                            onClick = { onProductClick(product) }
                        )
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(8.dp))
                }

                item {
                    Button(
                        onClick = onGoToCart,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Ver carrito")
                    }
                }
            }
        }
    }
}

@Composable
private fun ProductCard(
    product: ProductDto,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = product.nombre,
                style = MaterialTheme.typography.titleMedium
            )

            if (!product.descripcion.isNullOrBlank()) {
                Text(
                    text = product.descripcion,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Text(
                text = "S/ ${product.precio}",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(top = 12.dp)
            )
        }
    }
}