package com.example.cornache.viewmodel

import androidx.lifecycle.ViewModel
import com.example.cornache.data.repository.LoginRepository

class RegisterViewModel(private val repository: LoginRepository) : ViewModel() {
    fun registerUser(username:String,password:String,confirmPass:String) = repository.register(username,password, confirmPass)
}