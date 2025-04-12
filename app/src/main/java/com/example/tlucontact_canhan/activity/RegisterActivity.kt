package com.example.tlucontact_canhan.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tlucontact_canhan.databinding.ActivityRegisterBinding
import com.example.tlucontact_canhan.repository.AuthRepository
import com.example.tlucontact_canhan.viewmodel.RegisterViewModel

sealed class RegisterState {
    object Loading : RegisterState()
    data class Success(val data: Any?) : RegisterState()
    data class Error(val exception: Throwable?) : RegisterState()
}

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var registerViewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Khởi tạo ViewModel
        registerViewModel = RegisterViewModel(AuthRepository(this))

        // Xử lý sự kiện nút đăng ký
        binding.btnRegister.setOnClickListener {
            val email = binding.etEmailRegister.text.toString()
            val password = binding.etPasswordRegister.text.toString()
            val confirmPassword = binding.etPasswordConfirm.text.toString()

            if (password == confirmPassword) {
                registerViewModel.register(email, password)
                registerViewModel.registerResult.observe(this) { state ->
                    binding.progressBar.visibility = View.GONE
                    when (state) {
                        is RegisterState.Loading -> {
                            // Hiển thị loading
                            binding.progressBar.visibility = View.VISIBLE
                        }
                        is RegisterState.Success -> {
                            // Đăng ký thành công
                            Toast.makeText(this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show()
                            binding.progressBar.visibility = View.GONE
                        }
                        is RegisterState.Error -> {
                            // Hiển thị lỗi
                            Toast.makeText(this, "Lỗi: ${state.exception?.message}", Toast.LENGTH_SHORT).show()
                            binding.progressBar.visibility = View.GONE
                        }
                        else -> {
                            // Xử lý trường hợp không xác định (nếu cần)
                            binding.progressBar.visibility = View.GONE
                        }
                    }
                }
            } else {
                binding.etPasswordConfirm.error = "Mật khẩu không khớp"
            }
        }

        binding.txtGoToLogin.setOnClickListener {
            finish()
        }
    }
}