package com.example.cornache

import android.content.Intent
import android.net.Uri
import android.os.Binder
import android.os.Build
import android.os.Bundle
import android.widget.MediaController
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.cornache.data.LoginPreference
import com.example.cornache.data.dataStore
import com.example.cornache.databinding.ActivityResultBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    private val loginPreference = LoginPreference.getInstance(dataStore)
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val bundle = intent.getBundleExtra(DATA)
        val webView = binding.webView
        val nama = bundle?.getString("nama")
        val gambar = bundle?.getString("gambar")
        binding.apply {
            Glide.with(this@ResultActivity)
                .load(gambar?.toUri())
                .into(ivResult)
        }
        if (nama == "Common_Rust"){
            binding.apply {
                tvDisseaseName.text = "Common rust"
                desc.text = getString(R.string.common_rust)
            }
            val video = "<iframe width=\"560\" height=\"315\" src=\"https://www.youtube.com/embed/bP8wBKISWWY?si=BnkamTyzZlKm0qRO\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" referrerpolicy=\"strict-origin-when-cross-origin\" allowfullscreen></iframe>"
            webView.loadData(video,"text/html", "utf-8")
            webView.settings.javaScriptEnabled = true
            webView.webChromeClient
        }
        else if (nama == "Healthy"){
            binding.apply {
                tvDisseaseName.text = "Sehat"
                desc.text = getString(R.string.healty)
            }
        }
        else if (nama == "Gray_Leaf_Spot"){
            binding.apply {
                tvDisseaseName.text = "Gray leaf spot"
                desc.text = getString(R.string.gray_leaf)
            }
            val video = "<iframe width=\"560\" height=\"315\" src=\"https://www.youtube.com/embed/1foL4zGaDAE?si=_YK7J9zyWwvB-Z64\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" referrerpolicy=\"strict-origin-when-cross-origin\" allowfullscreen></iframe>"
            webView.loadData(video,"text/html", "utf-8")
            webView.settings.javaScriptEnabled = true
            webView.webChromeClient
        }
        else {
            binding.apply {
                tvDisseaseName.text = "Blight"
                desc.text = getString(R.string.blight)
            }
            val video = "<iframe width=\"560\" height=\"315\" src=\"https://www.youtube.com/embed/-Ffu_xiLrdU?si=hhjeepHCjDaAbJwi\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" referrerpolicy=\"strict-origin-when-cross-origin\" allowfullscreen></iframe>"
            webView.loadData(video,"text/html", "utf-8")
            webView.settings.javaScriptEnabled = true
            webView.webChromeClient
        }
        setupNavigation()
    }

    private fun setupNavigation() {
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.selectedItemId = R.id.navigation_detect_disease
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
            loginPreference.logout()
            startActivity(Intent(this@ResultActivity, LoginActivity::class.java))
            finish()
        }
    }

    companion object{
        const val DATA = "data"
    }
}