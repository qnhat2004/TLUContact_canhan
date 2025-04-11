package com.example.tlucontact_canhan.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tlucontact_canhan.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        // Xử lý sự kiện nút đăng ký
//        binding.btnRegister.setOnClickListener {
//            val username = binding.etRegisterEmail.text.toString()
//            val password = binding.etRegisterPassword.text.toString()
//            val confirmPassword = binding.etRegisterConfirmPassword.text.toString()
//
//            if (password == confirmPassword) {
//                // Gọi hàm đăng ký từ ViewModel hoặc Repository
//                // ...
//            } else {
//                // Hiển thị thông báo lỗi
//                // ...
//            }
//        }
    }
}