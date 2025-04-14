package com.example.tlucontact_canhan

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.tlucontact_canhan.activity.LoginActivity
import com.example.tlucontact_canhan.activity.UnitDetailActivity
import com.example.tlucontact_canhan.activity.UpdateProfileActivity
import com.example.tlucontact_canhan.databinding.FragmentProfileBinding
import com.example.tlucontact_canhan.repository.AuthRepository
import com.example.tlucontact_canhan.repository.StaffRepository
import com.example.tlucontact_canhan.repository.StudentRepository
import com.example.tlucontact_canhan.viewmodel.AccountProfileState
import com.example.tlucontact_canhan.viewmodel.ProfileViewModel

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var authRepository: AuthRepository
    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Khởi tạo AuthRepository và các repository khác
        authRepository = AuthRepository(requireContext())
        val studentRepository = StudentRepository(requireContext())
        val staffRepository = StaffRepository(requireContext())

        // Khởi tạo ProfileViewModel
        profileViewModel = ViewModelProvider(
            this,
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
                        return ProfileViewModel(authRepository, studentRepository, staffRepository) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
        ).get(ProfileViewModel::class.java)

        // Quan sát trạng thái profile
        profileViewModel.profileState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is AccountProfileState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is AccountProfileState.StudentSuccess -> {
                    binding.progressBar.visibility = View.GONE
                    val accountStudent = state.data

                    // Hiển thị thông tin tài khoản
                    binding.tvName.text = accountStudent.student.fullName
                    binding.tvEmail.text = accountStudent.account.email

                    // Hiển thị layout cho sinh viên, ẩn layout cho giảng viên
                    binding.layoutStudentInfo.visibility = View.VISIBLE
                    binding.layoutStaffInfo.visibility = View.GONE

                    // Hiển thị thông tin sinh viên
                    binding.tvDetailtStudentId.text = accountStudent.student.studentId ?: "N/A"
                    binding.tvDetailtStudentPhone.text = accountStudent.student.phone ?: "N/A"
                    binding.tvDetailtStudentEmail.text = accountStudent.student.email ?: "N/A"
                    binding.tvDetailtStudentAddress.text = accountStudent.student.address ?: "N/A"
                    binding.tvDetailtStudentUnit.text = accountStudent.student.unit?.name ?: "N/A"

                    // Hiển thị ảnh đại diện của sinh viên
                    val avatarUrl = accountStudent.student.avatarUrl
                    if (avatarUrl != null) {
                        Glide.with(requireContext())
                            .load(avatarUrl)
                            .placeholder(R.drawable.avatar)
                            .into(binding.ivAvatar)
                    } else {
                        binding.ivAvatar.setImageResource(R.drawable.avatar)
                    }
                }
                is AccountProfileState.StaffSuccess -> {
                    binding.progressBar.visibility = View.GONE
                    val accountStaff = state.data

                    // Hiển thị thông tin tài khoản
                    binding.tvName.text = accountStaff.staff.fullName
                    binding.tvEmail.text = accountStaff.account.email

                    // Hiển thị layout cho giảng viên, ẩn layout cho sinh viên
                    binding.layoutStudentInfo.visibility = View.GONE
                    binding.layoutStaffInfo.visibility = View.VISIBLE

                    // Hiển thị thông tin giảng viên
                    binding.tvDetailtStaffCode.text = accountStaff.staff.staffId ?: "N/A"
                    binding.tvDetailtStaffPhone.text = accountStaff.staff.phone ?: "N/A"
                    binding.tvDetailtStaffEmail.text = accountStaff.staff.email ?: "N/A"
                    binding.tvDetailtStaffPosition.text = accountStaff.staff.position ?: "N/A"
                    binding.tvDetailtStaffEducation.text = accountStaff.staff.education ?: "N/A"
                    binding.tvDetailtStaffAddress.text = accountStaff.staff.address ?: "N/A"
                    binding.tvDetailtStaffUnit.text = accountStaff.staff.unit?.name ?: "N/A"

                    // Thêm sự kiện nhấn vào đơn vị của giảng viên để xem chi tiết đơn vị
                    binding.tvDetailtStaffUnit.setOnClickListener {
                        if (accountStaff.staff.unit != null) {
                            val intent = Intent(requireContext(), UnitDetailActivity::class.java).apply {
                                putExtra("unit_id", accountStaff.staff.unit!!.id)
                            }
                            startActivity(intent)
                        }
                    }

                    // Hiển thị ảnh đại diện của giảng viên (nếu có)
                    val avatarUrl = accountStaff.staff.avatarUrl
                    if (avatarUrl != null) {
                        Glide.with(requireContext())
                            .load(avatarUrl)
                            .placeholder(R.drawable.avatar)
                            .into(binding.ivAvatar)
                    } else {
                        binding.ivAvatar.setImageResource(R.drawable.avatar)
                    }
                }
                is AccountProfileState.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), state.message, Toast.LENGTH_LONG).show()
                    if (state.message.contains("Phiên đăng nhập hết hạn")) {
                        authRepository.clearToken()
                        startActivity(Intent(requireContext(), LoginActivity::class.java))
                        requireActivity().finish()
                    }
                }
            }
        }

        // Xử lý nút "Cập nhật thông tin cá nhân"
        binding.btnUpdateProfile.setOnClickListener {
            Toast.makeText(requireContext(), "Chức năng cập nhật thông tin đang phát triển", Toast.LENGTH_SHORT).show()
//            val intent = Intent(requireContext(), UpdateProfileActivity::class.java)
//            startActivity(intent)
        }

        // Xử lý nút "Đăng xuất"
        binding.btnLogout.setOnClickListener {
            authRepository.clearToken()
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            requireActivity().finish()
        }

        // Gọi API lấy thông tin người dùng
        profileViewModel.loadUserInfo()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}