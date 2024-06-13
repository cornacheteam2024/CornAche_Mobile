package com.example.cornache

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cornache.adapter.HistoryAdapter
import com.example.cornache.data.LoginPreference
import com.example.cornache.data.ResultState
import com.example.cornache.data.dataStore
import com.example.cornache.databinding.ActivityHistoryBinding
import com.example.cornache.viewmodel.HistoryViewModel
import com.example.cornache.viewmodel.HistoryViewModelFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class HistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistoryBinding
    private lateinit var viewModel: HistoryViewModel
    private lateinit var preference: LoginPreference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val factory: HistoryViewModelFactory = HistoryViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[HistoryViewModel::class.java]
        binding.rvHistory.layoutManager = LinearLayoutManager(this)
        getData()
    }

//    private fun getData() {
//        binding.rvHistory.adapter = adapter
//        preference = LoginPreference.getInstance(dataStore)
//        val adapter = HistoryAdapter()
//        binding.rvHistory.adapter = adapter
//        viewModel.getHistory().observe(this) { result ->
//            if (result != null) {
//                when (result) {
//                    is ResultState.Loading -> {
////                        showLoading(true)
//                    }
//
//                    is ResultState.Success -> {
////                        showLoading(false)
//                        val historyData = result.data
//                        val historyItemList = mutableListOf(historyData)
//                        adapter.submitList(historyItemList)
//                    }
//
//                    is ResultState.Error -> {
//                        Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
//                    }
//                }
//            }
//        }
//    }

    private fun getData() {
        val adapter = HistoryAdapter()
        binding.rvHistory.adapter = adapter
        viewModel.history.observe(this,{
            adapter.submitData(lifecycle,it)
        })
    }
}
