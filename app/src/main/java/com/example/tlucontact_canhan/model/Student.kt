package com.example.tlucontact_canhan.model

data class Student(
    val id: Long,
    val studentId: String,   // Mã sinh viên
    val fullName: String,
    val phone: String,
    val address: String,
    val email: String,
    val user: User,
    val unit: ContactUnit,
    val avatarUrl: String
)