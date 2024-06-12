package com.example.cornache

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cornache.adapter.HistoryListAdapter
import com.example.cornache.databinding.ActivityHistoryBinding
import com.example.cornache.viewmodel.HistoryViewModel
import com.example.cornache.viewmodel.HistoryViewModelFactory

class HistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistoryBinding
    private lateinit var viewModel: HistoryViewModel
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
//        val factory :HistoryViewModelFactory = HistoryViewModelFactory.getInstance(this)
//        viewModel = ViewModelProvider(this,factory)[HistoryViewModel::class.java]
//        binding.rvHistory.layoutManager = LinearLayoutManager(this)
//        getData()
    }

    private fun getData(){
        val adapter = HistoryListAdapter()
        binding.rvHistory.adapter = adapter
        viewModel.getHistory.observe(this,{
            adapter.submitData(lifecycle, it)
        })
    }
}