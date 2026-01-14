package com.example.mamikosmobile.ui.home

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mamikosmobile.data.model.UlasanRequest
import com.example.mamikosmobile.data.model.UlasanResponse
import com.example.mamikosmobile.data.network.RetrofitClient
import kotlinx.coroutines.launch

class UlasanViewModel : ViewModel() {
    var ulasanList = mutableStateOf<List<UlasanResponse>>(emptyList())
    var isLoading = mutableStateOf(false)
    var errorMessage = mutableStateOf<String?>(null)

    fun fetchUlasan(context: Context, kosanId: Long) {
        isLoading.value = true
        viewModelScope.launch {
            try {
                val apiService = RetrofitClient.getClient(context)
                val response = apiService.getUlasanByKosan(kosanId)
                if (response.isSuccessful) {
                    ulasanList.value = response.body() ?: emptyList()
                }
            } catch (e: Exception) {
                errorMessage.value = "Gagal memuat ulasan"
            } finally {
                isLoading.value = false
            }
        }
    }

    fun createUlasan(context: Context, kosanId: Long, rating: Int, komentar: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                val apiService = RetrofitClient.getClient(context)
                val request = UlasanRequest(rating, komentar)
                val response = apiService.createUlasan(kosanId, request)
                if (response.isSuccessful) {
                    fetchUlasan(context, kosanId) // Refresh list
                    onSuccess()
                }
            } catch (e: Exception) {
                errorMessage.value = "Gagal mengirim ulasan"
            }
        }
    }

    fun deleteUlasan(context: Context, ulasanId: Long, kosanId: Long) {
        viewModelScope.launch {
            try {
                val apiService = RetrofitClient.getClient(context)
                val response = apiService.deleteUlasan(ulasanId)
                if (response.isSuccessful) {
                    fetchUlasan(context, kosanId) // Refresh list
                }
            } catch (e: Exception) {
                errorMessage.value = "Gagal menghapus ulasan"
            }
        }
    }
}