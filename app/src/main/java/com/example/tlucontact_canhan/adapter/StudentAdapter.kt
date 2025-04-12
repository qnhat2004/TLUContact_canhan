package com.example.tlucontact.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tlucontact_canhan.databinding.HeaderItemBinding
import com.example.tlucontact_canhan.databinding.StudentItemBinding
import com.example.tlucontact_canhan.model.StudentListItem

class StudentAdapter(
    private var items: List<StudentListItem>,
    private val onClick: (StudentListItem.Student_) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_UNIT = 1
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is StudentListItem.Header -> TYPE_HEADER
            is StudentListItem.Student_ -> TYPE_UNIT
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
                val binding = StudentItemBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                StudentViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = items[position]) {
            is StudentListItem.Header -> (holder as HeaderViewHolder).bind(item)
            is StudentListItem.Student_ -> {
                (holder as StudentViewHolder).bind(item)
                holder.itemView.setOnClickListener { onClick(item) }
            }
        }
    }

    override fun getItemCount(): Int = items.size

    fun updateItems(newItems: List<StudentListItem>) {
        val diffResult = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getOldListSize(): Int = items.size
            override fun getNewListSize(): Int = newItems.size
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                val oldItem = items[oldItemPosition]
                val newItem = newItems[newItemPosition]
                return when {
                    oldItem is StudentListItem.Header && newItem is StudentListItem.Header ->
                        oldItem.letter == newItem.letter
                    oldItem is StudentListItem.Student_ && newItem is StudentListItem.Student_ ->
                        oldItem.Student.id == newItem.Student.id
                    else -> false
                }
            }
            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                val oldItem = items[oldItemPosition]
                val newItem = newItems[newItemPosition]
                return when {
                    oldItem is StudentListItem.Header && newItem is StudentListItem.Header ->
                        oldItem.letter == newItem.letter
                    oldItem is StudentListItem.Student_ && newItem is StudentListItem.Student_ ->
                        oldItem.Student == newItem.Student
                    else -> false
                }
            }
        })
        items = newItems
        diffResult.dispatchUpdatesTo(this)
    }

    class HeaderViewHolder(private val binding: HeaderItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(header: StudentListItem.Header) {
            binding.tvHeader.text = header.letter
        }
    }

    class StudentViewHolder(private val binding: StudentItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(student: StudentListItem.Student_) {
            binding.tvStudentName.text = student.Student.fullName
            binding.tvStudentPhone.text = student.Student.phone
            binding.tvStudentEmail.text = student.Student.email
            // Set image
            Glide.with(binding.ivStudentAvatar.context)
                .load(student.Student.avatarUrl)
                .into(binding.ivStudentAvatar)
        }
    }
}