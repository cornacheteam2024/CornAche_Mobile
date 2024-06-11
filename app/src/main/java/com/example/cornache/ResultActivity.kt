package com.example.cornache

import android.net.Uri
import android.os.Binder
import android.os.Bundle
import android.widget.MediaController
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.cornache.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
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
    }
    companion object{
        const val DATA = "data"
    }
}