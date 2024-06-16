package com.example.cornache

import android.content.ContentValues.TAG
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
import com.example.cornache.data.UserModel
import com.example.cornache.data.dataStore
import com.example.cornache.databinding.ActivityLoginBinding
import com.example.cornache.viewmodel.LoginViewModel
import com.example.cornache.viewmodel.ViewModelFactory


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val loginPreference = LoginPreference.getInstance(dataStore)
        val factory = ViewModelFactory.getInstance(this, loginPreference)
        viewModel= ViewModelProvider(this, factory)[LoginViewModel::class.java]
        binding.progressBar.visibility = View.GONE

        setup()
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    private fun setup() {
        binding.loginButton.setOnClickListener {
            val username = binding.username.text.toString()
            val password = binding.password.text.toString()

            when {
                username.isEmpty() -> binding.username.error = "Email tidak boleh kosong"
                password.isEmpty() -> binding.password.error = "Password tidak boleh kosong"

                else -> {
                    viewModel.login(username, password).observe(this) { result ->
                        if (result != null) {
                            when (result) {
                                is ResultState.Loading -> binding.progressBar.visibility = View.VISIBLE
                                is ResultState.Success -> {
                                    binding.progressBar.visibility = View.GONE
                                    val response = result.data

                                    Log.d("LoginActivity", "Response: ${response}")
                                    response.user?.let { user ->
                                        viewModel.saveState(UserModel(user.userId.toString(), user.token.toString()))
                                        AlertDialog.Builder(this).apply {
                                            setTitle(getString(R.string.success))
                                            setMessage(getString(R.string.welcome_back) + " " + "${user.username}")
                                            setPositiveButton(getString(R.string.dialog_continue)) { _, _ ->
                                                val intent = Intent(this@LoginActivity, RoomActivity::class.java)
                                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                                startActivity(intent)
                                                finish()
                                            }
                                            create()
                                            show()
                                        }
                                    }
                                }
                                is ResultState.Error -> {
                                    binding.progressBar.visibility = View.GONE
                                    AlertDialog.Builder(this).apply {
                                        setTitle("Error")
                                        setMessage(result.error)
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

        binding.btnRegister.setOnClickListener {
            startActivity(Intent(this@LoginActivity,RegisterActivity::class.java))
        }
    }
}
