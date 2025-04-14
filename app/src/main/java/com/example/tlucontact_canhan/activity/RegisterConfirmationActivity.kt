package com.example.tlucontact_canhan.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tlucontact_canhan.databinding.ActivityRegisterConfirmationBinding

class RegisterConfirmationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterConfirmationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterConfirmationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Xử lý sự kiện nút chuyển đến trang đăng nhập
        binding.btnGoToLogin.setOnClickListener {
            // Chuyển đến trang đăng nhập
            finish()
        }
    }
}