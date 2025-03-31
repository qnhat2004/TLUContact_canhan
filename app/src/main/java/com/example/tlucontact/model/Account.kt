package com.example.tlucontact.model

data class Account(
    val id: Long,
    val login: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val imageUrl: String,
    val activated: Boolean,
    val langKey: String,
    val createdBy: String,
    val createdDate: String,
    val lastModifiedBy: String,
    val lastModifiedDate: String,
    val authorities: List<String>
)
