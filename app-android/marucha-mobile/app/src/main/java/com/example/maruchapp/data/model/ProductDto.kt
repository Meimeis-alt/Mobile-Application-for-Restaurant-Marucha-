package com.example.maruchapp.data.model

data class ProductDto(
    val id_platillo: Int,
    val id_categoria: Int,
    val categoria_nombre: String,
    val nombre: String,
    val descripcion: String?,
    val precio: String,
    val imagen_url: String?,
    val disponible: Int,
    val fecha_creacion: String?,
    val fecha_actualizacion: String?
)