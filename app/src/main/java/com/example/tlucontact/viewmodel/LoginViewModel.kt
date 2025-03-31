package com.example.tlucontact.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.tlucontact.model.LoginRequest
import com.example.tlucontact.network.ApiClient
import com.example.tlucontact.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import retrofit2.HttpException
import java.io.IOException

class LoginViewModel : ViewModel() {
    fun login(context: Context, username: String, password: String) = liveData<Result<String>>(Dispatchers.IO) {
        try {
            // call login method with LoginRequest object
            val response = RetrofitClient.getInstance(context)  // get instance of RetrofitClient with context to get SharedPreferences
                .create(ApiClient::class.java)     // create ApiService interface
                .login(LoginRequest(username, password))    // call login method with LoginRequest object
                .execute()

            if (response.isSuccessful) {
                val token = response.body()?.id_token
                if (token != null) {
                    val sharedPref = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                    with(sharedPref.edit()) {
                        putString("auth_token", token)
                        putString("email", username)
                        putString("password", password)
                        apply()
                    }
                    emit(Result.success(token))
                } else {
                    emit(Result.failure<String>(Exception("Token is null")))
                }
            } else {
                emit(Result.failure<String>(HttpException(response)))
            }
        } catch (e: IOException) {
            emit(Result.failure<String>(e))
        } catch (e: HttpException) {
            emit(Result.failure<String>(e))
        }
    }
}