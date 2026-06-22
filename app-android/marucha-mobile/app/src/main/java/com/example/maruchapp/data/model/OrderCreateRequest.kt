package com.example.maruchapp.data.model

data class OrderCreateRequest(
    val id_direccion: Int,
    val id_metodo_pago: Int,
    val observaciones: String? = null
)