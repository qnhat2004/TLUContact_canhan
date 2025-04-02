package com.example.tlucontact.data

import com.example.tlucontact.model.ContactUnit
import com.example.tlucontact.model.Staff
import com.example.tlucontact.model.Type

object SampleData {
    val units = listOf(
        ContactUnit(
            id = 1,
            code = "CNTT",
            name = "Khoa Công nghệ Thông tin",
            address = "Nhà A1, ĐH Thủy Lợi",
            logoURL = "https://example.com/logo1.png",
            phone = "024 3852 1234",
            email = "test@mail.com",
            fax = "024 3852 5678",
            Type = Type.KHOA,
            parentUnit = null
        ),
        ContactUnit(
            id = 2,
            code = "KTTNN",
            name = "Khoa Kỹ thuật Tài nguyên Nước",
            address = "Nhà A2, ĐH Thủy Lợi",
            logoURL = "https://example.com/logo2.png",
            phone = "024 3852 5678",
            email = "test@mail.com",
            fax = "024 3852 5678",
            Type = Type.KHOA,
            parentUnit = null
        ),
        ContactUnit(
            id = 3,
            code = "DT",
            name = "Phòng Đào tạo",
            address = "Nhà H1, ĐH Thủy Lợi",
            logoURL = "https://example.com/logo3.png",
            phone = "024 3852 9999",
            email = "test@mail.com",
            fax = "024 3852 5678",
            Type = Type.PHONG_BAN,
            parentUnit = null
        ),
        ContactUnit(
            id = 4,
            code = "CNTT",
            name = "Khoa Công nghệ Thông tin",
            address = "Nhà A1, ĐH Thủy Lợi",
            logoURL = "https://example.com/logo4.png",
            phone = "0977889900",
            email = "cntt@university.edu.vn",
            fax = "024 3852 5678",
            Type = Type.KHOA,
            parentUnit = null
        ),
        ContactUnit(
            id = 5,
            code = "CK",
            name = "Khoa Cơ khí",
            address = "Nhà A1, ĐH Thủy Lợi",
            logoURL = "https://example.com/logo5.png",
            phone = "0944556677",
            email = "cokhi@university.edu.vn",
            fax = "024 3852 5678",
            Type = Type.KHOA,
            parentUnit = null
        ),
        ContactUnit(
            id = 6,
            code = "DDT",
            name = "Khoa Điện - Điện tử",
            address = "Nhà A1, ĐH Thủy Lợi",
            logoURL = "https://example.com/logo6.png",
            phone = "0966123456",
            email = "dientu@university.edu.vn",
            fax = "024 3852 5678",
            Type = Type.KHOA,
            parentUnit = null
        ),
        ContactUnit(
            id = 7,
            code = "KT",
            name = "Khoa Kinh tế",
            address = "Nhà A1, ĐH Thủy Lợi",
            logoURL = "https://example.com/logo7.png",
            phone = "0922334455",
            email = "kinhte@university.edu.vn",
            fax = "024 3852 5678",
            Type = Type.KHOA,
            parentUnit = null
        ),
        ContactUnit(
            id = 8,
            code = "CTSV",
            name = "Phòng Công tác Sinh viên",
            address = "Nhà A1, ĐH Thủy Lợi",
            logoURL = "https://example.com/logo8.png",
            phone = "0922334455",
            email = "phongcongtacsinhvien@university.edu.vn",
            fax = "024 3852 5678",
            Type = Type.PHONG_BAN,
            parentUnit = null
        )
    )

    val staffs = listOf(
        Staff("Nguyễn Văn A", "Giảng viên", "0912 345 678", "nva@tlu.edu.vn", "Khoa CNTT"),
        Staff("Trần Thị B", "Trưởng phòng", "0987 654 321", "ttb@tlu.edu.vn", "Phòng Đào tạo"),
        Staff("Lê Văn C", "Phó khoa", "0909 123 456", "lvc@tlu.edu.vn", "Khoa KTTNN"),
        Staff("Phạm Thị D", "Giảng viên", "0912 111 222", "ptd@tlu.edu.vn", "Khoa CNTT"),
        Staff("Ngô Văn E", "Trưởng khoa", "0987 333 444", "nve@tlu.edu.vn", "Khoa KTTNN"),
        Staff("Hoàng Thị F", "Phó phòng", "0909 555 666", "htf@tlu.edu.vn", "Phòng Đào tạo"),
        Staff("Đỗ Văn G", "Giảng viên", "0912 777 888", "dvg@tlu.edu.vn", "Khoa CNTT"),
        Staff("Lý Thị H", "Trưởng phòng", "0987 999 000", "lth@tlu.edu.vn", "Phòng Công tác Sinh viên"),
        Staff("Trịnh Văn I", "Phó khoa", "0909 111 222", "tvi@tlu.edu.vn", "Khoa Kinh tế"),
        Staff("Nguyễn Thị J", "Giảng viên", "0912 333 444", "ntj@tlu.edu.vn", "Khoa Điện - Điện tử"),
        Staff("Phan Văn K", "Trưởng khoa", "0987 555 666", "pvk@tlu.edu.vn", "Khoa Cơ khí"),
        Staff("Vũ Thị L", "Phó phòng", "0909 777 888", "vtl@tlu.edu.vn", "Phòng Đào tạo"),
        Staff("Trần Văn M", "Giảng viên", "0912 999 000", "tvm@tlu.edu.vn", "Khoa CNTT")
    )
}