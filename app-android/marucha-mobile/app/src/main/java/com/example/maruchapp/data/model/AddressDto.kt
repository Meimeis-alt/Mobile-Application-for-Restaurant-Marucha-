package com.example.maruchapp.data.model

import com.google.gson.annotations.SerializedName

data class AddressDto(
    @SerializedName("id_direccion")
    val idDireccion: Int,

    @SerializedName("id_usuario")
    val idUsuario: Int,

    @SerializedName("alias")
    val alias: String?,

    @SerializedName("direccion_texto")
    val direccionTexto: String,

    @SerializedName("referencia")
    val referencia: String?,

    @SerializedName("latitud")
    val latitud: Double?,

    @SerializedName("longitud")
    val longitud: Double?,

    @SerializedName("es_principal")
    val esPrincipal: Int,

    @SerializedName("fecha_registro")
    val fechaRegistro: String? = null
)