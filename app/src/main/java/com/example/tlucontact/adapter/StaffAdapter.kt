package com.example.tlucontact.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tlucontact.model.Staff
import com.example.tlucontact.model.StaffListItem
import com.example.tlucontact_canhan.databinding.HeaderItemBinding
import com.example.tlucontact_canhan.databinding.StaffItemBinding

class StaffAdapter(
    private var items: List<StaffListItem>,
    private val onClick: (StaffListItem.Staff_) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_STAFF = 1
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is StaffListItem.Header -> TYPE_HEADER
            is StaffListItem.Staff_ -> TYPE_STAFF
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_HEADER -> {
                val binding = HeaderItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                HeaderViewHolder(binding)
            }
            TYPE_STAFF -> {
                val binding = StaffItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                StaffViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = items[position]) {
            is StaffListItem.Header -> (holder as HeaderViewHolder).bind(item)
            is StaffListItem.Staff_ -> {
                (holder as StaffViewHolder).bind(item.staff)
                holder.itemView.setOnClickListener { onClick(item) }
            }
        }
    }

    override fun getItemCount() = items.size

    fun updateItems(newItems: List<StaffListItem>) {
        items = newItems
        notifyDataSetChanged()
    }

    class HeaderViewHolder(private val binding: HeaderItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(header: StaffListItem.Header) {
            binding.tvHeader.text = header.letter
        }
    }

    class StaffViewHolder(private val binding: StaffItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(staff: Staff) {
            binding.tvStaffName.text = staff.name
            binding.tvStaffPhone.text = staff.phone
            binding.tvStaffEmail.text = staff.email
        }
    }
}