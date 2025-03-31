package com.example.tlucontact.model

sealed class StaffListItem {     // Lớp cha cho các mục trong danh sách
    data class Header(val letter: String) : StaffListItem()  // Lớp con cho tiêu đề
    data class Staff_(val staff: Staff) : StaffListItem()  // Lớp con cho đơn vị
}