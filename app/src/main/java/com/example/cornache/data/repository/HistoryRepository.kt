package com.example.cornache.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.cornache.data.CommentPagingSource
import com.example.cornache.data.HistoryPagingSource
import com.example.cornache.data.LoginPreference
import com.example.cornache.data.ResultState
import com.example.cornache.data.UserModel
import com.example.cornache.data.api.response.ChatsItem
import com.example.cornache.data.api.response.ErrorResponse
import com.example.cornache.data.api.response.HistoryItem
import com.example.cornache.data.api.retrofit.GETApiService
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import retrofit2.HttpException

class HistoryRepository private constructor(
    private val apiService: GETApiService,
    private val preference: LoginPreference
){
    fun getSession():Flow<UserModel>{
        return preference.getSession()
    }
    fun getHistory(): LiveData<PagingData<HistoryItem>> {
        val userId = runBlocking {
            getSession().first().userId
        }
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                HistoryPagingSource(apiService,userId)
            }
        ).liveData
    }

    fun getComments(roomId:String) : LiveData<PagingData<ChatsItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                CommentPagingSource(apiService,roomId)
            }
        ).liveData
    }

    fun getDetailUser(userId: String) = liveData {
        emit(ResultState.Loading)
        try {
            val successResponse = apiService.getDetailUser(userId)
            emit(ResultState.Success(successResponse))
        }catch (e: HttpException){
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
            emit(ResultState.Error(errorResponse.message.toString()))
        }
    }

    fun getDetailRoom(roomId: String) = liveData {
        emit(ResultState.Loading)
        try {
            val successResponse = apiService.getDetailRoom(roomId)
            emit(ResultState.Success(successResponse))
        }catch (e:HttpException){
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody,ErrorResponse::class.java)
            emit(ResultState.Error(errorResponse.message.toString()))
        }
    }

    fun getListRoom() = liveData {
        emit(ResultState.Loading)
        try {
            val successResponse = apiService.getRoom()
            val roomList = successResponse.data
            emit(ResultState.Success(roomList))
        }catch (e:HttpException){
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
            emit(ResultState.Error(errorResponse.message.toString()))
        }
    }
    companion object{
        fun getInstance(
            apiService: GETApiService,
            preference: LoginPreference
        ) = HistoryRepository(apiService, preference)
    }
}