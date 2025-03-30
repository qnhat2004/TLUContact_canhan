package com.example.tlucontact_canhan.model

data class ContactUnit(
    val name: String,
    val phone: String,
    val address: String? = null,
    val email: String? = null,
    val code: String? = null,
    val fax: String? = null
)