package com.example.cornache.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.cornache.data.ResultState
import com.example.cornache.data.api.response.DetailUserResponse
import com.example.cornache.data.repository.HistoryRepository
import com.example.cornache.data.repository.LoginRepository
import java.io.File
import kotlin.math.log

class EditProfileViewModel(private val repository: HistoryRepository, private val loginRepository: LoginRepository) : ViewModel() {
    fun getDetailUser(userId: String): LiveData<ResultState<DetailUserResponse>> = repository.getDetailUser(userId)

    fun updateDetailUser(username: String, imageFile: File?) = loginRepository.editProfile(username, imageFile)
}