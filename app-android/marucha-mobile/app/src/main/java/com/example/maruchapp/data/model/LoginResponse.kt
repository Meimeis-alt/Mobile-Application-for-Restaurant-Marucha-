package com.example.maruchapp.data.model

data class LoginResponse(
    val success: Boolean,
    val message: String,
    val data: LoginData?
)

data class LoginData(
    val user: UserDto
)