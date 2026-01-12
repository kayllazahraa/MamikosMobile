package com.example.mamikosmobile.ui.home

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.mamikosmobile.data.model.KosanResponse
import com.example.mamikosmobile.data.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class KosanViewModel : ViewModel() {
    var kosList = mutableStateOf<List<KosanResponse>>(emptyList())
    var isLoading = mutableStateOf(false)
    var errorMessage = mutableStateOf<String?>(null)

    fun fetchAllKosan(context: Context) {
        isLoading.value = true
        val apiService = RetrofitClient.getClient(context)

        apiService.getAllKosan().enqueue(object : Callback<List<KosanResponse>> {
            override fun onResponse(call: Call<List<KosanResponse>>, response: Response<List<KosanResponse>>) {
                isLoading.value = false
                if (response.isSuccessful) {
                    kosList.value = response.body() ?: emptyList()
                } else {
                    errorMessage.value = "Gagal mengambil data: ${response.code()}"
                }
            }

            override fun onFailure(call: Call<List<KosanResponse>>, t: Throwable) {
                isLoading.value = false
                errorMessage.value = "Error Koneksi: ${t.message}"
            }
        })
    }
}