package com.example.maruchapp.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.maruchapp.data.local.SessionManager
import com.example.maruchapp.data.model.AddressDto
import com.example.maruchapp.data.model.AddressRequest
import com.example.maruchapp.data.remote.RetrofitClient
import kotlinx.coroutines.launch

@Composable
fun AddressesScreen(
    onBackClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    val userId = sessionManager.getUserId()

    val scope = rememberCoroutineScope()

    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val addresses = remember { mutableStateListOf<AddressDto>() }

    var showFormDialog by remember { mutableStateOf(false) }
    var editingAddress by remember { mutableStateOf<AddressDto?>(null) }
    var addressToDelete by remember { mutableStateOf<AddressDto?>(null) }

    fun loadAddresses() {
        scope.launch {
            isLoading = true
            try {
                val response = RetrofitClient.addressApiService.getUserAddresses(userId)

                if (response.isSuccessful && response.body()?.success == true) {
                    addresses.clear()
                    addresses.addAll(response.body()?.data ?: emptyList())
                    errorMessage = null
                } else {
                    errorMessage =
                        response.body()?.message ?: "No se pudieron obtener las direcciones"
                }
            } catch (e: Exception) {
                errorMessage = "Error: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    LaunchedEffect(userId) {
        loadAddresses()
    }

    if (showFormDialog) {
        AddressFormDialog(
            initialAddress = editingAddress,
            onDismiss = {
                showFormDialog = false
                editingAddress = null
            },
            onSave = { request ->
                scope.launch {
                    try {
                        if (editingAddress == null) {
                            val response = RetrofitClient.addressApiService.createAddress(
                                userId = userId,
                                request = request
                            )

                            if (!response.isSuccessful || response.body()?.success != true) {
                                errorMessage =
                                    response.body()?.message ?: "No se pudo registrar la dirección"
                                return@launch
                            }
                        } else {
                            val response = RetrofitClient.addressApiService.updateAddress(
                                addressId = editingAddress!!.idDireccion,
                                request = request
                            )

                            if (!response.isSuccessful || response.body()?.success != true) {
                                errorMessage =
                                    response.body()?.message ?: "No se pudo actualizar la dirección"
                                return@launch
                            }
                        }

                        showFormDialog = false
                        editingAddress = null
                        loadAddresses()
                    } catch (e: Exception) {
                        errorMessage = "Error: ${e.message}"
                    }
                }
            }
        )
    }

    if (addressToDelete != null) {
        AlertDialog(
            onDismissRequest = { addressToDelete = null },
            confirmButton = {
                TextButton(
                    onClick = {
                        val address = addressToDelete ?: return@TextButton
                        scope.launch {
                            try {
                                val response = RetrofitClient.addressApiService.deleteAddress(address.idDireccion)

                                if (response.isSuccessful && response.body()?.success == true) {
                                    addressToDelete = null
                                    loadAddresses()
                                } else {
                                    errorMessage =
                                        response.body()?.message ?: "No se pudo eliminar la dirección"
                                }
                            } catch (e: Exception) {
                                errorMessage = "Error: ${e.message}"
                            }
                        }
                    }
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(onClick = { addressToDelete = null }) {
                    Text("Cancelar")
                }
            },
            title = {
                Text("Eliminar dirección")
            },
            text = {
                Text("¿Seguro que deseas eliminar esta dirección?")
            }
        )
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

        errorMessage != null && addresses.isEmpty() -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center
            ) {
                TextButton(onClick = onBackClick) {
                    Text("← Volver")
                }

                Text(
                    text = errorMessage ?: "Error desconocido",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        else -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                TextButton(onClick = onBackClick) {
                    Text("← Volver")
                }

                Text(
                    text = "Mis Direcciones",
                    style = MaterialTheme.typography.headlineMedium
                )

                Text(
                    text = "Gestiona tus direcciones de entrega",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp)
                )

                if (!errorMessage.isNullOrBlank()) {
                    Text(
                        text = errorMessage!!,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                Button(
                    onClick = {
                        editingAddress = null
                        showFormDialog = true
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    Text("Agregar dirección")
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (addresses.isEmpty()) {
                    Text(
                        text = "Aún no tienes direcciones registradas.",
                        style = MaterialTheme.typography.bodyLarge
                    )
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(addresses) { address ->
                            AddressCard(
                                address = address,
                                onEdit = {
                                    editingAddress = address
                                    showFormDialog = true
                                },
                                onDelete = {
                                    addressToDelete = address
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AddressCard(
    address: AddressDto,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = address.alias ?: "Sin alias",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )

                if (address.esPrincipal == 1) {
                    Box(
                        modifier = Modifier
                            .background(
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                                RoundedCornerShape(50)
                            )
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "Principal",
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }
            }

            Text(
                text = address.direccionTexto,
                modifier = Modifier.padding(top = 8.dp)
            )

            if (!address.referencia.isNullOrBlank()) {
                Text(
                    text = "Referencia: ${address.referencia}",
                    modifier = Modifier.padding(top = 4.dp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                OutlinedButton(onClick = onEdit) {
                    Text("Editar")
                }

                Spacer(modifier = Modifier.width(8.dp))

                TextButton(onClick = onDelete) {
                    Text(
                        text = "Eliminar",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@Composable
private fun AddressFormDialog(
    initialAddress: AddressDto?,
    onDismiss: () -> Unit,
    onSave: (AddressRequest) -> Unit
) {
    var alias by remember { mutableStateOf(initialAddress?.alias ?: "") }
    var direccion by remember { mutableStateOf(initialAddress?.direccionTexto ?: "") }
    var referencia by remember { mutableStateOf(initialAddress?.referencia ?: "") }
    var esPrincipal by remember { mutableStateOf(initialAddress?.esPrincipal == 1) }

    var validationError by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    if (alias.isBlank() || direccion.isBlank()) {
                        validationError = "Alias y dirección son obligatorios"
                        return@TextButton
                    }

                    validationError = null

                    onSave(
                        AddressRequest(
                            alias = alias.trim(),
                            direccion_texto = direccion.trim(),
                            referencia = referencia.trim().ifBlank { null },
                            es_principal = if (esPrincipal) 1 else 0
                        )
                    )
                }
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        },
        title = {
            Text(if (initialAddress == null) "Nueva dirección" else "Editar dirección")
        },
        text = {
            Column {
                OutlinedTextField(
                    value = alias,
                    onValueChange = { alias = it },
                    label = { Text("Alias") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = direccion,
                    onValueChange = { direccion = it },
                    label = { Text("Dirección") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                )

                OutlinedTextField(
                    value = referencia,
                    onValueChange = { referencia = it },
                    label = { Text("Referencia") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Marcar como principal",
                        modifier = Modifier.weight(1f)
                    )

                    Switch(
                        checked = esPrincipal,
                        onCheckedChange = { esPrincipal = it }
                    )
                }

                if (!validationError.isNullOrBlank()) {
                    Text(
                        text = validationError!!,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(top = 12.dp)
                    )
                }
            }
        }
    )
}