package com.example.mamikosmobile.ui.owner

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mamikosmobile.data.model.KosanRequest
import com.example.mamikosmobile.data.model.KosanResponse
import com.example.mamikosmobile.data.network.RetrofitClient
import kotlinx.coroutines.launch

class OwnerKosanViewModel : ViewModel() {

    var myKosanList = mutableStateOf<List<KosanResponse>>(emptyList())
    var isLoading = mutableStateOf(false)
    var errorMessage = mutableStateOf<String?>(null)
    var successMessage = mutableStateOf<String?>(null)

    fun loadMyKosan(context: Context) {
        isLoading.value = true
        errorMessage.value = null

        viewModelScope.launch {
            try {
                val api = RetrofitClient.getClient(context)
                val response = api.getMyListings()

                if (response.isSuccessful && response.body() != null) {
                    myKosanList.value = response.body()!!
                } else {
                    errorMessage.value = "Gagal memuat kos milik Anda"
                }
            } catch (e: Exception) {
                Log.e("OWNER_KOSAN", e.message ?: "")
                errorMessage.value = "Koneksi gagal"
            } finally {
                isLoading.value = false
            }
        }
    }

    fun createKosan(context: Context, request: KosanRequest, onSuccess: () -> Unit) {
        viewModelScope.launch {
            val api = RetrofitClient.getClient(context)
            api.createKosan(request)
            onSuccess()
        }
    }

    fun updateKosan(
        context: Context,
        id: Long,
        request: KosanRequest,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            val api = RetrofitClient.getClient(context)
            api.updateKosan(id, request)
            onSuccess()
        }
    }

    fun deleteKosan(context: Context, id: Long) {
        viewModelScope.launch {
            val api = RetrofitClient.getClient(context)
            api.deleteKosan(id)
            loadMyKosan(context)
        }
    }
}
