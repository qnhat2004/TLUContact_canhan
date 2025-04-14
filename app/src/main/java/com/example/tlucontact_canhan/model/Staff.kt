package com.example.tlucontact_canhan.model

data class Staff(
    val id: Long,
    val staffId: String,
    val fullName: String,
    val position: String,
    val phone: String,
    val address: String,
    val education: String,
    val email: String,
    val unit: ContactUnit,
    val avatarUrl: String,
)