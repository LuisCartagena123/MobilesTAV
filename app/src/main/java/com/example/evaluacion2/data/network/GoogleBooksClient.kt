package com.example.evaluacion2.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient

object GoogleBooksClient {
    private const val API_KEY = "AIzaSyB31sA2u7N7ZKn3q3S2Cbm4EAaAOeEp5xY"
    
    val api: GoogleBooksApi by lazy {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val originalRequest = chain.request()
                val url = originalRequest.url.newBuilder()
                    .addQueryParameter("key", API_KEY)
                    .build()
                chain.proceed(originalRequest.newBuilder().url(url).build())
            }
            .build()
        
        Retrofit.Builder()
            .baseUrl("https://www.googleapis.com/books/v1/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GoogleBooksApi::class.java)
    }
}
