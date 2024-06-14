package com.example.cornache.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.cornache.data.LoginPreference
import com.example.cornache.data.ResultState
import com.example.cornache.data.api.response.History
import com.example.cornache.data.api.response.HistoryItem
import com.example.cornache.data.api.response.Prediction
import com.example.cornache.data.api.response.Response
import com.example.cornache.data.repository.HistoryRepository
import kotlinx.coroutines.runBlocking

class HistoryViewModel(private val historyRepository: HistoryRepository) : ViewModel() {
//    fun getHistory() : LiveData<ResultState<HistoryItem>> = historyRepository.getHistory()
    val history : LiveData<PagingData<HistoryItem>> = historyRepository.getHistory().cachedIn(viewModelScope)
}