package com.example.tlucontact_canhan.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tlucontact_canhan.repository.AuthRepository
import com.example.tlucontact_canhan.viewmodel.LoginViewModel
import com.example.tlucontact_canhan.databinding.ActivityLoginBinding
import com.example.tlucontact_canhan.viewmodel.LoginState

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel: LoginViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return LoginViewModel(AuthRepository(this@LoginActivity)) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val authRepository = AuthRepository(this)
        if (authRepository.getToken() != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }

        loginViewModel.loginResult.observe(this) { state ->
            binding.progressBar.visibility = View.GONE
            binding.btnLogin.isEnabled = true
            when (state) {
                is LoginState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.btnLogin.isEnabled = false
                }
                is LoginState.Success -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
                is LoginState.Error -> {
                    val exception = state.exception
                    val errorMessage = when {
                        exception.message?.contains("401") == true -> "Sai tên đăng nhập hoặc mật khẩu"
                        exception.message?.contains("network") == true -> "Không có kết nối mạng"
                        else -> "Đăng nhập thất bại: ${exception.message}"
                    }
                    Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.btnLogin.setOnClickListener {
            val username = binding.etLoginEmail.text.toString()
            val password = binding.etLoginPassword.text.toString()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

//            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(username).matches()) {
//                Toast.makeText(this, "Email không hợp lệ", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }
//
//            if (password.length < 6) {
//                Toast.makeText(this, "Mật khẩu phải có ít nhất 6 ký tự", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }

            loginViewModel.login(username, password)
        }

        binding.txtRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}