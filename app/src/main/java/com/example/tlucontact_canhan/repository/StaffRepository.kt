package com.example.tlucontact_canhan.repository

import android.content.Context
import com.example.tlucontact_canhan.model.Staff
import com.example.tlucontact_canhan.model.Student
import com.example.tlucontact_canhan.network.ApiClient
import com.example.tlucontact_canhan.network.RetrofitClient

class StaffRepository(private val context: Context) {
    private val apiClient: ApiClient = RetrofitClient.getApiClient(context)

    suspend fun getStaffByUserId(id: Long): Result<Staff> {
        return try {
            val response = apiClient.getStaffByUserId(id)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}