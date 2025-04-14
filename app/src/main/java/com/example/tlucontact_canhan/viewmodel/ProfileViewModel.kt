package com.example.tlucontact_canhan.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tlucontact_canhan.model.AccountStaff
import com.example.tlucontact_canhan.model.AccountStudent
import com.example.tlucontact_canhan.repository.AuthRepository
import com.example.tlucontact_canhan.repository.StaffRepository
import com.example.tlucontact_canhan.repository.StudentRepository
import kotlinx.coroutines.launch

sealed class AccountProfileState {
    object Loading : AccountProfileState()
    data class StudentSuccess(val data: AccountStudent) : AccountProfileState()
    data class StaffSuccess(val data: AccountStaff) : AccountProfileState()
    data class Error(val message: String) : AccountProfileState()
}

class ProfileViewModel(
    private val authRepository: AuthRepository,
    private val studentRe: StudentRepository,
    private val staffRe: StaffRepository
) : ViewModel() {

    private val _profileState = MutableLiveData<AccountProfileState>()
    val profileState: LiveData<AccountProfileState> get() = _profileState

    fun loadUserInfo() {
        viewModelScope.launch {
            _profileState.value = AccountProfileState.Loading

            val accountResult = authRepository.getAccountInfo()
            if (accountResult.isFailure) {
                _profileState.value = AccountProfileState.Error("Lỗi khi lấy thông tin người dùng: ${accountResult.exceptionOrNull()?.message}")
                return@launch
            }

            val accountInfo = accountResult.getOrNull()!!

            when {
                accountInfo.authorities.contains("ROLE_STUDENT") -> {
                    val studentResult = studentRe.getStudentByUserId(accountInfo.id)
                    if (studentResult.isFailure) {
                        _profileState.value = AccountProfileState.Error("Lỗi khi lấy thông tin sinh viên: ${studentResult.exceptionOrNull()?.message}")
                        return@launch
                    }
                    val studentInfo = studentResult.getOrNull()!!
                    _profileState.value = AccountProfileState.StudentSuccess(AccountStudent(accountInfo, studentInfo))
                }

                accountInfo.authorities.contains("ROLE_TEACHER") -> {
                    val staffResult = staffRe.getStaffByUserId(accountInfo.id)
                    if (staffResult.isFailure) {
                        _profileState.value = AccountProfileState.Error("Lỗi khi lấy thông tin nhân viên: ${staffResult.exceptionOrNull()?.message}")
                        return@launch
                    }
                    val staffInfo = staffResult.getOrNull()!!
                    _profileState.value = AccountProfileState.StaffSuccess(AccountStaff(accountInfo, staffInfo))
                }

                else -> {
                    _profileState.value = AccountProfileState.Error("Vai trò người dùng không hợp lệ")
                }
            }
        }
    }
}
