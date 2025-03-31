package com.example.tlucontact.network

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val sharedPref = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val authToken = sharedPref.getString("auth_token", null)

        val request = chain.request().newBuilder()
        if (authToken != null) {
            request.addHeader("Authorization", "Bearer $authToken")
        }
        return chain.proceed(request.build())
    }
}