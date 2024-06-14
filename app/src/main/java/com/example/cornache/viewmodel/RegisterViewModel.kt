package com.example.cornache.viewmodel

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.lifecycle.ViewModel
import com.example.cornache.data.repository.RegisterRepository

class RegisterViewModel(private val registerRepository: RegisterRepository) : ViewModel() {
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    fun register(name: String, email: String, password: String) = registerRepository.register(name, email, password)
}