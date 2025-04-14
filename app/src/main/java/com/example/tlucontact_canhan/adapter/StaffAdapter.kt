package com.example.tlucontact.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tlucontact_canhan.databinding.HeaderItemBinding
import com.example.tlucontact_canhan.databinding.StaffItemBinding
import com.example.tlucontact_canhan.model.StaffListItem

class StaffAdapter(
    private var items: List<StaffListItem>,
    private val onClick: (StaffListItem.Staff_) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_UNIT = 1
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is StaffListItem.Header -> TYPE_HEADER
            is StaffListItem.Staff_ -> TYPE_UNIT
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_HEADER -> {
                val binding = HeaderItemBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                HeaderViewHolder(binding)
            }

            else -> {
                val binding = StaffItemBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                StaffViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = items[position]) {
            is StaffListItem.Header -> (holder as HeaderViewHolder).bind(item)
            is StaffListItem.Staff_ -> {
                (holder as StaffViewHolder).bind(item)
                holder.itemView.setOnClickListener { onClick(item) }
            }
        }
    }

    override fun getItemCount(): Int = items.size

    fun updateItems(newItems: List<StaffListItem>) {
        val diffResult = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getOldListSize(): Int = items.size
            override fun getNewListSize(): Int = newItems.size
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                val oldItem = items[oldItemPosition]
                val newItem = newItems[newItemPosition]
                return when {
                    oldItem is StaffListItem.Header && newItem is StaffListItem.Header ->
                        oldItem.letter == newItem.letter
                    oldItem is StaffListItem.Staff_ && newItem is StaffListItem.Staff_ ->
                        oldItem.staff.id == newItem.staff.id
                    else -> false
                }
            }
            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                val oldItem = items[oldItemPosition]
                val newItem = newItems[newItemPosition]
                return when {
                    oldItem is StaffListItem.Header && newItem is StaffListItem.Header ->
                        oldItem.letter == newItem.letter
                    oldItem is StaffListItem.Staff_ && newItem is StaffListItem.Staff_ ->
                        oldItem.staff == newItem.staff
                    else -> false
                }
            }
        })
        items = newItems
        diffResult.dispatchUpdatesTo(this)
    }

    class HeaderViewHolder(private val binding: HeaderItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(header: StaffListItem.Header) {
            binding.tvHeader.text = header.letter
        }
    }

    class StaffViewHolder(private val binding: StaffItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(staff: StaffListItem.Staff_) {
            binding.tvStaffName.text = staff.staff.fullName
            binding.tvStaffPhone.text = staff.staff.phone
            binding.tvStaffEmail.text = staff.staff.email
            Glide.with(binding.ivStaffAvatar.context)
                .load(staff.staff.avatarUrl)
                .into(binding.ivStaffAvatar)
        }
    }
}