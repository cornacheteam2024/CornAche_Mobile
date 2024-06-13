package com.example.cornache

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.cornache.data.LoginPreference
import com.example.cornache.data.dataStore
import com.example.cornache.databinding.ActivityMainBinding
import com.example.cornache.viewmodel.MainViewModel
import com.example.cornache.viewmodel.ViewModelFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel:MainViewModel
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Permission request granted", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Permission request denied", Toast.LENGTH_LONG).show()
            }
        }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            this,
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding =ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (!allPermissionsGranted()){
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }
        val loginPreference = LoginPreference.getInstance(dataStore)
        val factory : ViewModelFactory = ViewModelFactory.getInstance(this, loginPreference)
        viewModel = ViewModelProvider(this,factory)[MainViewModel::class.java]
        val token = runBlocking {
            loginPreference.getSession().first().token
        }

        viewModel.getSession().observe(this){user ->
            if (!user.isLogin){
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
            }
            else{
                setupView()
                setupAction()
                Toast.makeText(this, token, Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupAction() {
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
    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}
