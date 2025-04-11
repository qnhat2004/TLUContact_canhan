package com.example.tlucontact_canhan.model

sealed class UnitListItem {     // Lớp cha cho các mục trong danh sách
    data class Header(val letter: String) : UnitListItem()  // Lớp con cho tiêu đề
    data class Unit_(val contactUnit: UnitDetailDTO) : UnitListItem()  // Lớp con cho đơn vị
}