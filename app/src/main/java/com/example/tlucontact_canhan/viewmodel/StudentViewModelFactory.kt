package com.example.tlucontact_canhan.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tlucontact_canhan.repository.AuthRepository
import com.example.tlucontact_canhan.repository.StudentRepository

class StudentViewModelFactory(
    private val studentRepository: StudentRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StudentViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StudentViewModel(studentRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}