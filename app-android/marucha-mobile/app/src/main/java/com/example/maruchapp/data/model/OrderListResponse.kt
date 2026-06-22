package com.example.maruchapp.data.model

data class OrderListResponse(
    val success: Boolean,
    val message: String,
    val data: List<OrderSummaryDto>
)

data class OrderSummaryDto(
    val id_pedido: Int,
    val id_usuario: Int,
    val id_direccion: Int,
    val id_estado_pedido: Int,
    val estado_nombre: String,
    val id_metodo_pago: Int,
    val metodo_pago_nombre: String,
    val numero_pedido: String,
    val subtotal: String,
    val total: String,
    val observaciones: String?,
    val fecha_pedido: String?,
    val fecha_actualizacion: String?
)