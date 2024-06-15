package com.example.cornache

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.cornache.data.LoginPreference
import com.example.cornache.data.ResultState
import com.example.cornache.data.dataStore
import com.example.cornache.databinding.ActivityAddRoomBinding
import com.example.cornache.viewmodel.AddRoomViewModel
import com.example.cornache.viewmodel.MainViewModel
import com.example.cornache.viewmodel.ViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch

class AddRoomActivity : AppCompatActivity() {
    private lateinit var binding : ActivityAddRoomBinding
    private lateinit var loginPreference: LoginPreference
    private lateinit var viewModel : AddRoomViewModel
    private var currentImageUri:Uri?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val loginPreference = LoginPreference.getInstance(dataStore)
        val factory : ViewModelFactory = ViewModelFactory.getInstance(this, loginPreference)
        viewModel = ViewModelProvider(this,factory)[AddRoomViewModel::class.java]
        binding.imagePlaceholder.setOnClickListener { startGallery() }
        binding.postRoom.setOnClickListener { createRoom() }
        setupNavigation()
    }
    private fun createRoom(){
        currentImageUri?.let {
            val imageFile = uriToFile(it,this)
            val name = binding.judulText.text.toString()
            val description = binding.DeskripsiText.text.toString()
            viewModel.createRoom(name,imageFile,description).observe(this){result ->
                if (result!=null){
                    when(result){
                        is ResultState.Loading -> showLoading(true)
                        is ResultState.Success -> {
                            showLoading(false)
                            Intent(this@AddRoomActivity,RoomActivity::class.java).also {
                                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(it)
                            }
                        }
                        is ResultState.Error -> {
                            showLoading(false)
                            showToast(result.error)
                        }
                    }
                }
            }
        }
    }
    private fun startGallery(){
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }
    private fun showImage() {
        currentImageUri?.let {
            binding.imagePlaceholder.setImageURI(it)
        }
    }
    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ){uri: Uri? ->
        if (uri != null){
            currentImageUri = uri
            showImage()
        }else{
            Toast.makeText(this, "Silahkan pilih gambar terlebih dahulu", Toast.LENGTH_SHORT).show()
        }
    }
    private fun showLoading(isLoading:Boolean){
        binding.progressBar3.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun setupNavigation() {
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.selectedItemId = R.id.navigation_chat
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
            startActivity(Intent(this@AddRoomActivity, LoginActivity::class.java))
            finish()
        }
    }
}