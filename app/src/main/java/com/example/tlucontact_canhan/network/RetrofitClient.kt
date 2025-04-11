package com.example.tlucontact_canhan.network

import android.content.Context
import com.example.tlucontact_canhan.BuildConfig
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private var retrofit: Retrofit? = null

    fun getInstance(context: Context): Retrofit {
        if (retrofit == null) {
            // Add logging interceptor
            val loggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BASIC  // Change to Level.BODY for more details
            }

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(AuthInterceptor(context))
                .addInterceptor(loggingInterceptor)
                .build()

            retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())     // Add Gson converter, to convert JSON to POJO
                .addCallAdapterFactory(CoroutineCallAdapterFactory()) // Hỗ trợ suspend functions
                .build()
        }
        return retrofit!!
    }

    // Tạo ApiClient khi cần
    fun getApiClient(context: Context): ApiClient {
        return getInstance(context).create(ApiClient::class.java)
    }
}