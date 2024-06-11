package com.example.cornache.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.cornache.data.repository.HistoryRepository
import com.example.cornache.data.repository.UserRepository
import com.example.cornache.di.Injection

class HistoryViewModelFactory(private val historyRepository: HistoryRepository) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HistoryViewModel::class.java) ->{
                HistoryViewModel(historyRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        fun getInstance(context: Context) = HistoryViewModelFactory(Injection.historyRepository(context))
    }
}