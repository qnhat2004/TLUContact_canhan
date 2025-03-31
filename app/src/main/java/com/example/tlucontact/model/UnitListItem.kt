package com.example.tlucontact.model

sealed class UnitListItem {     // Lớp cha cho các mục trong danh sách
    data class Header(val letter: String) : UnitListItem()  // Lớp con cho tiêu đề
    data class Unit(val contactUnit: ContactUnit) : UnitListItem()  // Lớp con cho đơn vị
}