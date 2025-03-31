package com.example.tlucontact.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.tlucontact.network.ApiClient
import com.example.tlucontact.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

class UserViewModel : ViewModel() {
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun getUserInfo(context: Context) = liveData(Dispatchers.IO) {
        try {
            val response = RetrofitClient.getInstance(context).create(ApiClient::class.java).getUserInfo().execute()
            if (response.isSuccessful) {
                Log.d("UserViewModel", response.body().toString())
                emit(response.body())
            } else {
                _error.postValue("Error: ${response.message()}")
                emit(null)
            }
        } catch (e: IOException) {
            _error.postValue("Network error: ${e.message}")
        } catch (e: HttpException) {
            _error.postValue("HTTP error: ${e.message}")
        } catch (e: Exception) {
            _error.postValue("Unexpected error: ${e.message}")
        } catch (e: SocketTimeoutException) {
            _error.postValue("Timeout error: ${e.message}")
            Toast.makeText(context, "Timeout error: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}