package com.example.maruchapp.data.model

data class OrderResponse(
    val success: Boolean,
    val message: String,
    val data: OrderResponseData? = null
)

data class OrderResponseData(
    val order: OrderDto,
    val details: List<OrderDetailDto>
)

data class OrderDto(
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

data class OrderDetailDto(
    val id_detalle_pedido: Int,
    val id_pedido: Int,
    val id_platillo: Int,
    val platillo_nombre: String,
    val cantidad: Int,
    val precio_unitario: String,
    val subtotal: String
)