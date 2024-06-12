package com.example.cornache.viewmodel

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cornache.data.LoginPreference
import com.example.cornache.data.repository.LoginRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val loginRepository: LoginRepository, private val preferenceLogin: LoginPreference) : ViewModel() {

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    fun login(username: String, password: String) = loginRepository.login(username, password)

    fun saveState(token: String) {
        viewModelScope.launch {
            preferenceLogin.saveToken(token)
            preferenceLogin.login()
        }
    }
}