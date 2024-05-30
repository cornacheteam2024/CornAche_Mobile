package com.example.cornache

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.cornache.databinding.ActivityAnalyzeBinding

class AnalyzeActivity : AppCompatActivity() {
    private lateinit var binding:ActivityAnalyzeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAnalyzeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}