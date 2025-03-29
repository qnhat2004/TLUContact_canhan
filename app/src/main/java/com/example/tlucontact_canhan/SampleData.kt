package com.example.tlucontact_canhan

import com.example.tlucontact_canhan.model.ContactUnit
import com.example.tlucontact_canhan.model.Staff

object SampleData {
    val units = listOf(
        ContactUnit("Khoa Công nghệ Thông tin", "024 3852 1234", "Nhà A1, ĐH Thủy Lợi"),
        ContactUnit("Khoa Kỹ thuật Tài nguyên Nước", "024 3852 5678", "Nhà A2, ĐH Thủy Lợi"),
        ContactUnit("Phòng Đào tạo", "024 3852 9999", "Nhà H1, ĐH Thủy Lợi")
    )

    val staff = listOf(
        Staff("Nguyễn Văn A", "Giảng viên", "0912 345 678", "nva@tlu.edu.vn", "Khoa CNTT"),
        Staff("Trần Thị B", "Trưởng phòng", "0987 654 321", "ttb@tlu.edu.vn", "Phòng Đào tạo"),
        Staff("Lê Văn C", "Phó khoa", "0909 123 456", "lvc@tlu.edu.vn", "Khoa KTTNN")
    )
}