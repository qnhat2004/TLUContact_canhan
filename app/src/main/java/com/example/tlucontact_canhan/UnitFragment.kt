package com.example.tlucontact_canhan

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tlucontact_canhan.databinding.FragmentUnitBinding
import com.example.tlucontact_canhan.model.ContactUnit

class UnitFragment : Fragment() {
    private var _binding: FragmentUnitBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUnitBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("UnitFragment", "onViewCreated: ${SampleData.units.size} units loaded, units: ${SampleData.units}")
        val adapter = UnitAdapter(SampleData.units) { unit: ContactUnit ->
            val intent = Intent(requireContext(), UnitDetailActivity::class.java).apply {
                putExtra("unit_name", unit.name)
                putExtra("unit_phone", unit.phone)
                putExtra("unit_address", unit.address)
            }
            startActivity(intent)
        }

        binding.rvUnits.layoutManager = LinearLayoutManager(requireContext())
        binding.rvUnits.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}