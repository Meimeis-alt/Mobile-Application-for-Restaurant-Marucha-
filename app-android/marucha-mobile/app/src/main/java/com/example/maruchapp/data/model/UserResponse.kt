package com.example.maruchapp.data.model

import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String,

    @SerializedName("data")
    val data: UserDto
)