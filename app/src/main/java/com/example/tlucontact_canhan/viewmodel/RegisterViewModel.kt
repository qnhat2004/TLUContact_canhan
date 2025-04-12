package com.example.tlucontact_canhan.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tlucontact_canhan.model.RegisterRequest
import com.example.tlucontact_canhan.repository.AuthRepository
import kotlinx.coroutines.launch
import androidx.lifecycle.viewModelScope

sealed class RegisterState<out T> {
    object Loading : RegisterState<Nothing>()
    data class Success<T>(val data: T) : RegisterState<T>()
    data class Error(val exception: Throwable) : RegisterState<Nothing>()
}

class RegisterViewModel(private val authRepository: AuthRepository) : ViewModel() {
    private val _registerResult = MutableLiveData<RegisterState<String>>()
    val registerResult: LiveData<RegisterState<String>> get() = _registerResult

    fun register(username: String, password: String) {
        viewModelScope.launch {
            _registerResult.value = RegisterState.Loading
            val registerRequest = RegisterRequest(username, password)
            val result = authRepository.register(registerRequest)
            _registerResult.value = if (result.isSuccess) {
                RegisterState.Success(result.getOrNull()!!.id_token)
            } else {
                Log.e("RegisterViewModel", "Register failed: ${result.exceptionOrNull()?.message}")
                RegisterState.Error(result.exceptionOrNull()!!)
            }
        }
    }
}