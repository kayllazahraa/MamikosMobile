package com.example.mamikosmobile.ui.order

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.mamikosmobile.data.model.OrderRequest
import com.example.mamikosmobile.data.model.OrderResponse
import com.example.mamikosmobile.data.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderViewModel : ViewModel() {
    var isLoading = mutableStateOf(false)
    var successMessage = mutableStateOf<String?>(null)
    var errorMessage = mutableStateOf<String?>(null)

    fun createOrder(context: Context, kosanId: Long) {
        isLoading.value = true
        val apiService = RetrofitClient.getClient(context)

        apiService.createOrder("", OrderRequest(kosanId)).enqueue(object : Callback<OrderResponse> {
            override fun onResponse(call: Call<OrderResponse>, response: Response<OrderResponse>) {
                isLoading.value = false
                if (response.isSuccessful) {
                    successMessage.value = "Pesanan berhasil dibuat! Menunggu persetujuan pemilik."
                } else {
                    errorMessage.value = "Gagal: Kosan mungkin sudah penuh atau Anda bukan Pencari."
                }
            }

            override fun onFailure(call: Call<OrderResponse>, t: Throwable) {
                isLoading.value = false
                errorMessage.value = "Error: ${t.message}"
            }
        })
    }
}