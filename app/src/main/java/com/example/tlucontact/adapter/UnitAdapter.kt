package com.example.tlucontact.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tlucontact.model.UnitListItem
import com.example.tlucontact_canhan.databinding.HeaderItemBinding
import com.example.tlucontact_canhan.databinding.UnitItemBinding

class UnitAdapter(
    private var items: List<UnitListItem>,
    private val onClick: (UnitListItem.Unit) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_UNIT = 1
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is UnitListItem.Header -> TYPE_HEADER
            is UnitListItem.Unit -> TYPE_UNIT
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_HEADER -> {
                val binding = HeaderItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                HeaderViewHolder(binding)
            }
            else -> {
                val binding = UnitItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                UnitViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = items[position]) {
            is UnitListItem.Header -> (holder as HeaderViewHolder).bind(item)
            is UnitListItem.Unit -> {
                (holder as UnitViewHolder).bind(item)
                holder.itemView.setOnClickListener { onClick(item) }
            }
        }
    }

    override fun getItemCount() = items.size

    fun updateItems(newItems: List<UnitListItem>) {
        items = newItems
        notifyDataSetChanged()
    }

    class HeaderViewHolder(private val binding: HeaderItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(header: UnitListItem.Header) {
            binding.tvHeader.text = header.letter
        }
    }

    class UnitViewHolder(private val binding: UnitItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(unit: UnitListItem.Unit) {
            binding.tvUnitName.text = unit.contactUnit.name
            binding.tvUnitPhone.text = unit.contactUnit.phone
            binding.tvUnitEmail.text = unit.contactUnit.email
        }
    }
}