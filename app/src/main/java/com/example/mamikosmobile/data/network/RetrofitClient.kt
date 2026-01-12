package com.example.mamikosmobile.data.network

import android.content.Context
import com.example.mamikosmobile.ApiConfig
import com.example.mamikosmobile.data.session.SessionManager
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private var retrofit: Retrofit? = null

    fun getClient(context: Context): ApiService {
        val sessionManager = SessionManager(context)

        // Membuat Interceptor untuk menyisipkan Token JWT otomatis
        val client = OkHttpClient.Builder().addInterceptor { chain ->
            val requestBuilder = chain.request().newBuilder()

            // Ambil token dari session, jika ada tambahkan ke header
            sessionManager.fetchAuthToken()?.let {
                requestBuilder.addHeader("Authorization", "Bearer $it")
            }

            chain.proceed(requestBuilder.build())
        }.build()

        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(ApiConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client) // Tambahkan client dengan interceptor
                .build()
        }
        return retrofit!!.create(ApiService::class.java)
    }
}