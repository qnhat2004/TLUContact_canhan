package com.example.tlucontact_canhan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.tlucontact_canhan.databinding.FragmentStudentProfileBinding
import com.example.tlucontact_canhan.repository.AuthRepository

class StudentProfileFragment : Fragment() {
    private var _binding: FragmentStudentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var authRepository: AuthRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStudentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Khởi tạo AuthRepository
        authRepository = AuthRepository(requireContext())

        // Quan sát trạng thái lấy thông tin người dùng
        /*userViewModel.userState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UserState.Loading -> {
                    Toast.makeText(requireContext(), "Đang tải thông tin...", Toast.LENGTH_SHORT).show()
                }
                is UserState.Success -> {
                    val userInfo = state.userInfo
                    binding.tvName.text = userInfo.login
                    binding.tvEmail.text = userInfo.email
                    binding.tvStudentId.text = userInfo.studentId ?: "N/A"
                    binding.tvClass.text = userInfo.className ?: "N/A"
                    binding.tvPhone.text = userInfo.phone ?: "N/A"
                    binding.tvAddress.text = userInfo.address ?: "N/A"
                }
                is UserState.Error -> {
                    Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                    if (state.message.contains("Phiên đăng nhập hết hạn")) {
                        authRepository.clearToken()
                        startActivity(Intent(requireContext(), LoginActivity::class.java))
                        requireActivity().finish()
                    }
                }
            }
        }*/

        // Xử lý nút "Cập nhật thông tin cá nhân"
        binding.btnUpdateProfile.setOnClickListener {
            Toast.makeText(requireContext(), "Chức năng cập nhật thông tin đang phát triển", Toast.LENGTH_SHORT).show()
            // TODO: Chuyển đến màn hình cập nhật thông tin (UpdateProfileActivity)
        }

        // Xử lý nút "Đăng xuất"
        binding.btnLogout.setOnClickListener {
            authRepository.clearToken()
        }

        // Gọi API lấy thông tin người dùng
//        userViewModel.fetchUserInfo()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}