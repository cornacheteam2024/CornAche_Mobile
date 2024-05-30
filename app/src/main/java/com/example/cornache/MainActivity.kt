package com.example.cornache

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.cornache.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding =ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonAnalisa.setOnClickListener {
            Intent(this@MainActivity, AnalyzeActivity::class.java).also {
                startActivity(it)
            }
        }
        binding.buttonProfil.setOnClickListener {
            Intent(this@MainActivity, EditProfileActivity::class.java).also {
                startActivity(it)
            }
        }
        binding .buttonObrolan.setOnClickListener {
            Intent(this@MainActivity, ChatActivity::class.java).also {
                startActivity(it)
            }
        }
        binding.buttonRiwayat.setOnClickListener {
            Intent(this@MainActivity, HistoryActivity::class.java).also {
                startActivity(it)
            }
        }
    }
}
