package com.example.cornache.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.cornache.data.LoginPreference
import com.example.cornache.data.api.retrofit.ApiConfig
import com.example.cornache.data.repository.LoginRepository
import com.example.cornache.data.repository.UserRepository
import com.example.cornache.di.Injection

class ViewModelFactory(
    private val userRepository: UserRepository,
    private val loginPreference: LoginPreference,
    private val loginRepository: LoginRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AnalyzeViewModel::class.java) -> {
                AnalyzeViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(loginRepository, loginPreference) as T
            }
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(loginRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        fun getInstance(context: Context, loginPreference: LoginPreference): ViewModelFactory {
            return INSTANCE ?: synchronized(this) {
                val userRepository = Injection.provideRepository(context)
                val apiService = ApiConfig.getApiService() // Initialize ApiService here
                val loginRepository = LoginRepository.getInstance(apiService, loginPreference)
                INSTANCE ?: ViewModelFactory(userRepository, loginPreference, loginRepository).also { INSTANCE = it }
            }
        }
    }
}