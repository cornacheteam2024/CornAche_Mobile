package com.example.cornache.viewmodel

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cornache.data.LoginPreference
import com.example.cornache.data.UserModel
import com.example.cornache.data.repository.LoginRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val loginRepository: LoginRepository, private val preferenceLogin: LoginPreference) : ViewModel() {
    fun login(username: String, password: String) = loginRepository.login(username, password)
    fun saveState(userModel: UserModel) {
        viewModelScope.launch {
            loginRepository.saveSession(userModel)
        }
    }
}