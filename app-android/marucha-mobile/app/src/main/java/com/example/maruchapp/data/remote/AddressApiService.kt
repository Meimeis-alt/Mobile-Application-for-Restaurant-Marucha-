package com.example.maruchapp.data.remote

import com.example.maruchapp.data.model.AddressResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface AddressApiService {

    @GET("api/users/{userId}/addresses")
    suspend fun getUserAddresses(
        @Path("userId") userId: Int
    ): Response<AddressResponse>
}