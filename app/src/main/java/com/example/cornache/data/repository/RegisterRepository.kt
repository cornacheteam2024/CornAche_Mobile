package com.example.cornache.data.repository

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.cornache.data.LoginPreference
import com.example.cornache.data.ResultState
import com.example.cornache.data.api.response.ErrorResponse
import com.example.cornache.data.api.retrofit.ApiService
import com.google.gson.Gson
import retrofit2.HttpException

class RegisterRepository private constructor(private var ApiService: ApiService, private val pref: LoginPreference) {
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    fun register(
        name: String,
        email: String,
        password: String
    ): LiveData<ResultState<ErrorResponse>> =
        liveData {
            emit(ResultState.Loading)
            try {
                val response = ApiService.register(name, email, password)
                emit(ResultState.Success(response))
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val error = Gson().fromJson(jsonInString, ErrorResponse::class.java)
                emit(ResultState.Error(error.message.toString()))
            } catch (e: Exception) {
                emit(ResultState.Error(e.message.toString()))
            }
        }


    companion object {
        @Volatile
        private var instance: RegisterRepository? = null

        fun getInstance(apiService: ApiService, pref: LoginPreference): RegisterRepository =
            instance ?: synchronized(this) {
                instance ?: RegisterRepository(apiService, pref).also { instance = it }
            }
    }
}