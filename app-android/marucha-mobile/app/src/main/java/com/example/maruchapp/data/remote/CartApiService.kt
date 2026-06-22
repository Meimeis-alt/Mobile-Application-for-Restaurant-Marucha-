package com.example.maruchapp.data.remote

import com.example.maruchapp.data.model.CartAddRequest
import com.example.maruchapp.data.model.CartResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface CartApiService {

    @POST("api/users/{userId}/cart/items")
    suspend fun addItemToCart(
        @Path("userId") userId: Int,
        @Body request: CartAddRequest
    ): Response<CartResponse>
}