package com.example.tlucontact_canhan.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.tlucontact_canhan.databinding.ActivityUpdateProfileBinding
import com.example.tlucontact_canhan.repository.AuthRepository
import com.example.tlucontact_canhan.repository.StaffRepository
import com.example.tlucontact_canhan.repository.StudentRepository
import com.example.tlucontact_canhan.repository.UserRepository
import com.example.tlucontact_canhan.viewmodel.AccountProfileState
import com.example.tlucontact_canhan.viewmodel.ProfileViewModel

sealed class UpdateProfileState {
    object Loading : UpdateProfileState()
    object Success : UpdateProfileState()
    data class Error(val message: String) : UpdateProfileState()
}

class UpdateProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateProfileBinding
    private lateinit var profileViewModel: ProfileViewModel
    private var role: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Khởi tạo ViewModel
        val authRepository = AuthRepository(this)
        val studentRepository = StudentRepository(this)
        val staffRepository = StaffRepository(this)
        val userRepository = UserRepository(this)

        profileViewModel = ViewModelProvider(
            this,
            object : ViewModelProvider.Factory {
                override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
                        return ProfileViewModel(authRepository, studentRepository, staffRepository) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
        ).get(ProfileViewModel::class.java)

        // Lấy thông tin người dùng hiện tại để điền sẵn vào form
        profileViewModel.profileState.observe(this) { state ->
            when (state) {
                is AccountProfileState.StudentSuccess -> {
                    role = "ROLE_STUDENT"
                    binding.etPhone.setText(state.data.student.phone ?: "")
                    binding.etAddress.setText(state.data.student.address ?: "")
                }
                is AccountProfileState.StaffSuccess -> {
                    role = "ROLE_STAFF"
                    binding.etPhone.setText(state.data.staff.phone ?: "")
                    binding.etAddress.setText(state.data.staff.address ?: "")
                }
                is AccountProfileState.Error -> {
                    Toast.makeText(this, state.message, Toast.LENGTH_LONG).show()
                    finish()
                }
                else -> {}
            }
        }

        // Quan sát trạng thái cập nhật
//        profileViewModel.updateState.observe(this) { state ->
//            when (state) {
//                is UpdateProfileState.Loading -> {
//                    binding.progressBar.visibility = View.VISIBLE
//                }
//                is UpdateProfileState.Success -> {
//                    binding.progressBar.visibility = View.GONE
//                    Toast.makeText(this, "Cập nhật thông tin thành công!", Toast.LENGTH_SHORT).show()
//                    finish()
//                }
//                is UpdateProfileState.Error -> {
//                    binding.progressBar.visibility = View.GONE
//                    Toast.makeText(this, state.message, Toast.LENGTH_LONG).show()
//                }
//            }
//        }

        // Xử lý nút Lưu
//        binding.btnSave.setOnClickListener {
//            val phone = binding.etPhone.text.toString().trim().takeIf { it.isNotEmpty() }
//            val address = binding.etAddress.text.toString().trim().takeIf { it.isNotEmpty() }
//
//            when (role) {
//                "ROLE_STUDENT" -> profileViewModel.updateStudent(phone, address)
//                "ROLE_STAFF" -> profileViewModel.updateStaff(phone, address)
//                else -> Toast.makeText(this, "Không xác định được vai trò người dùng!", Toast.LENGTH_SHORT).show()
//            }
//        }

        // Xử lý nút Hủy
        binding.btnCancel.setOnClickListener {
            finish()
        }

        // Tải thông tin người dùng
        profileViewModel.loadUserInfo()
    }
}