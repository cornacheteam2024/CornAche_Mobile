package com.example.cornache.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.cornache.data.HistoryPagingSource
import com.example.cornache.data.api.ApiService
import com.example.cornache.data.api.PredictApiService
import com.example.cornache.data.api.Prediction

class HistoryRepository private constructor(
    private val apiService: ApiService
){
    fun getHistory(): LiveData<PagingData<Prediction>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                HistoryPagingSource(apiService)
            }
        ).liveData
    }
    companion object{
        fun getInstance(
            apiService: ApiService
        ) = HistoryRepository(apiService)
    }
}