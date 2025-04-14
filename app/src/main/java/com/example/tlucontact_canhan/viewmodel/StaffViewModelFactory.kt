package com.example.tlucontact_canhan.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tlucontact_canhan.repository.StaffRepository

class StaffViewModelFactory(private val repository: StaffRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StaffViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StaffViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}