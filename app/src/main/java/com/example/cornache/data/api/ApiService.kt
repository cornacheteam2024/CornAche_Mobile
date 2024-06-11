package com.example.cornache.data.api

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("history/{user_id}")
    suspend fun getHistory(
        @Path("user_id") userId:String,
        @Query("page") page:Int
    ):List<Prediction>
}