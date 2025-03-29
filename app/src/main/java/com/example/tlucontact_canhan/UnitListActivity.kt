package com.example.tlucontact_canhan

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tlucontact_canhan.databinding.ActivityUnitListBinding
import com.example.tlucontact_canhan.model.ContactUnit

class UnitListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUnitListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUnitListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = UnitAdapter(SampleData.units) { unit: ContactUnit ->
            val intent = Intent(this, UnitDetailActivity::class.java).apply {
                putExtra("unit_name", unit.name)
                putExtra("unit_phone", unit.phone)
                putExtra("unit_address", unit.address)
            }
            startActivity(intent)
        }

        binding.rvUnits.layoutManager = LinearLayoutManager(this)
        binding.rvUnits.adapter = adapter
    }
}