package com.example.tlucontact_canhan.fragment

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tlucontact_canhan.UnitDetailActivity
import com.example.tlucontact_canhan.adapter.UnitAdapter
import com.example.tlucontact_canhan.data.SampleData
import com.example.tlucontact_canhan.databinding.FragmentUnitBinding
import com.example.tlucontact_canhan.model.ContactUnit
import com.example.tlucontact_canhan.model.UnitListItem

class UnitFragment : Fragment() {
    private var _binding: FragmentUnitBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: UnitAdapter
    private var isAscending = true // Trạng thái sắp xếp: true = A-Z, false = Z-A
    private var originalItems = listOf<UnitListItem>() // Danh sách gốc

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUnitBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        isAscending = true // Mặc định là A-Z

        // Khởi tạo danh sách gốc
        originalItems = groupUnitsByLetter(SampleData.units)
        adapter = UnitAdapter(originalItems) { unit ->
            val intent = Intent(requireContext(), UnitDetailActivity::class.java).apply {
                putExtra("unit_name", unit.contactUnit.name)
                putExtra("unit_phone", unit.contactUnit.phone)
                putExtra("unit_email", unit.contactUnit.email)
                putExtra("unit_address", unit.contactUnit.address)
                putExtra("unit_code", unit.contactUnit.code)
                putExtra("unit_fax", unit.contactUnit.fax)
            }
            startActivity(intent)
        }

        binding.rvUnits.layoutManager = LinearLayoutManager(requireContext())
        binding.rvUnits.adapter = adapter

        // Xử lý tìm kiếm
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                filterUnits(s.toString())
            }
        })

        // Xử lý nút sắp xếp
        binding.btnSort.setOnClickListener {
            isAscending = !isAscending
            binding.btnSort.setImageResource(
                if (isAscending) com.example.tlucontact_canhan.R.drawable.ic_sort_ascending
                else com.example.tlucontact_canhan.R.drawable.ic_sort_descending
            )
            sortUnits()
            Toast.makeText(requireContext(), "Đã sắp xếp ${if (isAscending) "A-Z" else "Z-A"}", Toast.LENGTH_SHORT).show()        }
    }

    private fun groupUnitsByLetter(units: List<ContactUnit>): List<UnitListItem> {
        val groupedItems = mutableListOf<UnitListItem>()    // List hỗ trợ việc tạo danh sách các mục
        var currentLetter = ""
        for (unit in units) {
            val firstLetter = unit.name.firstOrNull()?.uppercase() ?: ""
            if (firstLetter != currentLetter) {
                currentLetter = firstLetter
                groupedItems.add(UnitListItem.Header(currentLetter))    // Thêm tiêu đề mới vào danh sách
            }
            groupedItems.add(UnitListItem.Unit(unit))   // Thêm đơn vị vào danh sách
        }
        return groupedItems // Trả về danh sách đã nhóm, groupedItems có dạng [Header, Unit, Header, Unit]
    }

    private fun filterUnits(query: String) {
        val filteredUnits = if (query.isEmpty()) {
            SampleData.units
        } else {
            SampleData.units.filter {
                it.name.contains(query, ignoreCase = true) ||
                it.phone.contains(query) ||
                it.email!!.contains(query, ignoreCase = true)
            }
        }
        val newItems = groupUnitsByLetter(if (isAscending) filteredUnits.sortedBy { it.name } else filteredUnits.sortedByDescending { it.name })
        adapter.updateItems(newItems)
        originalItems = newItems
    }

    private fun sortUnits() {
        val currentUnits = originalItems.filterIsInstance<UnitListItem.Unit>().map { it.contactUnit }
        val sortedUnits = if (isAscending) {
            currentUnits.sortedBy { it.name }
        } else {
            currentUnits.sortedByDescending { it.name }
        }
        val newItems = groupUnitsByLetter(sortedUnits)
        adapter.updateItems(newItems)
        originalItems = newItems
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}