package com.example.cornache.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.cornache.data.api.response.ChatsItem
import com.example.cornache.data.api.response.HistoryItem
import com.example.cornache.data.repository.HistoryRepository
import com.example.cornache.data.repository.RoomRepository

class DetailRoomViewModel(private val repository: HistoryRepository, private val roomRepository: RoomRepository) : ViewModel() {
    fun getDetailRoom(roomId:String) = repository.getDetailRoom(roomId)
    fun getDetailUser(userId: String) = repository.getDetailUser(userId)
    fun comments(roomId:String) : LiveData<PagingData<ChatsItem>> = repository.getComments(roomId).cachedIn(viewModelScope)
    fun postComment(roomId: String, content:String) = roomRepository.postComment(roomId,content)
}