package com.example.tlucontact_canhan.network

import android.content.Context
import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val sharedPref = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        val authToken = sharedPref.getString("jwt_token", null)

        val requestBuilder = chain.request().newBuilder()
        if (authToken != null) {
            requestBuilder.addHeader("Authorization", "Bearer $authToken")
            Log.d("Token", "Bearer $authToken")
        }
        return chain.proceed(requestBuilder.build())
    }
}