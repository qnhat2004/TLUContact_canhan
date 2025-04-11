package com.example.tlucontact_canhan.repository

import android.content.Context
import com.example.tlucontact_canhan.model.Account
import com.example.tlucontact_canhan.model.PageResponse
import com.example.tlucontact_canhan.model.Student
import com.example.tlucontact_canhan.network.ApiClient
import com.example.tlucontact_canhan.network.RetrofitClient

class StudentRepository(private val context: Context) {
    private val apiClient: ApiClient = RetrofitClient.getApiClient(context)

    suspend fun getAllStudents(page: Int, size: Int, sort: String? = null, search: String? = null): Result<PageResponse<Student>> {   // page: số trang hiện tại, size: số lượng sinh viên trên mỗi trang
        return try {
            val response = apiClient.getAllStudents(page, size, sort, search)
            if (response.isSuccessful) {
                val students = response.body() ?: emptyList()
                // Lấy thông tin phân trang từ header
                val totalElements = response.headers().get("X-Total-Count")?.toLong() ?: 0L
                val totalPages = (totalElements / size + if (totalElements % size > 0) 1 else 0).toInt()
                val pageResponse = PageResponse(
                    content = students,
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

    suspend fun getStudentById(id: Long): Result<Student> {
        return try {
            val response = apiClient.getStudentById(id)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getStudentByUserId(id: Long): Result<Student> {
        return try {
            val response = apiClient.getStudentByUserId(id)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}