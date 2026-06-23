package com.example.maruchapp.data.model

data class RegisterRequest(
    val username: String,
    val nombre: String,
    val apellido: String,
    val email: String,
    val password: String,
    val telefono: String? = null
)