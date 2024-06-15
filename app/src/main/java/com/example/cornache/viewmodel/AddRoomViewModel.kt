package com.example.cornache.viewmodel

import androidx.lifecycle.ViewModel
import com.example.cornache.data.repository.RoomRepository
import java.io.File

class AddRoomViewModel(private val repository: RoomRepository) : ViewModel() {
    fun createRoom(name:String, imageFile: File, description:String) = repository.createRoom(name, imageFile, description)
}