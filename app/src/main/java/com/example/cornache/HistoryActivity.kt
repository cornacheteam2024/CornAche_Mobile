package com.example.cornache

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cornache.adapter.HistoryAdapter
import com.example.cornache.adapter.LoadingStateAdapter
import com.example.cornache.data.LoginPreference
import com.example.cornache.data.dataStore
import com.example.cornache.databinding.ActivityHistoryBinding
import com.example.cornache.viewmodel.HistoryViewModel
import com.example.cornache.viewmodel.ViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch

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
        preference = LoginPreference.getInstance(dataStore)
        val factory: ViewModelFactory = ViewModelFactory.getInstance(this,preference)
        viewModel = ViewModelProvider(this, factory)[HistoryViewModel::class.java]
        binding.rvHistory.layoutManager = LinearLayoutManager(this)
        showLoading(true)
        getData()
        setupNavigation()
    }

    private fun getData() {
        val adapter = HistoryAdapter()
        binding.rvHistory.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter{
                adapter.retry()
            }
        )
        viewModel.history.observe(this,{
            adapter.submitData(lifecycle,it)
        })
        showLoading(false)
    }
    private fun showLoading(isLoading:Boolean){
        binding.progressBar3.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setupNavigation() {
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.selectedItemId = R.id.navigation_history
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_history -> {
                    startActivity(Intent(this, HistoryActivity::class.java))
                    true
                }
                R.id.navigation_detect_disease -> {
                    startActivity(Intent(this, AnalyzeActivity::class.java))
                    true
                }
                R.id.navigation_edit_profile -> {
                    startActivity(Intent(this, EditProfileActivity::class.java))
                    true
                }
                R.id.navigation_logout -> {
                    logout()
                    true
                }
                R.id.navigation_chat -> {
                    startActivity(Intent(this, RoomActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }

    private fun logout() {
        lifecycleScope.launch {
            preference.logout()
            startActivity(Intent(this@HistoryActivity, LoginActivity::class.java))
            finish()
        }
    }
}
