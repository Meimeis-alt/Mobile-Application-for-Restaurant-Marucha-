package com.example.maruchapp.data.model

data class UserDto(
    val id_usuario: Int,
    val id_rol: Int,
    val username: String,
    val nombre: String,
    val apellido: String,
    val email: String,
    val telefono: String?,
    val foto_perfil: String?,
    val auth_provider: String,
    val estado: Int
)