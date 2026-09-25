package com.jetpack.mvvm.net


import android.util.Log
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.ResponseBody.Companion.toResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit



object RetrofitClient {

    private const val BASE_URL="http://192.168.4.1/"

    private val logger=HttpLoggingInterceptor().apply {
        level=HttpLoggingInterceptor.Level.BODY
    }

    private val jsonLogger= Interceptor { chain ->
        val response = chain.proceed(chain.request())
        val body = response.body
        val json = body?.string()
        Log.d("ESP32_JSON", json ?: "empty")
        response.newBuilder()
            .body(json?.toResponseBody(body?.contentType()))
            .build()
    }

    private val client=OkHttpClient.Builder()
        .addInterceptor(logger)
        .addInterceptor(jsonLogger)
        .connectTimeout(10,TimeUnit.SECONDS)
        .readTimeout(10,TimeUnit.SECONDS)
        .writeTimeout(10,TimeUnit.SECONDS)
        .build()

    val api:ScaleApi=Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ScaleApi::class.java)
}