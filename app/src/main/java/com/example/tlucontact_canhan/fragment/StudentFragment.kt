package com.example.tlucontact.fragment

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tlucontact.adapter.StudentAdapter
import com.example.tlucontact_canhan.R
import com.example.tlucontact_canhan.activity.StudentDetailActivity
import com.example.tlucontact_canhan.databinding.FragmentStudentBinding
import com.example.tlucontact_canhan.model.StudentListItem
import com.example.tlucontact_canhan.repository.AuthRepository
import com.example.tlucontact_canhan.repository.StudentRepository
import com.example.tlucontact_canhan.viewmodel.StudentViewModel
import com.example.tlucontact_canhan.viewmodel.StudentViewModelFactory
import kotlinx.coroutines.runBlocking

class StudentFragment : Fragment() {
    private lateinit var viewModel: StudentViewModel
    private lateinit var studentAdapter: StudentAdapter
    private lateinit var sortDirection: String
    private var _binding: FragmentStudentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStudentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sortDirection = "asc" // Default sort direction

        // Khởi tạo authRepository
        val authRepository = AuthRepository(requireContext())

        // Kiểm tra vai trò người dùng
        val userRole = runBlocking { authRepository.getAccountInfo().getOrNull()?.authorities?.contains("ROLE_USER") }
        Log.d("StudentFragment", "User role: $userRole")

        if (userRole == null) {
            Toast.makeText(context, "Không thể xác định vai trò người dùng!", Toast.LENGTH_LONG).show()
            requireActivity().finish()
            return
        }

        // Initialize ViewModel
        viewModel = ViewModelProvider(
            this,
            StudentViewModelFactory(StudentRepository(requireContext()))
        ).get(StudentViewModel::class.java)

        // Initialize RecyclerView and Adapter
        studentAdapter = StudentAdapter(emptyList()) { student ->
            // Handle student click
            val intent = Intent(requireContext(), StudentDetailActivity::class.java).apply {
                putExtra("student_id", student.Student.studentId)
                putExtra("student_fullName", student.Student.fullName)
                putExtra("student_phone", student.Student.phone)
                putExtra("student_email", student.Student.email)
                putExtra("student_address", student.Student.address)
                putExtra("student_unit", student.Student.unit?.id?.toString() ?: "Không có thông tin")
                putExtra("student_avatarUrl", student.Student.avatarUrl)
            }
            startActivity(intent) // Start the activity
        }

        binding.rvStudent.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = studentAdapter
        }

        // Observe student list
        viewModel.students.observe(viewLifecycleOwner) { students ->
            studentAdapter.updateItems(students)
            binding.rvStudent.scrollToPosition(0)
            Log.d("StudentFragment", "Student list updated: ${students.size} items")
        }

        // Observe loading state
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.swipeRefreshLayout.isRefreshing = isLoading
        }

        // Observe error
        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                if (it.contains("Không thể xác định lớp của bạn")) {
                    // Nếu không lấy được classId, không hiển thị danh sách và thoát
                    studentAdapter.updateItems(emptyList())
                    binding.rvStudent.visibility = View.GONE
                }
            }
        }

        // Set swipe-to-refresh listener
        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.etStudentSearch.setText("") // Xóa query tìm kiếm khi làm mới
            viewModel.loadFirstPage(sortField = "fullName", sortDirection = sortDirection)
        }

        // Handle scrolling to load more data
        binding.rvStudent.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val totalItemCount = layoutManager.itemCount
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()

                if (lastVisibleItem >= totalItemCount - 5 && viewModel.hasMoreData.value == true) {
                    viewModel.loadNextPage()
                }
            }
        })

        // Handle search
        binding.etStudentSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                viewModel.searchStudents(s.toString())
            }
        })

        // Load the first page
        viewModel.loadFirstPage(sortField = "fullName", sortDirection = sortDirection)

        // Handle sort button click
        binding.btnSort.setOnClickListener {
            sortDirection = if (sortDirection == "asc") "desc" else "asc"
            binding.btnSort.setImageResource(
                if (sortDirection == "asc") R.drawable.ic_sort_ascending
                else R.drawable.ic_sort_descending
            )
            viewModel.loadFirstPage(sortField = "fullName", sortDirection = sortDirection)
        }

        // Cập nhật icon ban đầu của nút
        binding.btnSort.setImageResource(R.drawable.ic_sort_ascending)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}