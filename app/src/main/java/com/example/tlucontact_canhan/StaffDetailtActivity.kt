package com.example.tlucontact_canhan

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tlucontact_canhan.databinding.ActivityStaffDetailBinding

class StaffDetailtActivity: AppCompatActivity() {
    private lateinit var binding: ActivityStaffDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStaffDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val name = intent.getStringExtra("staff_name")
        val phone = intent.getStringExtra("staff_phone")
        val email = intent.getStringExtra("staff_email")
        val unit = intent.getStringExtra("staff_unit")
        val position = intent.getStringExtra("staff_position")

        binding.tvDetailtStaffName.text = name
        binding.tvDetailtStaffPhone.text = "SĐT: $phone"
        binding.tvDetailtStaffEmail.text = "Email: $email"
        binding.tvDetailtStaffUnit.text = "Đơn vị: $unit"
        binding.tvDetailtStaffPosition.text = "Chức vụ: $position"

        binding.btnStaffBack.setOnClickListener { finish() }
    }
}