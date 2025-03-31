package com.example.tlucontact.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.tlucontact.activity.MainActivity
import com.example.tlucontact.viewmodel.LoginViewModel
import com.example.tlucontact_canhan.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            val email = binding.etLoginEmail.text.toString()
            val password = binding.etLoginPassword.text.toString()
            loginViewModel.login(this, email, password).observe(this) { result: Result<String?> ->
                result.onSuccess { token ->
                    if (token != null) {
                        // Save token to SharedPreferences
                        val sharedPref = getSharedPreferences("APP_PREFS", MODE_PRIVATE)
                        with(sharedPref.edit()) {
                            putString("AUTH_TOKEN", token)
                            apply()
                        }
                        // Save user info: email, password, name
                        with(sharedPref.edit()) {
                            putString("email", email)
                            putString("password", password)
                            apply()
                        }
                        Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                        // Navigate to main screen
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show()
                    }
                }.onFailure {
                    Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show()
                }
            }
        }

//        binding.txtRegister.setOnClickListener() {
//            startActivity(Intent(this, RegisterActivity::class.java))
//        }
    }
}