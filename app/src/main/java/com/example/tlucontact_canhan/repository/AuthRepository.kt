package com.example.tlucontact_canhan.repository

import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.tlucontact_canhan.activity.LoginActivity
import com.example.tlucontact_canhan.model.Account
import com.example.tlucontact_canhan.model.LoginRequest
import com.example.tlucontact_canhan.model.LoginResponse
import com.example.tlucontact_canhan.model.RegisterRequest
import com.example.tlucontact_canhan.model.RegisterResponse
import com.example.tlucontact_canhan.network.ApiClient
import com.example.tlucontact_canhan.network.RetrofitClient

// This class handles authentication-related operations such as login, token storage, and token retrieval.
class AuthRepository(private val context: Context) {
    private val sharedPreferences = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    private val apiClient: ApiClient = RetrofitClient.getApiClient(context)

    suspend fun login(loginRequest: LoginRequest): Result<LoginResponse> {
        return try {
            val response = apiClient.login(loginRequest)
            // Lưu token
            sharedPreferences.edit()
                .putString("jwt_token", response.id_token)
                .apply()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getToken(): String? {
        return sharedPreferences.getString("jwt_token", null)
    }

    fun saveToken(token: String) {
        sharedPreferences.edit()
            .putString("jwt_token", token)
            .apply()
    }

    fun clearToken() {
        sharedPreferences.edit()
            .remove("jwt_token")
            .apply()
        val intent = Intent(context, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        context.startActivity(intent)
    }

    suspend fun getAccountInfo(): Result<Account> {
        return try {
            val response = apiClient.getUserInfo()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(registerRequest: RegisterRequest): Result<RegisterResponse> {
        return try {
            val response = apiClient.register(registerRequest)
            // Lưu token
            sharedPreferences.edit()
                .putString("jwt_token", response.id_token)
                .apply()
            Result.success(response)
        } catch (e: Exception) {
            Log.e("AuthRepository", "Register failed: ${e.message}")
            Result.failure(e)
        }
    }
}