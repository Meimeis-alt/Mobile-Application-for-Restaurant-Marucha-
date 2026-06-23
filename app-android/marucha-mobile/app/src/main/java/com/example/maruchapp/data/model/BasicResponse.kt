package com.example.maruchapp.data.model

import com.google.gson.annotations.SerializedName

data class BasicResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String
)