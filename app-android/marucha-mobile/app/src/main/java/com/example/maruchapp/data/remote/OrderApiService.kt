package com.example.maruchapp.data.remote

import com.example.maruchapp.data.model.OrderCreateRequest
import com.example.maruchapp.data.model.OrderResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface OrderApiService {

    @POST("api/users/{userId}/orders")
    suspend fun createOrder(
        @Path("userId") userId: Int,
        @Body request: OrderCreateRequest
    ): Response<OrderResponse>
}