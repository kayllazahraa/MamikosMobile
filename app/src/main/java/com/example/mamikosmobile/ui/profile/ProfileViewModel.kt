package com.example.mamikosmobile.ui.profile

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mamikosmobile.data.model.ProfileResponseDto
import com.example.mamikosmobile.data.model.UpdateProfileRequest
import com.example.mamikosmobile.data.network.RetrofitClient
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    var isLoading = mutableStateOf(false)
    var errorMessage = mutableStateOf<String?>(null)
    var successMessage = mutableStateOf<String?>(null)
    var profile = mutableStateOf<ProfileResponseDto?>(null)

    fun loadProfile(context: Context) {
        isLoading.value = true
        errorMessage.value = null

        viewModelScope.launch {
            try {
                val api = RetrofitClient.getClient(context)
                val response = api.getMyProfile()

                if (response.isSuccessful) {
                    profile.value = response.body()
                } else {
                    errorMessage.value = "Gagal memuat profil"
                }
            } catch (e: Exception) {
                Log.e("PROFILE", e.message ?: "")
                errorMessage.value = "Koneksi bermasalah"
            } finally {
                isLoading.value = false
            }
        }
    }

    fun updateProfile(
        context: Context,
        namaLengkap: String,
        username: String,
        nomorTelefon: String,
        password: String?
    ) {
        isLoading.value = true
        errorMessage.value = null

        viewModelScope.launch {
            try {
                val api = RetrofitClient.getClient(context)
                val request = UpdateProfileRequest(
                    namaLengkap = namaLengkap,
                    username = username,
                    nomorTelefon = nomorTelefon,
                    password = password?.takeIf { it.isNotBlank() }
                )

                val response = api.updateMyProfile(request)

                if (response.isSuccessful) {
                    profile.value = response.body()
                    successMessage.value = "Profil berhasil diperbarui"
                } else {
                    errorMessage.value = "Update profil gagal"
                }
            } catch (e: Exception) {
                Log.e("PROFILE", e.message ?: "")
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
