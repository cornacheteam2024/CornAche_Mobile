package com.example.cornache.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.cornache.data.api.response.Prediction
import com.example.cornache.data.repository.HistoryRepository

class HistoryViewModel(historyRepository: HistoryRepository) : ViewModel() {
    val getHistory:LiveData<PagingData<Prediction>> = historyRepository.getHistory().cachedIn(viewModelScope)
}