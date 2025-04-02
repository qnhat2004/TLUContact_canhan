package com.example.tlucontact.model

data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String
)