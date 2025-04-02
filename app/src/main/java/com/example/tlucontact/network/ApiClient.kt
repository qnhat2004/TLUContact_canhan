package com.example.tlucontact.network

import com.example.tlucontact.model.Account
import com.example.tlucontact.model.LoginRequest
import com.example.tlucontact.model.LoginResponse
import com.example.tlucontact.model.ContactUnit
import com.example.tlucontact.model.RegisterRequest
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiClient {
    // Login
    @POST("/api/authenticate")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    // Get user info
    @GET("/api/account")
    fun getUserInfo(): Call<Account>

    // Register
    @POST("/api/register")
    fun register(@Body request: RegisterRequest): Call<Unit>

    @GET("api/units")
    fun getUnits(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("eagerload") eagerload: Boolean,
//        @Query("sort") sort: String,
    ): Call<List<ContactUnit>>

    @GET("api/units/{id}")
    fun getUnitById(
        @Path("id") id: Long,
    ): Call<ContactUnit>
}