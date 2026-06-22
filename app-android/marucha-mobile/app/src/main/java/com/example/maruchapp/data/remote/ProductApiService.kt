package com.example.maruchapp.data.remote

import com.example.maruchapp.data.model.ProductsResponse
import retrofit2.Response
import retrofit2.http.GET

interface ProductApiService {

    @GET("api/products")
    suspend fun getProducts(): Response<ProductsResponse>
}