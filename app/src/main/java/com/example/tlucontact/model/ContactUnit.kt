package com.example.tlucontact.model

data class ContactUnit(
    val id: Long,
    val code: String,
    val name: String,
    val address: String,
    val logoURL: String,
    val phone: String,
    val email: String,
    val fax: String,
    val Type: Type,
    val parentUnit: ContactUnit?,
)

enum class Type {
    KHOA,
    PHONG_BAN,
    TRUNG_TAM,
    VIEN
}