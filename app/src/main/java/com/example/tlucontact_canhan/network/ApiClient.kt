package com.example.tlucontact_canhan.network

import com.example.tlucontact_canhan.model.Account
import com.example.tlucontact_canhan.model.LoginRequest
import com.example.tlucontact_canhan.model.LoginResponse
import com.example.tlucontact_canhan.model.ContactUnit
import com.example.tlucontact_canhan.model.PageResponse
import com.example.tlucontact_canhan.model.RegisterRequest
import com.example.tlucontact_canhan.model.RegisterResponse
import com.example.tlucontact_canhan.model.Staff
import com.example.tlucontact_canhan.model.Student
import com.example.tlucontact_canhan.model.UnitDetailDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiClient {
    // ----------- Auth API -----------
    @POST("/api/authenticate")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @GET("/api/account")
    suspend fun getUserInfo(): Account

    @POST("/api/register")
    suspend fun register(@Body request: RegisterRequest): RegisterResponse

    // ----------- Unit API -----------
    @GET("api/units/{id}")
    suspend fun getUnitById(
        @Path("id") id: Long,
    ): UnitDetailDTO

    @GET("api/units")
    suspend fun getAllContactUnits(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("sort") sort: String?,
        @Query("search") search: String? = null,
    ): Response<List<UnitDetailDTO>>

    // ----------- Student API -----------
    @GET("api/students/{id}")
    suspend fun getStudentById(
        @Path("id") id: Long,
    ): Student

    @GET("api/students")
    suspend fun getAllStudents(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("sort") sort: String?,
        @Query("search") search: String? = null,
    ): Response<List<Student>>  // Response: trả về một danh sách sinh viên và mã trạng thái HTTP

    @GET("api/students/user/{id}")
    suspend fun getStudentByUserId(
        @Path("id") id: Long,
    ): Student

    @GET("api/students/studentUnit")
    suspend fun getAllStudentsByUnitId(): Response<List<Student>>  // Response: trả về một danh sách sinh viên và mã trạng thái HTTP

    // ----------- Staff API -----------
    @GET("api/staff/{id}")
    suspend fun getStaffById(
        @Path("id") id: Long,
    ): Staff

    @GET("api/staff/user/{id}")
    suspend fun getStaffByUserId(
        @Path("id") id: Long,
    ): Staff

    @GET("api/staff")
    suspend fun getAllStaffs(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("sort") sort: String?,
        @Query("search") search: String? = null,
    ): Response<List<Staff>>  // Response: trả về một danh sách giảng viên và mã trạng thái HTTP
}