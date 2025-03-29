package com.example.tlucontact_canhan

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tlucontact_canhan.databinding.ActivityUnitDetailBinding

class UnitDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUnitDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUnitDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val name = intent.getStringExtra("unit_name")
        val phone = intent.getStringExtra("unit_phone")
        val address = intent.getStringExtra("unit_address")

        binding.tvDetailName.text = name
        binding.tvDetailPhone.text = "SĐT: $phone"
        binding.tvDetailAddress.text = "Địa chỉ: $address"

        binding.btnBack.setOnClickListener { finish() }
    }
}