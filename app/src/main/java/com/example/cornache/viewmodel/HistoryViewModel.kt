package com.example.cornache.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.cornache.data.api.FileUploadResponse
import com.example.cornache.data.api.History
import com.example.cornache.data.api.Prediction
import com.example.cornache.data.repository.HistoryRepository
import com.example.cornache.data.repository.UserRepository

class HistoryViewModel(historyRepository: HistoryRepository) : ViewModel() {
    val getHistory:LiveData<PagingData<Prediction>> = historyRepository.getHistory().cachedIn(viewModelScope)
}