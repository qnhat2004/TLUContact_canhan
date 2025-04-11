package com.example.tlucontact_canhan.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tlucontact_canhan.model.UnitDetailDTO
import com.example.tlucontact_canhan.model.UnitListItem
import com.example.tlucontact_canhan.repository.ContactUnitRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class ContactUnitViewModel(private val repository: ContactUnitRepository) : ViewModel() {
    private val _units = MutableLiveData<List<UnitListItem>>(emptyList())
    val units: LiveData<List<UnitListItem>> get() = _units

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _hasMoreData = MutableLiveData(true)
    val hasMoreData: LiveData<Boolean> get() = _hasMoreData

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    // Quản lý tiêu chí sắp xếp
    private var sortField: String? = "name" // Mặc định sắp xếp theo name
    private var sortDirection: String = "asc" // Mặc định tăng dần

    private var currentPage = 0
    private val pageSize = 20 // Kích thước trang
    private var totalPages = Int.MAX_VALUE // Sẽ cập nhật sau khi lấy dữ liệu

    // Lưu danh sách gốc (đã tải từ server)
    private val originalUnitDetailDTOs = mutableListOf<UnitDetailDTO>()

    // StateFlow để lắng nghe query tìm kiếm
    private val searchQueryFlow = MutableStateFlow("")

    init {
        // Lắng nghe query từ searchQueryFlow và áp dụng debouncing
        viewModelScope.launch {
            searchQueryFlow
                .debounce(TimeUnit.MILLISECONDS.toMillis(300)) // Trì hoãn 300ms
                .distinctUntilChanged() // Chỉ xử lý nếu query thay đổi
                .filter { it.length >= 0 } // Xử lý tất cả query (bao gồm rỗng)
                .collect { query ->
                    searchUnitDetailDTOsInternal(query)
                }
        }
    }

    // Tải trang đầu tiên
    fun loadFirstPage(sortField: String? = this.sortField, sortDirection: String = this.sortDirection) {
        this.sortField = sortField
        this.sortDirection = sortDirection
        currentPage = 0
        originalUnitDetailDTOs.clear() // Xóa danh sách gốc khi tải lại
        _units.value = emptyList()
        _hasMoreData.value = true
        loadNextPage()
    }

    // Tải trang tiếp theo
    fun loadNextPage() {
        if (!_hasMoreData.value!! || _isLoading.value == true) return

        viewModelScope.launch {
            _isLoading.value = true
            val sortParam = if (sortField != null) "$sortField,$sortDirection" else null
            val result = repository.getAllContactUnits(currentPage, pageSize, sortParam)
            _isLoading.value = false

            result.onSuccess { pageResponse ->
                val currentList = _units.value ?: emptyList()
                // Thêm dữ liệu mới vào danh sách gốc
                originalUnitDetailDTOs.addAll(pageResponse.content)
                // Cập nhật danh sách hiển thị dựa trên query hiện tại
                searchUnitDetailDTOsInternal(searchQueryFlow.value)
                currentPage++
                totalPages = pageResponse.totalPages
                _hasMoreData.value = currentPage < totalPages
            }.onFailure { exception ->
                _error.value = "Lỗi khi tải dữ liệu: ${exception.message}"
            }
        }
    }

    // Gửi query tìm kiếm vào Flow
    fun searchUnitDetailDTOs(query: String) {
        searchQueryFlow.value = query
    }

    // Hàm xử lý tìm kiếm
    private fun searchUnitDetailDTOsInternal(query: String) {
        val filteredUnitDetailDTOs = if (query.isEmpty()) {
            originalUnitDetailDTOs
        } else {
            originalUnitDetailDTOs.filter {
                it.name.contains(query, ignoreCase = true) ||
                        it.email.contains(query, ignoreCase = true)
            }
        }
        val newItems = convertToUnitDetailDTOListItems(filteredUnitDetailDTOs)
        _units.value = newItems
    }

    // Hàm chuyển đổi List<UnitDetailDTO> thành List<UnitDetailDTOListItem> với Header
    private fun convertToUnitDetailDTOListItems(units: List<UnitDetailDTO>): List<UnitListItem> {
        val result = mutableListOf<UnitListItem>()
        val groupedUnitDetailDTOs = units.groupBy { it.name.first().uppercaseChar() }

        val sortedKeys = if (sortDirection == "asc") {
            groupedUnitDetailDTOs.keys.sorted()
        } else {
            groupedUnitDetailDTOs.keys.sortedDescending()
        }

        sortedKeys.forEach { letter ->
            result.add(UnitListItem.Header(letter.toString()))
            groupedUnitDetailDTOs[letter]?.forEach { student ->
                result.add(UnitListItem.Unit_(student))
            }
        }

        return result
    }

    // Lấy chi tiết đơn vị theo ID
    suspend fun getUnitById(unitId: Long): Result<UnitDetailDTO> {
        return try {
            val response = repository.getUnitById(unitId)
            return if (response.isSuccess) {
                Result.success(response.getOrThrow())
            } else {
                Result.failure(Exception("Error: ${response.exceptionOrNull()?.message}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}