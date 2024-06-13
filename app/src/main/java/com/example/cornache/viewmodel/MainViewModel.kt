package com.example.cornache.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.cornache.data.UserModel
import com.example.cornache.data.repository.LoginRepository

class MainViewModel(private val repository: LoginRepository) : ViewModel(){
    fun getSession():LiveData<UserModel>{
        return repository.getSession().asLiveData()
    }
}