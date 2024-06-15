package com.example.cornache.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.cornache.data.ResultState
import com.example.cornache.data.api.response.DataItem
import com.example.cornache.data.repository.HistoryRepository
import com.example.cornache.data.repository.RoomRepository

class RoomViewModel(private val repository: HistoryRepository): ViewModel() {
    fun getRoomList():LiveData<ResultState<List<DataItem?>?>> = repository.getListRoom()
}