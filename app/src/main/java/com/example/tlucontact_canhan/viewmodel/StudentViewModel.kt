package com.example.tlucontact_canhan.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tlucontact_canhan.model.Student
import com.example.tlucontact_canhan.model.StudentListItem
import com.example.tlucontact_canhan.repository.StudentRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class StudentViewModel(private val repository: StudentRepository) : ViewModel() {
    private val _students = MutableLiveData<List<StudentListItem>>(emptyList())
    val students: LiveData<List<StudentListItem>> get() = _students

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _hasMoreData = MutableLiveData(true)
    val hasMoreData: LiveData<Boolean> get() = _hasMoreData

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    // Quản lý tiêu chí sắp xếp
    private var sortField: String? = "fullName" // Mặc định sắp xếp theo fullName
    private var sortDirection: String = "asc" // Mặc định tăng dần

    private var currentPage = 0
    private val pageSize = 20 // Kích thước trang
    private var totalPages = Int.MAX_VALUE // Sẽ cập nhật sau khi lấy dữ liệu

    // Lưu danh sách gốc (đã tải từ server)
    private val originalStudents = mutableListOf<Student>()

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
                    searchStudentsInternal(query)
                }
        }
    }

    // Tải trang đầu tiên
    fun loadFirstPage(sortField: String? = this.sortField, sortDirection: String = this.sortDirection) {
        this.sortField = sortField
        this.sortDirection = sortDirection
        currentPage = 0
        originalStudents.clear() // Xóa danh sách gốc khi tải lại
        _students.value = emptyList()
        _hasMoreData.value = true
        loadNextPage()
    }

    // Tải trang tiếp theo
    fun loadNextPage() {
        if (!_hasMoreData.value!! || _isLoading.value == true) return

        viewModelScope.launch {
            _isLoading.value = true
            val sortParam = if (sortField != null) "$sortField,$sortDirection" else null
            val result = repository.getAllStudents(currentPage, pageSize, sortParam)
            _isLoading.value = false

            result.onSuccess { pageResponse ->
                val currentList = _students.value ?: emptyList()
                // Thêm dữ liệu mới vào danh sách gốc
                originalStudents.addAll(pageResponse.content)
                // Cập nhật danh sách hiển thị dựa trên query hiện tại
                searchStudentsInternal(searchQueryFlow.value)
                currentPage++
                totalPages = pageResponse.totalPages
                _hasMoreData.value = currentPage < totalPages
            }.onFailure { exception ->
                _error.value = "Lỗi khi tải dữ liệu: ${exception.message}"
            }
        }
    }

    // Gửi query tìm kiếm vào Flow
    fun searchStudents(query: String) {
        searchQueryFlow.value = query
    }

    // Hàm xử lý tìm kiếm
    private fun searchStudentsInternal(query: String) {
        val filteredStudents = if (query.isEmpty()) {
            originalStudents
        } else {
            originalStudents.filter {
                it.fullName.contains(query, ignoreCase = true) ||
                        it.phone.contains(query, ignoreCase = true) ||
                        it.email.contains(query, ignoreCase = true)
            }
        }
        val newItems = convertToStudentListItems(filteredStudents)
        _students.value = newItems
    }

    // Hàm chuyển đổi List<Student> thành List<StudentListItem> với Header
    private fun convertToStudentListItems(students: List<Student>): List<StudentListItem> {
        val result = mutableListOf<StudentListItem>()
        val groupedStudents = students.groupBy { it.fullName.first().uppercaseChar() }

        val sortedKeys = if (sortDirection == "asc") {
            groupedStudents.keys.sorted()
        } else {
            groupedStudents.keys.sortedDescending()
        }

        sortedKeys.forEach { letter ->
            result.add(StudentListItem.Header(letter.toString()))
            groupedStudents[letter]?.forEach { student ->
                result.add(StudentListItem.Student_(student))
            }
        }

        return result
    }
}