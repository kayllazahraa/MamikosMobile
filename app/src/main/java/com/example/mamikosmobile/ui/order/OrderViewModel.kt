package com.example.mamikosmobile.ui.order

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mamikosmobile.data.model.OrderRequest
import com.example.mamikosmobile.data.network.RetrofitClient
import kotlinx.coroutines.launch

class OrderViewModel : ViewModel() {
    var isLoading = mutableStateOf(false)
    var successMessage = mutableStateOf<String?>(null)
    var errorMessage = mutableStateOf<String?>(null)

    fun createOrder(context: Context, kosanId: Long, onSuccess: () -> Unit = {}) {
        isLoading.value = true
        errorMessage.value = null
        successMessage.value = null

        viewModelScope.launch {
            try {
                val apiService = RetrofitClient.getClient(context)
                val request = OrderRequest(kosanId = kosanId)
                val response = apiService.createOrder(request)

                if (response.isSuccessful && response.body() != null) {
                    Log.d("ORDER", "Order berhasil dibuat")
                    successMessage.value = "Pesanan berhasil dibuat! Menunggu persetujuan pemilik."
                    onSuccess()
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("ORDER", "Gagal membuat order: $errorBody")

                    errorMessage.value = when (response.code()) {
                        403 -> "Anda harus login sebagai PENCARI untuk memesan kosan"
                        409 -> "Kosan sudah tidak tersedia atau Anda sudah memiliki pesanan aktif"
                        else -> "Gagal membuat pesanan"
                    }
                }
            } catch (e: Exception) {
                Log.e("ORDER", "Error: ${e.message}", e)
                errorMessage.value = "Koneksi gagal: ${e.localizedMessage}"
            } finally {
                isLoading.value = false
            }
        }
    }

    fun clearMessages() {
        errorMessage.value = null
        successMessage.value = null
    }
}