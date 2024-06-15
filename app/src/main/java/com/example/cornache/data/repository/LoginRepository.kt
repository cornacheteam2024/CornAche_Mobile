package com.example.cornache.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.cornache.data.LoginPreference
import com.example.cornache.data.ResultState
import com.example.cornache.data.UserModel
import com.example.cornache.data.api.response.ErrorResponse
import com.example.cornache.data.api.response.LoginResult
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

class LoginRepository private constructor(
    private val apiService: ApiService,
    private val pref: LoginPreference
) {

    fun getSession():Flow<UserModel>{
        return pref.getSession()
    }

    suspend fun logout(){
        pref.logout()
    }

    fun editProfile(username:String,imageFile:File) = liveData {
        emit(ResultState.Loading)
        val userId = runBlocking { getSession().first().userId }
        val requestBody = username.toRequestBody("text/plain".toMediaType())
        val requestImageFile = imageFile.asRequestBody("image/jpg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "avatar_image",
            imageFile.name,
            requestImageFile
        )
        try {
            val successResponse = apiService.updateProfile(userId,requestBody,multipartBody)
            emit(ResultState.Success(successResponse))
        }catch (e:HttpException){
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody,ErrorResponse::class.java)
            emit(ResultState.Error(errorResponse.message.toString()))
        }
    }

    fun register(username:String, password: String, confirmPass:String) = liveData {
        emit(ResultState.Loading)
        try {
            val registerResponse = apiService.register(username,password,confirmPass)
            emit(ResultState.Success(registerResponse))
        }catch (e:HttpException){
            val errorBody = e.response()?.errorBody()?.string()
            Log.e("LoginRepository", "HTTP Exception: ${e.message()}")
            Log.e("LoginRepository", "Error response body: $errorBody")
            if (e.code() == 503) {
                emit(ResultState.Error("Service is unavailable. Please try again later."))
            } else {
                try {
                    val error = Gson().fromJson(errorBody, ErrorResponse::class.java)
                    emit(ResultState.Error(error.message.toString()))
                } catch (jsonException: Exception) {
                    Log.e("LoginRepository", "JSON Parsing Error: ${jsonException.message}")
                    emit(ResultState.Error("Unexpected error occurred"))
                }
            }
        } catch (e: Exception) {
            Log.e("LoginRepository", "Exception: ${e.message}")
            emit(ResultState.Error(e.message.toString()))
        }
    }

    fun login(username: String, password: String)= liveData {
        emit(ResultState.Loading)
        try {
            val loginResponse = apiService.login(username, password)
            emit(ResultState.Success(loginResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            Log.e("LoginRepository", "HTTP Exception: ${e.message()}")
            Log.e("LoginRepository", "Error response body: $errorBody")
            if (e.code() == 503) {
                emit(ResultState.Error("Service is unavailable. Please try again later."))
            } else {
                try {
                    val error = Gson().fromJson(errorBody, ErrorResponse::class.java)
                    emit(ResultState.Error(error.message.toString()))
                } catch (jsonException: Exception) {
                    Log.e("LoginRepository", "JSON Parsing Error: ${jsonException.message}")
                    emit(ResultState.Error("Unexpected error occurred"))
                }
            }
        } catch (e: Exception) {
            Log.e("LoginRepository", "Exception: ${e.message}")
            emit(ResultState.Error(e.message.toString()))
        }
    }
    suspend fun saveSession(userModel: UserModel){
        pref.saveSession(userModel)
    }

    companion object {
        @Volatile
        private var instance: LoginRepository? = null

        fun getInstance(apiService: ApiService, pref: LoginPreference): LoginRepository = instance ?: synchronized(this) {
            instance ?: LoginRepository(apiService, pref).also { instance = it }
        }
    }
}






