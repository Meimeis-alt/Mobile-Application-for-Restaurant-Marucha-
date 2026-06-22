package com.example.maruchapp.ui.screens.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.maruchapp.data.model.LoginRequest
import com.example.maruchapp.data.remote.RetrofitClient
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit
) {
    var email by remember { mutableStateOf("admin@marucha.com") }
    var password by remember { mutableStateOf("admin123") }
    var isLoading by remember { mutableStateOf(false) }
    var resultMessage by remember { mutableStateOf<String?>(null) }

    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Iniciar sesión",
            style = MaterialTheme.typography.headlineMedium
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        )

        Button(
            onClick = {
                resultMessage = null
                isLoading = true

                scope.launch {
                    try {
                        val response = RetrofitClient.authApiService.login(
                            LoginRequest(
                                email = email.trim(),
                                password = password
                            )
                        )

                        if (response.isSuccessful && response.body()?.success == true) {
                            resultMessage = "Login correcto"
                            onLoginSuccess()
                        } else {
                            resultMessage = response.body()?.message ?: "Login failed"
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
            Text("Entrar")
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
                color = if (it.startsWith("ERROR") || it.contains("failed", true))
                    MaterialTheme.colorScheme.error
                else
                    MaterialTheme.colorScheme.primary
            )
        }
    }
}