package com.example.maruchapp.data.model

data class AddressResponse(
    val success: Boolean,
    val message: String,
    val data: List<AddressDto>
)