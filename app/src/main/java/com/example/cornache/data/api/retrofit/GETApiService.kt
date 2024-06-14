package com.example.cornache.data.api.retrofit

import com.example.cornache.data.api.response.DetailUserResponse
import com.example.cornache.data.api.response.Response
import com.example.cornache.data.api.response.RoomDetailResponse
import com.example.cornache.data.api.response.RoomListResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GETApiService {
    @GET("history/{user_id}")
    suspend fun getHistory(
        @Path("user_id") userId:String,
        @Query("page") page:Int
    ):Response

    @GET("profile/{user_id}")
    suspend fun getDetailUser(
        @Path("user_id") userId: String
    ):DetailUserResponse

    @GET("room")
    suspend fun getRoom():RoomListResponse

    @GET("room/{user_id}")
    suspend fun getDetailRoom(
        @Path("user_id") userId: String
    ):RoomDetailResponse
}