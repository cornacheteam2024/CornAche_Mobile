package com.example.cornache.data.api.retrofit

import com.example.cornache.data.api.response.ErrorResponse
import com.example.cornache.data.api.response.LoginResponse
import com.example.cornache.data.api.response.Prediction
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("history/{user_id}")
    suspend fun getHistory(
        @Path("user_id") userId:String,
        @Query("page") page:Int
    ):List<Prediction>

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): LoginResponse

    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("username") name: String,
        @Field("password") password: String
    ): ErrorResponse
}