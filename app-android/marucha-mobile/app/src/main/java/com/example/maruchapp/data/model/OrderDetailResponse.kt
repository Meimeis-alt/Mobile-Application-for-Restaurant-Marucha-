package com.example.maruchapp.data.model

data class OrderDetailResponse(
    val success: Boolean,
    val message: String,
    val data: OrderDetailData
)

data class OrderDetailData(
    val order: OrderDto,
    val address: AddressDto,
    val details: List<OrderDetailDto>
)