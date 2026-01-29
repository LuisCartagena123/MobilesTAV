package com.example.evaluacion2.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

object BackendClient {
    // Cambiar según el ambiente:
    // Local: "http://10.0.2.2:8080/api/"
    // EC2: "http://TU_IP_O_DOMINIO_EC2:8080/api/"
    // Otro PC local: "http://IP_LOCAL:8080/api/"
    private const val BASE_URL = "http://10.0.2.2:8080/api/"
    
    val api: BackendApi by lazy {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BackendApi::class.java)
    }
}
