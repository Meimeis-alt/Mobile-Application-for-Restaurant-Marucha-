package com.example.maruchapp.ui.screens.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.maruchapp.data.model.RegisterRequest
import com.example.maruchapp.data.remote.RetrofitClient
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(
    onBackToLogin: () -> Unit
) {
    var username by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var isLoading by remember { mutableStateOf(false) }
    var resultMessage by remember { mutableStateOf<String?>(null) }

    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        TextButton(
            onClick = onBackToLogin
        ) {
            Text("← Volver")
        }

        Text(
            text = "Crear cuenta",
            style = MaterialTheme.typography.headlineMedium
        )

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Nombre de usuario") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            singleLine = true
        )

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            singleLine = true
        )

        OutlinedTextField(
            value = apellido,
            onValueChange = { apellido = it },
            label = { Text("Apellido") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            singleLine = true
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            singleLine = true
        )

        OutlinedTextField(
            value = telefono,
            onValueChange = { telefono = it },
            label = { Text("Teléfono (opcional)") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            singleLine = true
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            singleLine = true
        )

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirmar contraseña") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            singleLine = true
        )

        Button(
            onClick = {
                resultMessage = null

                if (
                    username.isBlank() ||
                    nombre.isBlank() ||
                    apellido.isBlank() ||
                    email.isBlank() ||
                    password.isBlank() ||
                    confirmPassword.isBlank()
                ) {
                    resultMessage = "Completa todos los campos obligatorios"
                    return@Button
                }

                if (password != confirmPassword) {
                    resultMessage = "Las contraseñas no coinciden"
                    return@Button
                }

                if (password.length < 8) {
                    resultMessage = "La contraseña debe tener al menos 8 caracteres"
                    return@Button
                }

                isLoading = true

                scope.launch {
                    try {
                        val response = RetrofitClient.authApiService.register(
                            RegisterRequest(
                                username = username.trim(),
                                nombre = nombre.trim(),
                                apellido = apellido.trim(),
                                email = email.trim(),
                                password = password,
                                telefono = telefono.trim().ifBlank { null }
                            )
                        )

                        if (response.isSuccessful && response.body()?.success == true) {
                            resultMessage = "Cuenta creada correctamente. Ahora inicia sesión."
                        } else {
                            resultMessage = response.body()?.message ?: "No se pudo registrar la cuenta"
                        }
                    } catch (e: Exception) {
                        resultMessage = "ERROR: ${e.message}"
                    } finally {
                        isLoading = false
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            enabled = !isLoading
        ) {
            Text("Crear cuenta")
        }

        OutlinedButton(
            onClick = onBackToLogin,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            enabled = !isLoading
        ) {
            Text("Volver al login")
        }

        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.padding(top = 16.dp)
            )
        }

        resultMessage?.let {
            Text(
                text = it,
                modifier = Modifier.padding(top = 16.dp),
                color = if (
                    it.startsWith("ERROR") ||
                    it.contains("no se pudo", true) ||
                    it.contains("coinciden", true) ||
                    it.contains("completa", true)
                ) {
                    MaterialTheme.colorScheme.error
                } else {
                    MaterialTheme.colorScheme.primary
                }
            )
        }
    }
}