package com.example.cornache.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.cornache.data.repository.HistoryRepository

class DetailRoomViewModel(private val repository: HistoryRepository) : ViewModel() {
    fun getDetailRoom(userId:String) = repository.getDetailRoom(userId)
    fun getDetailUser(userId: String) = repository.getDetailUser(userId)
}