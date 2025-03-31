package com.example.tlucontact.network

import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:9000"
    private var retrofit: Retrofit? = null

    fun getInstance(context: Context): Retrofit {
        if (retrofit == null) {
            // Add logging interceptor
            val loggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BASIC  // Change to Level.BODY for more details
            }

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(AuthInterceptor(context))   // Add AuthInterceptor
                .addInterceptor(loggingInterceptor)     // Add logging interceptor
                .build()

            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())     // Add Gson converter, to convert JSON to POJO
                .build()
        }
        return retrofit!!   // not null assertion operator, throw NullPointernException if retrofit is null
    }

    val apiClient: ApiClient? = retrofit?.create(ApiClient::class.java)
}