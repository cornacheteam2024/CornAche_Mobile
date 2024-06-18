package com.example.cornache

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.cornache.data.LoginPreference
import com.example.cornache.data.ResultState
import com.example.cornache.data.dataStore
import com.example.cornache.databinding.ActivityRegisterBinding
import com.example.cornache.databinding.ItemHistoryBinding
import com.example.cornache.viewmodel.LoginViewModel
import com.example.cornache.viewmodel.RegisterViewModel
import com.example.cornache.viewmodel.ViewModelFactory

class RegisterActivity : AppCompatActivity() {
    private lateinit var viewModel : RegisterViewModel
    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val loginPreference = LoginPreference.getInstance(dataStore)
        val factory = ViewModelFactory.getInstance(this, loginPreference)
        viewModel= ViewModelProvider(this, factory)[RegisterViewModel::class.java]
        binding.registerBtn.setOnClickListener { registerUser() }
        binding.btnLogin.setOnClickListener {
            startActivity(Intent(this@RegisterActivity,LoginActivity::class.java))
        }
    }

    private fun registerUser(){
        val username = binding.usernameEditText.text.toString()
        val password = binding.passwordEditText.text.toString()
        val confirmPass = binding.confirmPassEditText.text.toString()
        if (username.isEmpty() || password.isEmpty() || confirmPass.isEmpty()) {
            showToast("mohon isi semua field")
            return
        }
        viewModel.registerUser(username,password, confirmPass).observe(this){resut ->
            if (resut != null) {
                when(resut){
                    is ResultState.Loading -> showLoading(true)
                    is ResultState.Success -> {
                        showLoading(false)
                        showToast(resut.data.message.toString())
                        startActivity(Intent(this@RegisterActivity,LoginActivity::class.java))
                    }
                    is ResultState.Error -> {
                        showLoading(false)
                        showToast(resut.error)
                    }
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
    private fun showToast(message:String){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show()
    }
}