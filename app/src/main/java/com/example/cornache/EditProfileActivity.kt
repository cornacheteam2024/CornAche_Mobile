package com.example.cornache

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.cornache.data.LoginPreference
import com.example.cornache.data.ResultState
import com.example.cornache.data.dataStore
import com.example.cornache.databinding.ActivityEditProfileBinding
import com.example.cornache.viewmodel.EditProfileViewModel
import com.example.cornache.viewmodel.ViewModelFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var viewModel: EditProfileViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val preference = LoginPreference.getInstance(dataStore)
        val factory: ViewModelFactory = ViewModelFactory.getInstance(this, preference)
        viewModel = ViewModelProvider(this, factory)[EditProfileViewModel::class.java]
        getDetailUser()
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
    private fun showLoading(isLoading:Boolean){
        binding.progressBar2.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}