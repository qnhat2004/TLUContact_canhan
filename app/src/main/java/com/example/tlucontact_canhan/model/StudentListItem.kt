package com.example.tlucontact_canhan.model

sealed class StudentListItem {     // Lớp cha cho các mục trong danh sách
    data class Header(val letter: String) : StudentListItem()  // Lớp con cho tiêu đề
    data class Student_(val Student: Student) : StudentListItem()  // Lớp con cho sinh viên
}