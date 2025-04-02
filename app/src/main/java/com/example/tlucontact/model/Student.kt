package com.example.tlucontact.model

data class Student(
    val id: Long,
    val code: String,   // Mã sinh viên
    val fullName: String,
    val avatarURL: String,
    val phone: String,
    val email: String,
    val position: String,
    val unit: String,
)