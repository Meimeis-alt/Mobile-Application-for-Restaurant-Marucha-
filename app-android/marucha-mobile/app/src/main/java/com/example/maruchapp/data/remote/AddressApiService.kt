package com.example.maruchapp.data.remote

import com.example.maruchapp.data.model.AddressRequest
import com.example.maruchapp.data.model.AddressResponse
import com.example.maruchapp.data.model.ApiResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface AddressApiService {

    @GET("api/users/{userId}/addresses")
    suspend fun getUserAddresses(
        @Path("userId") userId: Int
    ): Response<AddressResponse>

    @POST("api/users/{userId}/addresses")
    suspend fun createAddress(
        @Path("userId") userId: Int,
        @Body request: AddressRequest
    ): Response<ApiResponse>

    @PUT("api/addresses/{addressId}")
    suspend fun updateAddress(
        @Path("addressId") addressId: Int,
        @Body request: AddressRequest
    ): Response<ApiResponse>

    @DELETE("api/addresses/{addressId}")
    suspend fun deleteAddress(
        @Path("addressId") addressId: Int
    ): Response<ApiResponse>
}