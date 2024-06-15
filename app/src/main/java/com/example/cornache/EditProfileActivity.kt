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
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.cornache.data.LoginPreference
import com.example.cornache.data.ResultState
import com.example.cornache.data.dataStore
import com.example.cornache.databinding.ActivityEditProfileBinding
import com.example.cornache.viewmodel.EditProfileViewModel
import com.example.cornache.viewmodel.ViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var viewModel: EditProfileViewModel
    private val loginPreference: LoginPreference = LoginPreference.getInstance(dataStore)
    private var currentImageUri:Uri?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val preference = LoginPreference.getInstance(dataStore)
        val factory: ViewModelFactory = ViewModelFactory.getInstance(this, preference)
        viewModel = ViewModelProvider(this, factory)[EditProfileViewModel::class.java]
        getDetailUser()
        binding.profileImage.setOnClickListener { startGallery() }
        binding.btnSave.setOnClickListener {
            updateDetailUser()
        }
        setupNavigation()
    }

    private fun getDetailUser(){
        val preference = LoginPreference.getInstance(dataStore)
        val userId = runBlocking { preference.getSession().first().userId }
        viewModel.getDetailUser(userId).observe(this){result ->
            if (result!=null){
                when(result){
                    is ResultState.Loading -> showLoading(true)
                    is ResultState.Success -> {
                        showLoading(false)
                        val detailData = result.data.user
                        if (detailData?.avatarImg.isNullOrEmpty()){
                            binding.apply {
                                username.setText(detailData?.username)
                            }
                        }else{
                            binding.apply {
                                GlideApp.with(this@EditProfileActivity)
                                    .load(detailData?.avatarImg)
                                    .into(profileImage)
                                username.setText(detailData?.username)
                            }
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
    private fun updateDetailUser(){
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri,this)
            val updatedUsername = binding.username.text.toString()
            viewModel.updateDetailUser(updatedUsername,imageFile).observe(this){result ->
                if (result!=null){
                    when(result){
                        is ResultState.Loading -> showLoading(true)
                        is ResultState.Success -> {
                            showLoading(false)
                            getDetailUser()
                        }
                        is ResultState.Error -> {
                            showLoading(false)
                            Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    private fun startGallery(){
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
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
    private fun showImage() {
        currentImageUri?.let {
            binding.profileImage.setImageURI(it)
        }
    }
    private fun showLoading(isLoading:Boolean){
        binding.progressBar2.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun setupNavigation() {
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.selectedItemId = R.id.navigation_edit_profile
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
            startActivity(Intent(this@EditProfileActivity, LoginActivity::class.java))
            finish()
        }
    }
}