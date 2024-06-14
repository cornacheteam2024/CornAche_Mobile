package com.example.cornache.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.cornache.data.ResultState
import com.example.cornache.data.api.response.DetailUserResponse
import com.example.cornache.data.repository.HistoryRepository

class EditProfileViewModel(private val repository: HistoryRepository) : ViewModel(){
    fun getDetailUser(userId:String) : LiveData<ResultState<DetailUserResponse>> =repository.getDetailUser(userId)
}