package com.example.maruchapp.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(
    onGoToProfile: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Home - Marucha",
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = "Aquí irá el catálogo de productos.",
            modifier = Modifier.padding(top = 12.dp)
        )

        Button(
            onClick = onGoToProfile,
            modifier = Modifier.padding(top = 20.dp)
        ) {
            Text("Ir a perfil")
        }
    }
}