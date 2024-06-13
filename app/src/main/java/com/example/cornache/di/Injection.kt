package com.example.cornache.di

import android.content.Context
import com.example.cornache.data.LoginPreference
import com.example.cornache.data.api.retrofit.ApiConfig
import com.example.cornache.data.api.retrofit.HistoryApiConfig
import com.example.cornache.data.api.retrofit.PredictApiConfig
import com.example.cornache.data.dataStore
import com.example.cornache.data.repository.HistoryRepository
import com.example.cornache.data.repository.UserRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val apiService = PredictApiConfig.getApiService()
        return UserRepository.getInstance(apiService)
    }
    fun historyRepository(context: Context) : HistoryRepository{
        val preference = LoginPreference.getInstance(context.dataStore)
        val token = runBlocking { preference.getToken().first() }
        val apiService = HistoryApiConfig.getApiService(token.toString())
        return HistoryRepository.getInstance(apiService,preference)
    }
    fun provideApiService() = ApiConfig.getApiService()
}