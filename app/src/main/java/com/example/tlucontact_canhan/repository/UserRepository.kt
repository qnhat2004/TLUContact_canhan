package com.example.tlucontact_canhan.repository

import android.content.Context
import com.example.tlucontact_canhan.model.Account
import com.example.tlucontact_canhan.network.ApiClient
import com.example.tlucontact_canhan.network.RetrofitClient

class UserRepository(private val context: Context) {
    private val apiClient: ApiClient = RetrofitClient.getApiClient(context)

    suspend fun getUserInfo(): Result<Account> {
        return try {
            val response = apiClient.getUserInfo()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}