package com.example.cornache

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresExtension
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.cornache.data.LoginPreference
import com.example.cornache.data.ResultState
import com.example.cornache.data.dataStore
import com.example.cornache.databinding.ActivityRegisterBinding
import com.example.cornache.viewmodel.RegisterViewModel
import com.example.cornache.viewmodel.ViewModelFactory

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // progress bar visibility
        binding.progressBar.visibility = View.GONE
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    private fun setup() {
        val factory: ViewModelFactory = ViewModelFactory.getInstance(this, LoginPreference.getInstance(dataStore));
        val viewModel: RegisterViewModel = ViewModelProvider(this, factory)[RegisterViewModel::class.java]

        binding.submit.setOnClickListener {
            val username = binding.username.text.toString()
            val password = binding.password.text.toString()
            val passwordConfirm = binding.passwordConfirm.text.toString()

            viewModel.register(username, passwordConfirm, password).observe(this) {
                if (it != null) {
                    when (it) {
                        is ResultState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }

                        is ResultState.Success -> {
                            val response = it.data
                            binding.progressBar.visibility = View.GONE
                            AlertDialog.Builder(this).apply {
                                setTitle(getString(R.string.success))
                                setMessage(response.message)
                                setPositiveButton(getString(R.string.dialog_continue)) { _, _ ->
                                    startActivity(
                                        Intent(
                                            this@RegisterActivity,
                                            LoginActivity::class.java
                                        )
                                    )
                                }
                                create()
                                show()
                            }.apply {
                                setOnCancelListener {
                                    startActivity(
                                        Intent(
                                            this@RegisterActivity,
                                            LoginActivity::class.java
                                        )
                                    )
                                }
                                show()
                            }
                        }

                        is ResultState.Error -> {
                            binding.progressBar.visibility = View.GONE
                            AlertDialog.Builder(this).apply {
                                setTitle(getString(R.string.error))
                                setMessage(it.error)
                                setPositiveButton(getString(R.string.dialog_continue)) { _, _ -> }
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