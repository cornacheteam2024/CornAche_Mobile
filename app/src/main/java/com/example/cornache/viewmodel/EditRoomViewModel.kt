package com.example.cornache.viewmodel

import androidx.lifecycle.ViewModel
import com.example.cornache.data.repository.RoomRepository
import java.io.File

class EditRoomViewModel(private val repository: RoomRepository) : ViewModel() {
    fun updateRoom(roomId:String, name:String, imageFile: File, description:String) = repository.updateRoom(roomId, name, imageFile, description)
    fun deleteRoom(roomId: String) = repository.deleteRoom(roomId)
}