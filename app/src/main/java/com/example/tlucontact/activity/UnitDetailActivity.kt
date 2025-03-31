package com.example.tlucontact.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tlucontact_canhan.databinding.ActivityUnitDetailBinding

class UnitDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUnitDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUnitDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Nhận dữ liệu từ Intent
        val unitName = intent.getStringExtra("unit_name") ?: "Không có tên"
        val unitPhone = intent.getStringExtra("unit_phone") ?: "Không có số điện thoại"
        val unitAddress = intent.getStringExtra("unit_address") ?: "Không có địa chỉ"
        val unitEmail = intent.getStringExtra("unit_email") ?: "Không có email"
        val unitCode = intent.getStringExtra("unit_code") ?: "Không có mã đơn vị"
        val unitFax = intent.getStringExtra("unit_fax") ?: "Không có fax"

        // Hiển thị dữ liệu lên giao diện
        binding.tvUnitName.text = unitName
        binding.tvUnitCode.text = unitCode
        binding.tvUnitEmail.text = unitEmail
        binding.tvUnitPhone.text = unitPhone
        binding.tvUnitAddress.text = unitAddress
        binding.tvUnitFax.text = unitFax

        // Nút gọi điện
        binding.btnUnitCall.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:$unitPhone")
            }
            startActivity(intent)
        }

        // Nút Gửi email
        binding.btnUnitEmail.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:$unitEmail")
                putExtra(Intent.EXTRA_SUBJECT, "Liên hệ với $unitName")
            }
            startActivity(Intent.createChooser(intent, "Gửi email"))
        }

        // Nút Nhắn tin
        binding.btnUnitMessage.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("smsto:$unitPhone")
                putExtra("sms_body", "Xin chào $unitName")
            }
            startActivity(intent)
        }

        // Nút chia sẻ
        binding.btnUnitShare.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, """
                    Tên đơn vị: $unitName
                    Mã đơn vị: $unitCode
                    Email: $unitEmail
                    Số điện thoại: $unitPhone
                    Địa chỉ: $unitAddress
                    Fax: $unitFax
                """.trimIndent())
            }
            startActivity(Intent.createChooser(intent, "Chia sẻ thông tin đơn vị"))
        }

        // Nút quay lại
        binding.btnBack.setOnClickListener {
            finish()
        }
    }
}