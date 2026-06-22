package com.example.maruchapp.data.remote

import com.example.maruchapp.data.model.HealthResponse
import com.example.maruchapp.data.model.LoginRequest
import com.example.maruchapp.data.model.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthApiService {

    @GET("api/health")
    suspend fun health(): Response<HealthResponse>

    @POST("api/auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>
}