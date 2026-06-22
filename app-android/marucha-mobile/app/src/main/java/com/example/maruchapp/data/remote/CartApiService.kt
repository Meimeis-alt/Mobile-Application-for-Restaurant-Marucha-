package com.example.maruchapp.data.remote

import com.example.maruchapp.data.model.CartAddRequest
import com.example.maruchapp.data.model.CartResponse
import com.example.maruchapp.data.model.CartUpdateRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface CartApiService {

    @GET("api/users/{userId}/cart")
    suspend fun getActiveCart(
        @Path("userId") userId: Int
    ): Response<CartResponse>

    @POST("api/users/{userId}/cart/items")
    suspend fun addItemToCart(
        @Path("userId") userId: Int,
        @Body request: CartAddRequest
    ): Response<CartResponse>

    @PUT("api/cart/items/{cartItemId}")
    suspend fun updateCartItem(
        @Path("cartItemId") cartItemId: Int,
        @Body request: CartUpdateRequest
    ): Response<Map<String, Any>>

    @DELETE("api/cart/items/{cartItemId}")
    suspend fun deleteCartItem(
        @Path("cartItemId") cartItemId: Int
    ): Response<Map<String, Any>>
}