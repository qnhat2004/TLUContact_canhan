package com.example.tlucontact_canhan.repository

import android.content.Context
import com.example.tlucontact_canhan.model.PageResponse
import com.example.tlucontact_canhan.model.Staff
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

    suspend fun getAllStaffs(page: Int, size: Int, sort: String? = null, search: String? = null): Result<PageResponse<Staff>> {   // page: số trang hiện tại, size: số lượng sinh viên trên mỗi trang
        return try {
            val response = apiClient.getAllStaffs(page, size, sort, search)
            if (response.isSuccessful) {
                val staffs = response.body() ?: emptyList()
                // Lấy thông tin phân trang từ header
                val totalElements = response.headers().get("X-Total-Count")?.toLong() ?: 0L
                val totalPages = (totalElements / size + if (totalElements % size > 0) 1 else 0).toInt()
                val pageResponse = PageResponse(
                    content = staffs,
                    totalElements = totalElements,
                    totalPages = totalPages,
                    currentPage = page,
                    pageSize = size
                )
                Result.success(pageResponse)
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getStaffById(id: Long): Result<Staff> {
        return try {
            val response = apiClient.getStaffById(id)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}