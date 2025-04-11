package com.example.tlucontact_canhan.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tlucontact_canhan.R
import com.example.tlucontact_canhan.model.UnitListItem

class ContactUnitAdapter(
    private var items: List<UnitListItem>,
    private val onItemClick: (UnitListItem.Unit_) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_UNIT = 1
    }

    class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvHeader: TextView = itemView.findViewById(R.id.tvHeader)
    }

    class UnitViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvUnitName: TextView = itemView.findViewById(R.id.tvUnitName)
        val tvUnitCode: TextView = itemView.findViewById(R.id.tvUnitCode)
        val tvUnitEmail: TextView = itemView.findViewById(R.id.tvUnitEmail)
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
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.header_item, parent, false)
                HeaderViewHolder(view)
            }
            TYPE_UNIT -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.unit_item, parent, false)
                UnitViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = items[position]) {
            is UnitListItem.Header -> {
                val headerHolder = holder as HeaderViewHolder
                headerHolder.tvHeader.text = item.letter
            }
            is UnitListItem.Unit_ -> {
                val unitHolder = holder as UnitViewHolder
                unitHolder.tvUnitName.text = item.contactUnit.name
                unitHolder.tvUnitCode.text = item.contactUnit.unitCode
                unitHolder.tvUnitEmail.text = item.contactUnit.email
                unitHolder.itemView.setOnClickListener {
                    onItemClick(item)
                }
            }
        }
    }

    override fun getItemCount(): Int = items.size

    fun updateItems(newItems: List<UnitListItem>) {
        items = newItems
        notifyDataSetChanged()
    }
}