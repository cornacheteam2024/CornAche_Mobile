package com.example.cornache.di

import android.content.Context
import com.example.cornache.data.api.PredictApiConfig
import com.example.cornache.data.repository.UserRepository
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val apiService = PredictApiConfig.getApiService()
        return UserRepository.getInstance(apiService)
    }
}