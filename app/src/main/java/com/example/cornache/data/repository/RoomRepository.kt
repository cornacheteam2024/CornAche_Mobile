package com.example.cornache.data.repository

import android.util.Log
import androidx.lifecycle.liveData
import com.example.cornache.data.LoginPreference
import com.example.cornache.data.ResultState
import com.example.cornache.data.UserModel
import com.example.cornache.data.api.response.ErrorResponse
import com.example.cornache.data.api.retrofit.ApiService
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File

class RoomRepository private constructor(
    private val apiService: ApiService,
    private val pref: LoginPreference
) {
    fun createRoom(name:String, imageFile: File,description:String) = liveData {
        emit(ResultState.Loading)
        val userId = runBlocking { pref.getSession().first().userId }
        val userIdRequestBody = userId.toRequestBody("text/plain".toMediaType())
        val nameRequestBody = name.toRequestBody("text/plain".toMediaType())
        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "room_image",
            imageFile.name,
            requestImageFile
        )
        val descriptionRequestBody = description.toRequestBody("text/plain".toMediaType())
        try {
            val successResponse = apiService.createRoom(userIdRequestBody,nameRequestBody,multipartBody,descriptionRequestBody)
            emit(ResultState.Success(successResponse))
        }catch (e:HttpException){
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
            emit(ResultState.Error(errorResponse.message.toString()))
        }
    }

    fun updateRoom(roomId:String, name:String, imageFile: File,description:String) = liveData {
        emit(ResultState.Loading)
        val userId = runBlocking { pref.getSession().first().userId }
        val userIdRequestBody = userId.toRequestBody("text/plain".toMediaType())
        val nameRequestBody = name.toRequestBody("text/plain".toMediaType())
        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "room_image",
            imageFile.name,
            requestImageFile
        )
        val descriptionRequestBody = description.toRequestBody("text/plain".toMediaType())
        try {
            val successResponse = apiService.updateRoom(roomId, userIdRequestBody,nameRequestBody,multipartBody,descriptionRequestBody)
            emit(ResultState.Success(successResponse))
        }catch (e:HttpException){
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
            emit(ResultState.Error(errorResponse.message.toString()))
        }
    }
    fun deleteRoom(roomId: String) = liveData {
        emit(ResultState.Loading)
        try {
            val successResponse = apiService.deleteRoom(roomId)
            emit(ResultState.Success(successResponse))
        }catch (e:HttpException){
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
            emit(ResultState.Error(errorResponse.message.toString()))
        }
    }
    fun postComment(roomId: String,content:String) = liveData {
        emit(ResultState.Loading)
        val userId = runBlocking { pref.getSession().first().userId }
        try {
            val successResponse = apiService.postComment(roomId, content, userId)
            emit(ResultState.Success(successResponse))
        }catch (e:HttpException){
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
            emit(ResultState.Error(errorResponse.message.toString()))
        }
    }
    companion object {
        @Volatile
        private var instance: RoomRepository? = null

        fun getInstance(apiService: ApiService, pref: LoginPreference): RoomRepository = instance ?: synchronized(this) {
            instance ?: RoomRepository(apiService, pref).also { instance = it }
        }
    }
}