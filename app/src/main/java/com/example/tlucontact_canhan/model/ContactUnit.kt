package com.example.tlucontact_canhan.model

data class ContactUnit(
    val id: Long,
    val unitCode: String,
    val name: String,
    val address: String,
    val logoURL: String,
    val email: String,
    val fax: String,
    val Type: String,
    val parentUnit: ContactUnit?,
)