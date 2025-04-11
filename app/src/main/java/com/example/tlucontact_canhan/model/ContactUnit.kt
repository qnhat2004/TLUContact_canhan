package com.example.tlucontact_canhan.model

data class ContactUnit(
    val id: Long,
    val unitCode: String,
    val name: String,
    val address: String,
    val logoURL: String,
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