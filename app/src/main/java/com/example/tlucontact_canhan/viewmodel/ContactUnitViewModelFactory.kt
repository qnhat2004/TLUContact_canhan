package com.example.tlucontact_canhan.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tlucontact_canhan.repository.ContactUnitRepository

class ContactUnitViewModelFactory(private val repository: ContactUnitRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ContactUnitViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ContactUnitViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}