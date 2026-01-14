package com.example.mamikosmobile.ui.order

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mamikosmobile.data.model.OrderRequest
import com.example.mamikosmobile.data.network.RetrofitClient
import kotlinx.coroutines.launch
import com.example.mamikosmobile.data.model.OrderResponse
import com.example.mamikosmobile.data.model.StatusUpdate


class OrderViewModel : ViewModel() {
    var myOrders = mutableStateOf<List<OrderResponse>>(emptyList())
    var isLoading = mutableStateOf(false)
    var successMessage = mutableStateOf<String?>(null)
    var errorMessage = mutableStateOf<String?>(null)
    var incomingRentals = mutableStateOf<List<OrderResponse>>(emptyList())

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

    fun loadMyBookings(context: Context) {
        isLoading.value = true
        errorMessage.value = null

        viewModelScope.launch {
            try {
                val apiService = RetrofitClient.getClient(context)
                val response = apiService.getMyBookings()

                if (response.isSuccessful && response.body() != null) {
                    val newOrders = response.body()!!
                    val latestUpdate = newOrders.firstOrNull { it.status == "APPROVED" || it.status == "REJECTED" }
                    if (latestUpdate != null) {
                        successMessage.value = "Pembaruan: Pesanan Anda di ${latestUpdate.kosan.nama} telah ${latestUpdate.status}!"
                    }

                    myOrders.value = newOrders
                } else {
                    errorMessage.value = "Gagal memuat pesanan"
                }
            } catch (e: Exception) {
                Log.e("ORDER", "Error load bookings: ${e.message}", e)
                errorMessage.value = "Koneksi gagal"
            } finally {
                isLoading.value = false
            }
        }
    }

    fun loadOwnerRentals(context: Context) {
        isLoading.value = true
        errorMessage.value = null

        viewModelScope.launch {
            try {
                val apiService = RetrofitClient.getClient(context)
                val response = apiService.getMyRentals()

                if (response.isSuccessful && response.body() != null) {
                    incomingRentals.value = response.body()!!
                } else {
                    errorMessage.value = "Gagal memuat pesanan masuk"
                }
            } catch (e: Exception) {
                errorMessage.value = "Koneksi gagal"
            } finally {
                isLoading.value = false
            }
        }
    }

    fun updateRentalStatus(context: Context, orderId: Long, newStatus: String) {
        isLoading.value = true
        errorMessage.value = null

        viewModelScope.launch {
            try {
                val apiService = RetrofitClient.getClient(context)
                val response = apiService.updateOrderStatus(
                    orderId,
                    StatusUpdate(status = newStatus)
                )

                if (response.isSuccessful) {
                    successMessage.value = "Status pesanan berhasil diperbarui menjadi $newStatus"
                    loadOwnerRentals(context)
                } else {
                    errorMessage.value = "Gagal memperbarui status"
                }
            } catch (e: Exception) {
                errorMessage.value = "Terjadi kesalahan"
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