package com.example.mamikosmobile.ui.home

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mamikosmobile.data.model.KosanResponse
import com.example.mamikosmobile.data.network.RetrofitClient
import kotlinx.coroutines.launch

class KosanViewModel : ViewModel() {
    var kosList = mutableStateOf<List<KosanResponse>>(emptyList())
    var isLoading = mutableStateOf(false)
    var errorMessage = mutableStateOf<String?>(null)

    fun fetchAllKosan(context: Context) {
        isLoading.value = true
        errorMessage.value = null

        viewModelScope.launch {
            try {
                val apiService = RetrofitClient.getClient(context)
                val response = apiService.getAllKosan()

                if (response.isSuccessful && response.body() != null) {
                    kosList.value = response.body()!!
                    Log.d("KOSAN", "Berhasil memuat ${kosList.value.size} kosan")
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("KOSAN", "Gagal memuat kosan: $errorBody")
                    errorMessage.value = "Gagal memuat data kosan"
                }
            } catch (e: Exception) {
                Log.e("KOSAN", "Error: ${e.message}", e)
                errorMessage.value = "Koneksi gagal: ${e.localizedMessage}"
            } finally {
                isLoading.value = false
            }
        }
    }

    fun refreshKosan(context: Context) {
        fetchAllKosan(context)
    }
}