package com.example.cornache.viewmodel

import androidx.lifecycle.ViewModel
import com.example.cornache.data.repository.UserRepository
import java.io.File

class AnalyzeViewModel(private val repo:UserRepository) : ViewModel() {
    fun analyzeImage(file: File,userId:String) = repo.analyzeImage(file,userId)
}