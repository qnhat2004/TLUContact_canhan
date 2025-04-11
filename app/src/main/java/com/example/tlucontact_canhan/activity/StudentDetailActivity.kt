package com.example.tlucontact_canhan.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tlucontact_canhan.databinding.ActivityStudentDetailBinding

class StudentDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStudentDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Lấy dữ liệu từ Intent
        val studentId = intent.getStringExtra("student_id")
        val fullName = intent.getStringExtra("student_fullName")
        val phone = intent.getStringExtra("student_phone")
        val email = intent.getStringExtra("student_email")
        val address = intent.getStringExtra("student_address")
        val unit = intent.getStringExtra("student_unit") ?: "Không có thông tin"

        // Hiển thị dữ liệu lên giao diện
        binding.tvDetailtStudentId.text = studentId ?: "Không có mã sinh viên"
        binding.tvDetailtStudentName.text = fullName ?: "Không có thông tin"
        binding.tvDetailtStudentPhone.text = phone ?: "Không có thông tin"
        binding.tvDetailtStudentEmail.text = email ?: "Không có thông tin"
        binding.tvDetailtStudentAddress.text = address ?: "Không có thông tin"
        binding.tvDetailtStudentUnit.text = intent.getStringExtra("student_unit") ?: "Không có thông tin"

        // Thiết lập nút Back (nếu cần)
        binding.btnBack.setOnClickListener {
            finish()
        }

        // Nút gọi điện
        binding.btnStudentCall.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:$phone")
            }
            startActivity(intent)
        }

        // Nút Gửi email
        binding.btnStudentEmail.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:$email")
                putExtra(Intent.EXTRA_SUBJECT, "Liên hệ với $email")
            }
            startActivity(Intent.createChooser(intent, "Gửi email"))
        }

        // Nút Nhắn tin
        binding.btnStudentMessage.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("smsto:$phone")
                putExtra("sms_body", "Xin chào $phone")
            }
            startActivity(intent)
        }

        // Nút chia sẻ
        binding.btnStudentShare.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, """
                    Tên sinh viên: $fullName
                    Email: $email
                    Số điện thoại: $phone
                    Địa chỉ: $address
                    Đơn vị: $unit
                """.trimIndent())
            }
            startActivity(Intent.createChooser(intent, "Chia sẻ thông tin CBGV"))
        }
    }
}