package com.example.cornache.data.repository

import androidx.lifecycle.liveData
import com.example.cornache.data.LoginPreference
import com.example.cornache.data.ResultState
import com.example.cornache.data.UserModel
import com.example.cornache.data.api.response.ErrorResponse
import com.example.cornache.data.api.retrofit.HistoryApiService
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import retrofit2.HttpException

class HistoryRepository private constructor(
    private val apiService: HistoryApiService,
    private val preference: LoginPreference
){
    fun getSession():Flow<UserModel>{
        return preference.getSession()
    }
//    fun getHistory(): LiveData<PagingData<Prediction>> {
//        val userId = runBlocking {
//            getSession().first().userId
//        }
//        return Pager(
//            config = PagingConfig(
//                pageSize = 5
//            ),
//            pagingSourceFactory = {
//                HistoryPagingSource(apiService, userId)
//            }
//        ).liveData
//    }

    fun getHistory() = liveData {
        emit(ResultState.Loading)
        val userId = runBlocking {
            getSession().first().userId
        }
        try {
            val successResponse = apiService.getHistory(userId,1)
            emit(ResultState.Success(successResponse))
        }catch (e:HttpException){
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
            emit(ResultState.Error(errorResponse.message.toString()))
        }
    }
    companion object{
        fun getInstance(
            apiService: HistoryApiService,
            preference: LoginPreference
        ) = HistoryRepository(apiService, preference)
    }
}