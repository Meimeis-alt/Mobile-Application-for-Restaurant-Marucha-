package com.example.maruchapp.ui.screens.splash

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.maruchapp.data.local.SessionManager
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToMain: () -> Unit
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        delay(1000)
        val sessionManager = SessionManager(context)
        sessionManager.clearSession()
        onNavigateToLogin()
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Marucha Mobile",
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = "Bienvenido",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}