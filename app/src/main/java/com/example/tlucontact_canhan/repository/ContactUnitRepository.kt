package com.example.tlucontact_canhan.repository

import android.content.Context
import com.example.tlucontact_canhan.model.PageResponse
import com.example.tlucontact_canhan.model.ContactUnit
import com.example.tlucontact_canhan.model.UnitDetailDTO
import com.example.tlucontact_canhan.network.ApiClient
import com.example.tlucontact_canhan.network.RetrofitClient

class ContactUnitRepository(private val context: Context) {
    private val apiClient: ApiClient = RetrofitClient.getApiClient(context)

    suspend fun getAllContactUnits(page: Int, size: Int, sort: String? = null, search: String? = null): Result<PageResponse<UnitDetailDTO>> {   // page: số trang hiện tại, size: số lượng sinh viên trên mỗi trang
        return try {
            val response = apiClient.getAllContactUnits(page, size, sort, search)
            if (response.isSuccessful) {
                val contactUnits = response.body() ?: emptyList()
                // Lấy thông tin phân trang từ header
                val totalElements = response.headers().get("X-Total-Count")?.toLong() ?: 0L
                val totalPages = (totalElements / size + if (totalElements % size > 0) 1 else 0).toInt()
                val pageResponse = PageResponse(
                    content = contactUnits,
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

    suspend fun getUnitById(id: Long): Result<UnitDetailDTO> {
        return try {
            val response = apiClient.getUnitById(id)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}