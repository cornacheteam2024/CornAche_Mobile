package com.example.cornache.di

import android.content.Context
import com.example.cornache.data.api.retrofit.ApiConfig
import com.example.cornache.data.api.retrofit.PredictApiConfig
import com.example.cornache.data.repository.HistoryRepository
import com.example.cornache.data.repository.UserRepository

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val apiService = PredictApiConfig.getApiService()
        return UserRepository.getInstance(apiService)
    }
    fun historyRepository(context: Context) : HistoryRepository{
        val apiService = ApiConfig.getApiService()
        return HistoryRepository.getInstance(apiService)
    }
    fun provideApiService() = ApiConfig.getApiService()
}