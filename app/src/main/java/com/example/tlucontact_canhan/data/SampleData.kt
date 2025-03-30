package com.example.tlucontact_canhan.data

import com.example.tlucontact_canhan.model.ContactUnit
import com.example.tlucontact_canhan.model.Staff

object SampleData {
    val units = listOf(
        ContactUnit(
            "Khoa Công nghệ Thông tin",
            "024 3852 1234",
            "Nhà A1, ĐH Thủy Lợi",
            "test@mail.com",
            "CNTT",
            "024 3852 5678"
        ),
        ContactUnit(
            "Khoa Kỹ thuật Tài nguyên Nước",
            "024 3852 5678",
            "Nhà A2, ĐH Thủy Lợi",
            "test@mail.com",
            "CNTT",
            "024 3852 5678"
        ),
        ContactUnit(
            "Phòng Đào tạo",
            "024 3852 9999",
            "Nhà H1, ĐH Thủy Lợi",
            "test@mail.com",
            "CNTT",
            "024 3852 5678"
        ),
        ContactUnit(
            name = "Khoa Công nghệ Thông tin",
            phone = "0977889900",
            email = "cntt@university.edu.vn",
            address = "Nhà A1, ĐH Thủy Lợi",
            code = "CNTT",
            fax = "024 3852 5678"
        ),
        ContactUnit(
            name = "Khoa Cơ khí",
            phone = "0944556677",
            email = "cokhi@university.edu.vn",
            address = "Nhà A1, ĐH Thủy Lợi",
            code = "CNTT",
            fax = "024 3852 5678"
        ),
        ContactUnit(
            name = "Khoa Điện - Điện tử",
            phone = "0966123456",
            email = "dientu@university.edu.vn",
            address = "Nhà A1, ĐH Thủy Lợi",
            code = "CNTT",
            fax = "024 3852 5678"
        ),
        ContactUnit(
            name = "Khoa Kinh tế",
            phone = "0922334455",
            email = "kinhte@university.edu.vn",
            address = "Nhà A1, ĐH Thủy Lợi",
            code = "CNTT",
            fax = "024 3852 5678"
        ),
        ContactUnit(
            name = "Phòng Công tác Sinh viên",
            phone = "0922334455",
            email = "phongcongtacsinhvien@university.edu.vn",
            address = "Nhà A1, ĐH Thủy Lợi",
            code = "CNTT",
            fax = "024 3852 5678"
        )
    )

    val staffs = listOf(
        Staff("Nguyễn Văn A", "Giảng viên", "0912 345 678", "nva@tlu.edu.vn", "Khoa CNTT"),
        Staff("Trần Thị B", "Trưởng phòng", "0987 654 321", "ttb@tlu.edu.vn", "Phòng Đào tạo"),
        Staff("Lê Văn C", "Phó khoa", "0909 123 456", "lvc@tlu.edu.vn", "Khoa KTTNN")
    )
}