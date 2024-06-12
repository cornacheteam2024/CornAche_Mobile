package com.example.cornache.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.cornache.data.HistoryPagingSource
import com.example.cornache.data.LoginPreference
import com.example.cornache.data.UserModel
import com.example.cornache.data.api.retrofit.ApiService
import com.example.cornache.data.api.response.Prediction
import com.example.cornache.data.api.retrofit.HistoryApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class HistoryRepository private constructor(
    private val apiService: HistoryApiService,
    private val preference: LoginPreference
){
    fun getSession():Flow<UserModel>{
        return preference.getSession()
    }
    fun getHistory(): LiveData<PagingData<Prediction>> {
        val userId = runBlocking {
            getSession().first().userId
        }
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                HistoryPagingSource(apiService, userId)
            }
        ).liveData
    }
    companion object{
        fun getInstance(
            apiService: HistoryApiService,
            preference: LoginPreference
        ) = HistoryRepository(apiService, preference)
    }
}