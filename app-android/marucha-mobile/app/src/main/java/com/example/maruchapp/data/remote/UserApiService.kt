package com.example.maruchapp.data.remote

import com.example.maruchapp.data.model.UserResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface UserApiService {

    @GET("api/users/{userId}")
    suspend fun getUserById(
        @Path("userId") userId: Int
    ): Response<UserResponse>
}