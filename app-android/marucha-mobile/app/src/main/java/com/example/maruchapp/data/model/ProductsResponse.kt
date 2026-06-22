package com.example.maruchapp.data.model

data class ProductsResponse(
    val success: Boolean,
    val message: String,
    val data: List<ProductDto>
)