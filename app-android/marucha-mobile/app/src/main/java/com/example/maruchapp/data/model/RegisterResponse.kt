package com.example.maruchapp.data.model

data class RegisterResponse(
    val success: Boolean,
    val message: String,
    val data: RegisterUserData? = null
)

data class RegisterUserData(
    val id_usuario: Int,
    val username: String,
    val nombre: String,
    val apellido: String,
    val email: String,
    val telefono: String?
)