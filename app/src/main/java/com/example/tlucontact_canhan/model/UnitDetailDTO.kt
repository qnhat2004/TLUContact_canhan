package com.example.tlucontact_canhan.model

data class UnitDetailDTO(
    val id: Long,
    val unitCode: String,
    val name: String,
    val address: String,
    val logoUrl: String,
    val email: String,
    val fax: String,
    val type: String,
    val parentUnit: ContactUnit?,
    val childUnitIds: List<Long>
)