package com.example.maruchapp.ui.screens.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.ReceiptLong
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.maruchapp.data.local.SessionManager
import com.example.maruchapp.data.model.UserDto
import com.example.maruchapp.data.remote.RetrofitClient

@Composable
fun ProfileScreen(
    onBackClick: () -> Unit = {},
    onAddressesClick: () -> Unit = {},
    onPaymentMethodsClick: () -> Unit = {},
    onOrdersClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    val userId = sessionManager.getUserId()

    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var user by remember { mutableStateOf<UserDto?>(null) }

    LaunchedEffect(userId) {
        try {
            val response = RetrofitClient.userApiService.getUserById(userId)

            if (response.isSuccessful && response.body()?.success == true) {
                user = response.body()?.data
                errorMessage = null
            } else {
                errorMessage = response.body()?.message ?: "No se pudo obtener el perfil"
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
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
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

        user != null -> {
            ProfileContent(
                user = user!!,
                onBackClick = onBackClick,
                onAddressesClick = onAddressesClick,
                onPaymentMethodsClick = onPaymentMethodsClick,
                onOrdersClick = onOrdersClick,
                onSettingsClick = onSettingsClick,
                onLogoutClick = onLogoutClick
            )
        }
    }
}

@Composable
private fun ProfileContent(
    user: UserDto,
    onBackClick: () -> Unit,
    onAddressesClick: () -> Unit,
    onPaymentMethodsClick: () -> Unit,
    onOrdersClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    val fullName = listOf(user.nombre, user.apellido)
        .filter { it.isNotBlank() }
        .joinToString(" ")
        .ifBlank { user.username }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        ProfileTopBar(
            onBackClick = onBackClick,
            onSettingsClick = onSettingsClick
        )

        Spacer(modifier = Modifier.height(20.dp))

        ProfileHeaderCard(
            fullName = fullName,
            email = user.email
        )

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
        ) {
            Column(
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                ProfileOptionRow(
                    title = "Mis Direcciones",
                    icon = {
                        Icon(
                            imageVector = Icons.Outlined.LocationOn,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    },
                    onClick = onAddressesClick
                )

                Divider()

                ProfileOptionRow(
                    title = "Métodos de Pago",
                    icon = {
                        Icon(
                            imageVector = Icons.Outlined.CreditCard,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    },
                    onClick = onPaymentMethodsClick
                )

                Divider()

                ProfileOptionRow(
                    title = "Historial de Pedidos",
                    icon = {
                        Icon(
                            imageVector = Icons.Outlined.ReceiptLong,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    },
                    onClick = onOrdersClick
                )

                Divider()

                ProfileOptionRow(
                    title = "Configuración",
                    icon = {
                        Icon(
                            imageVector = Icons.Outlined.Settings,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    },
                    onClick = onSettingsClick
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedButton(
            onClick = onLogoutClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(18.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.ExitToApp,
                contentDescription = "Cerrar sesión",
                tint = MaterialTheme.colorScheme.error
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "Cerrar Sesión",
                color = MaterialTheme.colorScheme.error
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        MaruchaBannerPlaceholder()
    }
}

@Composable
private fun ProfileTopBar(
    onBackClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBackClick) {
            Icon(
                imageVector = Icons.Outlined.ArrowBack,
                contentDescription = "Volver",
                tint = MaterialTheme.colorScheme.primary
            )
        }

        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Perfil",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }

        IconButton(onClick = onSettingsClick) {
            Icon(
                imageVector = Icons.Outlined.Settings,
                contentDescription = "Configuración",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun ProfileHeaderCard(
    fullName: String,
    email: String
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            contentAlignment = Alignment.BottomEnd
        ) {
            Surface(
                modifier = Modifier.size(120.dp),
                shape = CircleShape,
                tonalElevation = 4.dp,
                color = MaterialTheme.colorScheme.surfaceVariant
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = initialsFromName(fullName),
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primary,
                shadowElevation = 4.dp
            ) {
                Box(
                    modifier = Modifier.size(38.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "✎",
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }

        Text(
            text = fullName,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(top = 16.dp)
        )

        Text(
            text = email,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
private fun ProfileOptionRow(
    title: String,
    icon: @Composable () -> Unit,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 18.dp, vertical = 18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        icon()

        Spacer(modifier = Modifier.width(14.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )

        Icon(
            imageVector = Icons.Outlined.ChevronRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun MaruchaBannerPlaceholder() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 28.dp, horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Aquí irá tu imagen / banner de Marucha",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = "Puedes reemplazar este bloque por una imagen más adelante.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

private fun initialsFromName(fullName: String): String {
    val parts = fullName.trim().split(" ").filter { it.isNotBlank() }
    return when {
        parts.isEmpty() -> "U"
        parts.size == 1 -> parts.first().take(1).uppercase()
        else -> "${parts[0].take(1)}${parts[1].take(1)}".uppercase()
    }
}