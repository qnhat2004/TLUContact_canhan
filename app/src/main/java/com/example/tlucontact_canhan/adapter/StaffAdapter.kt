package com.example.tlucontact_canhan.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tlucontact_canhan.databinding.StaffItemBinding
import com.example.tlucontact_canhan.model.Staff

class StaffAdapter(private val staffs: List<Staff>, private val onClick: (Staff) -> Unit) :
    RecyclerView.Adapter<StaffAdapter.StaffViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StaffViewHolder {
        val binding = StaffItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StaffViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StaffViewHolder, position: Int) {
        val staff = staffs[position]
        holder.bind(staff)
        holder.itemView.setOnClickListener { onClick(staff) }
    }

    override fun getItemCount() = staffs.size

    class StaffViewHolder(private val binding: StaffItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(staff: Staff) {
            binding.tvStaffName.text = staff.name
            binding.tvStaffPhone.text = staff.phone
        }
    }
}