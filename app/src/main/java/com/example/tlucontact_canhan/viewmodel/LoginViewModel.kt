package com.example.tlucontact_canhan.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tlucontact_canhan.model.LoginRequest
import com.example.tlucontact_canhan.repository.AuthRepository
import kotlinx.coroutines.launch
import androidx.lifecycle.viewModelScope

sealed class LoginState<out T> {
    object Loading : LoginState<Nothing>()
    data class Success<T>(val data: T) : LoginState<T>()
    data class Error(val exception: Throwable) : LoginState<Nothing>()
}

class LoginViewModel(private val authRepository: AuthRepository) : ViewModel() {
    private val _loginResult = MutableLiveData<LoginState<String>>()
    val loginResult: LiveData<LoginState<String>> get() = _loginResult

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _loginResult.value = LoginState.Loading
            val loginRequest = LoginRequest(username, password)
            val result = authRepository.login(loginRequest)
            _loginResult.value = if (result.isSuccess) {
                LoginState.Success(result.getOrNull()!!.id_token)
            } else {
                Log.e("LoginViewModel", "Login failed: ${result.exceptionOrNull()?.message}")
                LoginState.Error(result.exceptionOrNull()!!)
            }
        }
    }
}