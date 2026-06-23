package com.example.maruchapp.data.model

data class AddressRequest(
    val alias: String,
    val direccion_texto: String,
    val referencia: String? = null,
    val es_principal: Int
)