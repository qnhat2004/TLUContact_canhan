package com.example.tlucontact_canhan

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tlucontact_canhan.databinding.UnitItemBinding
import com.example.tlucontact_canhan.model.ContactUnit

class UnitAdapter(private val units: List<ContactUnit>, private val onClick: (ContactUnit) -> Unit) :
    RecyclerView.Adapter<UnitAdapter.UnitViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UnitViewHolder {
        val binding = UnitItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UnitViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UnitViewHolder, position: Int) {
        // Log the position and unit name for debugging
        Log.d("UnitAdapter", "onBindViewHolder: position = $position, unit = ${units[position]}")
        val unit = units[position]
        holder.bind(unit)
        holder.itemView.setOnClickListener { onClick(unit) }
    }

    override fun getItemCount() = units.size

    class UnitViewHolder(private val binding: UnitItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(unit: ContactUnit) {
            binding.tvUnitName.text = unit.name
            binding.tvUnitPhone.text = unit.phone
        }
    }
}