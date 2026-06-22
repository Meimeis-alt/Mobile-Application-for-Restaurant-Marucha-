package com.example.maruchapp.data.model

data class CartResponse(
    val success: Boolean,
    val message: String,
    val data: CartData
)

data class CartData(
    val cart: CartInfo,
    val items: List<CartItemDto>,
    val total: Double
)

data class CartInfo(
    val id_carrito: Int,
    val id_usuario: Int,
    val estado: String,
    val fecha_creacion: String?,
    val fecha_actualizacion: String?
)

data class CartItemDto(
    val id_carrito_detalle: Int,
    val id_carrito: Int,
    val id_platillo: Int,
    val platillo_nombre: String,
    val platillo_descripcion: String?,
    val imagen_url: String?,
    val cantidad: Int,
    val precio_unitario: String,
    val subtotal: String
)