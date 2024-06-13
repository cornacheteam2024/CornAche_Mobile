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
import retrofit2.HttpException

class LoginRepository private constructor(
    private val apiService: ApiService,
    private val pref: LoginPreference
) {

    fun getSession():Flow<UserModel>{
        return pref.getSession()
    }

    fun login(username: String, password: String): LiveData<ResultState<LoginResult>> = liveData {
        emit(ResultState.Loading)
        try {
            val loginResponse = apiService.login(username, password)
            if (loginResponse.error == false) {
                val user = loginResponse.user?.let {
                    LoginResult(
                        userId = it.userId,
                        username = it.username
                    )
                }
                emit(ResultState.Success(user!!))
            } else {
                emit(ResultState.Error(loginResponse.message ?: "There was an error"))
            }
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






