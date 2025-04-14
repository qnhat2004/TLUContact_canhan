package com.example.tlucontact_canhan.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.tlucontact_canhan.R
import com.example.tlucontact_canhan.activity.StaffDetailActivity
import com.example.tlucontact_canhan.activity.UnitDetailActivity
import com.example.tlucontact_canhan.databinding.ActivityStudentDetailBinding
import com.example.tlucontact_canhan.repository.ContactUnitRepository
import com.example.tlucontact_canhan.viewmodel.ContactUnitViewModel
import com.example.tlucontact_canhan.viewmodel.ContactUnitViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StudentDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStudentDetailBinding
    private lateinit var viewModel: ContactUnitViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize ViewModel
        viewModel = ViewModelProvider(this, ContactUnitViewModelFactory(ContactUnitRepository(this)))
            .get(ContactUnitViewModel::class.java)

        // Lấy dữ liệu từ Intent
        val studentId = intent.getStringExtra("student_id")
        val fullName = intent.getStringExtra("student_fullName")
        val phone = intent.getStringExtra("student_phone")
        val email = intent.getStringExtra("student_email")
        val address = intent.getStringExtra("student_address")
        val unitId = intent.getStringExtra("student_unit") ?: "Không có thông tin"

        // Hiển thị dữ liệu lên giao diện
        binding.tvDetailtStudentId.text = studentId ?: "Không có mã sinh viên"
        binding.tvDetailtStudentName.text = fullName ?: "Không có thông tin"
        binding.tvDetailtStudentPhone.text = phone ?: "Không có thông tin"
        binding.tvDetailtStudentEmail.text = email ?: "Không có thông tin"
        binding.tvDetailtStudentAddress.text = address ?: "Không có thông tin"

        if (unitId != null && unitId.toLong() != 0L) { // Thêm kiểm tra != 0
            Log.d("UnitDetailActivity", "Parent Unit ID: ${unitId}")
            binding.tvDetailtStudentUnit.visibility = View.VISIBLE

            // Launch a coroutine in the lifecycle scope
            lifecycleScope.launch {
                try {
                    val unitId1 = unitId.toLong()
                    val result = withContext(Dispatchers.IO) {
                        viewModel.getUnitById(unitId1)
                    }
                    result.onSuccess { parentUnit ->
                        binding.tvDetailtStudentUnit.text = parentUnit.name
                        binding.tvDetailtStudentUnit.setOnClickListener {
                            val intent = Intent(this@StudentDetailActivity, UnitDetailActivity::class.java).apply {
                                putExtra("unit_id", parentUnit.id)
                            }
                            startActivity(intent)
                        }
                    }.onFailure { exception ->
                        Log.e("UnitDetailActivity", "Failed to load parent unit: ${exception.message}", exception)
                        Toast.makeText(
                            this@StudentDetailActivity,
                            "Không thể tải đơn vị cha: ${exception.message}",
                            Toast.LENGTH_LONG
                        ).show()
                        binding.tvDetailtStudentUnit.text = "Không thể tải đơn vị cha"
                    }
                } catch (e: Exception) {
                    Log.e("UnitDetailActivity", "Error loading parent unit: ${e.message}", e)
                    Toast.makeText(
                        this@StudentDetailActivity,
                        "Lỗi khi tải đơn vị cha: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        } else {
            Log.d("UnitDetailActivity", "No parent unit found (parentUnitId: ${unitId})")
        }


        // Lấy URL ảnh đại diện từ Intent
        val avatarUrl = intent.getStringExtra("student_avatarUrl") ?: "Không có ảnh đại diện"

        // Hiển thị ảnh đại diện
        Glide.with(this@StudentDetailActivity)
            .load(avatarUrl)
            .placeholder(R.drawable.avatar)
            .error(R.drawable.avatar)
            .into(binding.ivStudent)

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
                    Đơn vị: $unitId
                """.trimIndent())
            }
            startActivity(Intent.createChooser(intent, "Chia sẻ thông tin CBGV"))
        }
    }
}