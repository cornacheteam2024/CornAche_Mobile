package com.example.cornache.data.api

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @Multipart
    @POST("predict")
    suspend fun uploadImage(
        @Part file:MultipartBody.Part,
        @Part("user_id") userId:RequestBody
    ):FileUploadResponse
}