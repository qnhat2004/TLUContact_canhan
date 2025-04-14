package com.example.tlucontact.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tlucontact_canhan.databinding.HeaderItemBinding
import com.example.tlucontact_canhan.databinding.UnitItemBinding
import com.example.tlucontact_canhan.model.UnitListItem

class ContactUnitAdapter(
    private var items: List<UnitListItem>,
    private val onClick: (UnitListItem.Unit_) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_UNIT = 1
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is UnitListItem.Header -> TYPE_HEADER
            is UnitListItem.Unit_ -> TYPE_UNIT
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
                val binding = UnitItemBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                UnitViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = items[position]) {
            is UnitListItem.Header -> (holder as HeaderViewHolder).bind(item)
            is UnitListItem.Unit_ -> {
                (holder as UnitViewHolder).bind(item)
                holder.itemView.setOnClickListener { onClick(item) }
            }
        }
    }

    override fun getItemCount(): Int = items.size

    fun updateItems(newItems: List<UnitListItem>) {
        val diffResult = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getOldListSize(): Int = items.size
            override fun getNewListSize(): Int = newItems.size
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                val oldItem = items[oldItemPosition]
                val newItem = newItems[newItemPosition]
                return when {
                    oldItem is UnitListItem.Header && newItem is UnitListItem.Header ->
                        oldItem.letter == newItem.letter
                    oldItem is UnitListItem.Unit_ && newItem is UnitListItem.Unit_ ->
                        oldItem.contactUnit.id == newItem.contactUnit.id
                    else -> false
                }
            }
            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                val oldItem = items[oldItemPosition]
                val newItem = newItems[newItemPosition]
                return when {
                    oldItem is UnitListItem.Header && newItem is UnitListItem.Header ->
                        oldItem.letter == newItem.letter
                    oldItem is UnitListItem.Unit_ && newItem is UnitListItem.Unit_ ->
                        oldItem.contactUnit == newItem.contactUnit
                    else -> false
                }
            }
        })
        items = newItems
        diffResult.dispatchUpdatesTo(this)
    }

    class HeaderViewHolder(private val binding: HeaderItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(header: UnitListItem.Header) {
            binding.tvHeader.text = header.letter
        }
    }

    class UnitViewHolder(private val binding: UnitItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(unit: UnitListItem.Unit_) {
            binding.tvUnitName.text = unit.contactUnit.name
            binding.tvUnitCode.text = unit.contactUnit.unitCode
            binding.tvUnitEmail.text = unit.contactUnit.email
            // Set image
            Glide.with(binding.ivUnitLogo.context)
                .load(unit.contactUnit.logoUrl)
                .into(binding.ivUnitLogo)
        }
    }
}