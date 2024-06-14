package com.example.cornache.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.cornache.data.UserModel
import com.example.cornache.data.repository.LoginRepository
import kotlinx.coroutines.launch

class MainViewModel(private val repository: LoginRepository) : ViewModel(){
    fun getSession():LiveData<UserModel>{
        return repository.getSession().asLiveData()
    }
    fun logout(){
        viewModelScope.launch {
            repository.logout()
        }
    }
}