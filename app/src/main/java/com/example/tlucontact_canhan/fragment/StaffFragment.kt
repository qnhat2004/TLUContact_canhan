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
import com.example.tlucontact.adapter.StaffAdapter
import com.example.tlucontact_canhan.R
import com.example.tlucontact_canhan.activity.StaffDetailActivity
import com.example.tlucontact_canhan.databinding.FragmentStaffBinding
import com.example.tlucontact_canhan.model.StaffListItem
import com.example.tlucontact_canhan.repository.StaffRepository
import com.example.tlucontact_canhan.viewmodel.StaffViewModel
import com.example.tlucontact_canhan.viewmodel.StaffViewModelFactory

class StaffFragment : Fragment() {
    private lateinit var viewModel: StaffViewModel
    private lateinit var staffAdapter: StaffAdapter
    private lateinit var sortDirection: String
    private var _binding: FragmentStaffBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStaffBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sortDirection = "asc" // Default sort direction

        // Initialize ViewModel
        viewModel = ViewModelProvider(this, StaffViewModelFactory(StaffRepository(requireContext())))
            .get(StaffViewModel::class.java)

        // Initialize RecyclerView and Adapter
        staffAdapter = StaffAdapter(emptyList()) { staff ->
            // Handle staff click
            val intent = Intent(requireContext(), StaffDetailActivity::class.java).apply {
                putExtra("staff_id", staff.staff.staffId)
                putExtra("staff_fullName", staff.staff.fullName)
                putExtra("staff_phone", staff.staff.phone)
                putExtra("staff_email", staff.staff.email)
                putExtra("staff_address", staff.staff.address)
                putExtra("staff_position", staff.staff.position)
                putExtra("staff_education", staff.staff.education)
                putExtra("staff_unitId", staff.staff.unit.id)
                putExtra("staff_unitName", staff.staff.unit.name)
                putExtra("staff_avatarUrl", staff.staff.avatarUrl)
            }
            startActivity(intent) // Start the activity
        }

        binding.rvStaff.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = staffAdapter
        }

        // Observe staff list
        viewModel.staffs.observe(viewLifecycleOwner) { staffs ->
            staffAdapter.updateItems(staffs)
            binding.rvStaff.scrollToPosition(0)
        }

        // Observe loading state
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.swipeRefreshLayout.isRefreshing = isLoading
        }

        // Observe error
        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        }

        // Set swipe-to-refresh listener
        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.etStaffSearch.setText("") // Xóa query tìm kiếm khi làm mới
            viewModel.loadFirstPage(sortField = "fullName", sortDirection = sortDirection)
        }

        // Handle scrolling to load more data
        binding.rvStaff.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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
        binding.etStaffSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                viewModel.searchStaffs(s.toString())
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