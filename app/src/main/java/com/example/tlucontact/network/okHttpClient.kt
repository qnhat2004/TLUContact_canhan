package com.example.tlucontact.network

import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

fun okHttpClient(context: Context): OkHttpClient {
    val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    return OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor(context))
        .addInterceptor(logging)
        .build()
}