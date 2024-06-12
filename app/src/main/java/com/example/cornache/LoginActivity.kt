package com.example.cornache

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresExtension
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.cornache.data.LoginPreference
import com.example.cornache.data.ResultState
import com.example.cornache.data.dataStore
import com.example.cornache.databinding.ActivityLoginBinding
import com.example.cornache.viewmodel.LoginViewModel
import com.example.cornache.viewmodel.ViewModelFactory


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.progressBar.visibility = View.GONE

        setup()
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    private fun setup() {
        val loginPreference = LoginPreference.getInstance(dataStore)
        val factory = ViewModelFactory.getInstance(this, loginPreference)
        val viewModel: LoginViewModel = ViewModelProvider(this, factory)[LoginViewModel::class.java]

        binding.loginButton.setOnClickListener {
            val username = binding.username.text.toString()
            val password = binding.password.text.toString()

            when {
                username.isEmpty() -> binding.username.error = "Email tidak boleh kosong"
                password.isEmpty() -> binding.password.error = "Password tidak boleh kosong"

                else -> {
                    viewModel.login(username, password).observe(this) {
                        if (it != null) {
                            when (it) {
                                is ResultState.Loading -> binding.progressBar.visibility = View.VISIBLE
                                is ResultState.Success -> {
                                    binding.progressBar.visibility = View.GONE
                                    val response = it.data

                                    Log.d("LoginActivity", "Response: ${response}")

                                    val token = response.token
                                    if (token != null) {
                                        viewModel.saveState(token.toString())
                                        AlertDialog.Builder(this).apply {
                                            setTitle(getString(R.string.success))
                                            setMessage(getString(R.string.welcome_back) + " " + "${response.username}")
                                            setPositiveButton(getString(R.string.dialog_continue)) { _, _ ->
                                                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                                startActivity(intent)
                                                finish()
                                            }
                                            create()
                                            show()
                                        }
                                    } else {
                                        Log.e("LoginActivity", "Token is null")
                                        // Handle the null token case appropriately
                                    }
                                }
                                is ResultState.Error -> {
                                    binding.progressBar.visibility = View.GONE
                                    AlertDialog.Builder(this).apply {
                                        setTitle("Error")
                                        setMessage(it.error)
                                        setPositiveButton("OK") { _, _ -> }
                                        create()
                                        show()
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
