package com.example.tlucontact_canhan

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tlucontact_canhan.databinding.FragmentStaffBinding
import com.example.tlucontact_canhan.model.Staff

class StaffFragment : Fragment() {
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

        val adapter = StaffAdapter(SampleData.staffs) { staff: Staff ->
            val intent = Intent(requireContext(), StaffDetailtActivity::class.java).apply {
                putExtra("staff_name", staff.name)
                putExtra("staff_phone", staff.phone)
                putExtra("staff_email", staff.email)
                putExtra("staff_unit", staff.unit)
                putExtra("staff_position", staff.position)
            }
            startActivity(intent)
        }

        binding.rvStaff.layoutManager = LinearLayoutManager(requireContext())
        binding.rvStaff.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}