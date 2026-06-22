package com.example.maruchapp.data.model

data class AddressDto(
    val id_direccion: Int,
    val id_usuario: Int,
    val alias: String?,
    val direccion_texto: String,
    val referencia: String?,
    val latitud: Double?,
    val longitud: Double?,
    val es_principal: Int,
    val fecha_registro: String?
)