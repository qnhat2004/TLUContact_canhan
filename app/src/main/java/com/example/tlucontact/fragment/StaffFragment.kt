package com.example.tlucontact.fragment

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tlucontact.data.SampleData
import com.example.tlucontact.activity.StaffDetailtActivity
import com.example.tlucontact.adapter.StaffAdapter
import com.example.tlucontact.model.Staff
import com.example.tlucontact.model.StaffListItem
import com.example.tlucontact_canhan.R
import com.example.tlucontact_canhan.databinding.FragmentStaffBinding

class StaffFragment : Fragment() {
    private var _binding: FragmentStaffBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: StaffAdapter
    private var isAscending = true // Trạng thái sắp xếp: true = A-Z, false = Z-A
    private var originalItems = listOf<StaffListItem>() // Danh sách gốc

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStaffBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        isAscending = true // Mặc định là A-Z

        originalItems = groupStaffsByLetter(SampleData.staffs)
        adapter = StaffAdapter(originalItems) { staff ->
            val intent = Intent(requireContext(), StaffDetailtActivity::class.java).apply {
                putExtra("staff_name", staff.staff.name)
                putExtra("staff_phone", staff.staff.phone)
                putExtra("staff_email", staff.staff.email)
                putExtra("staff_unit", staff.staff.unit)
                putExtra("staff_position", staff.staff.position)
            }
            startActivity(intent)
        }

        binding.rvStaff.layoutManager = LinearLayoutManager(requireContext())
        binding.rvStaff.adapter = adapter

        // Xử lý tìm kiếm
        binding.etStaffSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                filterStaffs(s.toString())
            }
        })

        // Xử lý sắp xếp
        binding.btnSort.setOnClickListener {
            isAscending = !isAscending
            binding.btnSort.setImageResource(
                if (isAscending) R.drawable.ic_sort_ascending
                else R.drawable.ic_sort_descending
            )
            sortStaffs()
        }
    }

    private fun groupStaffsByLetter(staffs: List<Staff>): List<StaffListItem> {
        val groupedItems = mutableListOf<StaffListItem>()    // List hỗ trợ việc tạo danh sách các mục
        var currentLetter = ""
        for (staff in staffs) {
            val firstLetter = staff.name.firstOrNull()?.uppercase() ?: ""
            if (firstLetter != currentLetter) {
                currentLetter = firstLetter
                groupedItems.add(StaffListItem.Header(currentLetter))    // Thêm tiêu đề mới vào danh sách
            }
            groupedItems.add(StaffListItem.Staff_(staff))   // Thêm cbnv vào danh sách
        }
        return groupedItems // Trả về danh sách đã nhóm, groupedItems có dạng [Header, Unit, Header, Unit]
    }

    private fun filterStaffs(query: String) {
        val filterdStaffs = if (query.isEmpty()) {
            SampleData.staffs
        } else {
            SampleData.staffs.filter {
                it.name.contains(query, ignoreCase = true) ||
                        it.phone.contains(query, ignoreCase = true) ||
                        it.email.contains(query, ignoreCase = true) ||
                        it.unit.contains(query, ignoreCase = true) ||
                        it.position.contains(query, ignoreCase = true)
            }
        }
        val groupedItems = groupStaffsByLetter(if (isAscending) filterdStaffs.sortedBy { it.name } else filterdStaffs.sortedByDescending { it.name })
        adapter.updateItems(groupedItems)
        originalItems = groupedItems
    }

    private fun sortStaffs() {
        val currentStaffs = originalItems.filterIsInstance<StaffListItem.Staff_>().map { it.staff }
        val sortedStaffs = if (isAscending) {
            currentStaffs.sortedBy { it.name }
        } else {
            currentStaffs.sortedByDescending { it.name }
        }
        val newItems = groupStaffsByLetter(sortedStaffs)
        adapter.updateItems(newItems)
        originalItems = newItems
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}