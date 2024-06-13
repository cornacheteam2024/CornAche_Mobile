package com.example.cornache.data.api.retrofit

import com.example.cornache.data.api.response.Prediction
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface HistoryApiService {
    @GET("history/{user_id}")
    suspend fun getHistory(
        @Path("user_id") userId:String,
        @Query("page") page:Int
    ):List<Prediction>
}