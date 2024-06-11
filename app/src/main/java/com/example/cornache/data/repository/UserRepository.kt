package com.example.cornache.data.repository

import androidx.lifecycle.LiveData
import com.example.cornache.data.api.PredictApiService
import java.io.File
import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.cornache.data.HistoryPagingSource
import com.example.cornache.data.ResultState
import com.example.cornache.data.api.ErrorResponse
import com.example.cornache.data.api.Prediction
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException

class UserRepository private constructor(
    private val predictApiService: PredictApiService
){
    fun analyzeImage(imageFile: File, userId:String) = liveData {
        emit(ResultState.Loading)
        val requestBody = userId.toRequestBody("text/plain".toMediaType())
        val requestImageFile = imageFile.asRequestBody("image/jpg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "image_predict",
            imageFile.name,
            requestImageFile
        )
        try {
            val successResponse = predictApiService.uploadImage(multipartBody,requestBody)
            emit(ResultState.Success(successResponse))
        }catch (e:HttpException){
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody,ErrorResponse::class.java)
            emit(ResultState.Error(errorResponse.message.toString()))
        }
    }
    companion object{
        fun getInstance(
            predictApiService: PredictApiService
        ) = UserRepository(predictApiService)
    }
}