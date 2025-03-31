package com.example.tlucontact.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tlucontact_canhan.databinding.ActivityStaffDetailBinding

class StaffDetailtActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStaffDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStaffDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val name = intent.getStringExtra("staff_name") ?: "Không có tên"
        val phone = intent.getStringExtra("staff_phone") ?: "Không có số điện thoại"
        val email = intent.getStringExtra("staff_email") ?: "Không có email"
        val unit = intent.getStringExtra("staff_unit") ?: "Không có đơn vị"
        val position = intent.getStringExtra("staff_position") ?: "Không có chức vụ"

        binding.tvDetailtStaffName.text = name
        binding.tvDetailtStaffPhone.text = "SĐT: $phone"
        binding.tvDetailtStaffEmail.text = "Email: $email"
        binding.tvDetailtStaffUnit.text = "Đơn vị: $unit"
        binding.tvDetailtStaffPosition.text = "Chức vụ: $position"

        binding.btnUnitCall.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:$phone")
            }
            startActivity(intent)
        }

        // Nút Gửi email
        binding.btnUnitEmail.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:$email")
                putExtra(Intent.EXTRA_SUBJECT, "Liên hệ với $email")
            }
            startActivity(Intent.createChooser(intent, "Gửi email"))
        }

        // Nút Nhắn tin
        binding.btnUnitMessage.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("smsto:$phone")
                putExtra("sms_body", "Xin chào $phone")
            }
            startActivity(intent)
        }

        // Nút chia sẻ
        binding.btnUnitShare.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, """
                    Tên cán bộ nhân viên: $name
                    Email: $email
                    Số điện thoại: $phone
                    Chức vụ: $position
                    Đơn vị: $unit
                """.trimIndent())
            }
            startActivity(Intent.createChooser(intent, "Chia sẻ thông tin CBGV"))
        }

        // Nút quay lại
        binding.btnBack.setOnClickListener {
            finish()
        }
    }
}