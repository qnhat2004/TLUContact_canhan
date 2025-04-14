package com.example.tlucontact_canhan.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.tlucontact_canhan.R
import com.example.tlucontact_canhan.activity.UnitDetailActivity
import com.example.tlucontact_canhan.databinding.ActivityStaffDetailBinding
import com.example.tlucontact_canhan.repository.ContactUnitRepository
import com.example.tlucontact_canhan.viewmodel.ContactUnitViewModel
import com.example.tlucontact_canhan.viewmodel.ContactUnitViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StaffDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStaffDetailBinding
    private lateinit var unitViewModel: ContactUnitViewModel
    private val coroutineScope = CoroutineScope(Dispatchers.Main + Job())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStaffDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Khởi tạo ViewModel
        unitViewModel = ViewModelProvider(
            this,
            ContactUnitViewModelFactory(ContactUnitRepository(this))
        ).get(ContactUnitViewModel::class.java)

        // Lấy dữ liệu từ Intent
        val name = intent.getStringExtra("staff_fullName") ?: "Không có tên"
        val phone = intent.getStringExtra("staff_phone") ?: "Không có số điện thoại"
        val email = intent.getStringExtra("staff_email") ?: "Không có email"
        val staffId = intent.getStringExtra("staff_id") ?: "Không có mã cán bộ"
        val address = intent.getStringExtra("staff_address") ?: "Không có địa chỉ"
        val education = intent.getStringExtra("staff_education") ?: "Không có trình độ"
        val unitId = intent.getLongExtra("staff_unitId", -1L) // Sửa thành getLongExtra
        val unitName = intent.getStringExtra("staff_unitName") ?: "Không có đơn vị"
        val position = intent.getStringExtra("staff_position") ?: "Không có chức vụ"
        val avatarUrl = intent.getStringExtra("staff_avatarUrl") ?: "Không có ảnh đại diện"

        // Gán dữ liệu vào giao diện
        binding.tvDetailtStaffName.text = name
        binding.tvDetailtStaffPhone.text = phone
        binding.tvDetailtStaffEmail.text = email
        binding.tvDetailtStaffPosition.text = position
        binding.tvDetailtStaffCode.text = staffId
        binding.tvDetailtStaffAddress.text = address
        binding.tvDetailtStaffEducation.text = education

        // Hiển thị ảnh đại diện
        Glide.with(this@StaffDetailActivity)
            .load(avatarUrl)
            .placeholder(R.drawable.avatar)
            .error(R.drawable.avatar)
            .into(binding.ivStaffImage)

        // Hiển thị đơn vị và xử lý sự kiện nhấn vào đơn vị
        if (unitId != -1L) {
            binding.tvDetailtStaffUnit.text = unitName
            binding.tvDetailtStaffUnit.setOnClickListener {
                // Chuyển hướng đến UnitDetailActivity
                val intent = Intent(this@StaffDetailActivity, UnitDetailActivity::class.java).apply {
                    putExtra("unit_id", unitId)
                }
                startActivity(intent)
            }
        } else {
            binding.tvDetailtStaffUnit.text = "Không có đơn vị"
            binding.tvDetailtStaffUnit.isClickable = false // Không cho phép nhấn nếu không có đơn vị
        }

        // Nút gọi điện
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
                    Đơn vị: $unitName
                """.trimIndent())
            }
            startActivity(Intent.createChooser(intent, "Chia sẻ thông tin CBGV"))
        }

        // Nút quay lại
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.coroutineContext[Job]?.cancel()
    }
}