package com.example.maruchapp.data.model

import com.google.gson.annotations.SerializedName

data class AddressListResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String,

    @SerializedName("data")
    val data: List<AddressDto> = emptyList()
)