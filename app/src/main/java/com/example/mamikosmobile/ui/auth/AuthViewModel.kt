package com.example.mamikosmobile.ui.auth

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mamikosmobile.data.model.LoginRequest
import com.example.mamikosmobile.data.model.LoginResponse
import com.example.mamikosmobile.data.model.RegisterRequest
import com.example.mamikosmobile.data.network.RetrofitClient
import com.example.mamikosmobile.data.session.SessionManager
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    var isLoading = mutableStateOf(false)
    var errorMessage = mutableStateOf<String?>(null)
    var successMessage = mutableStateOf<String?>(null)

    fun login(context: Context, username: String, password: String, onSuccess: () -> Unit) {
        // Validasi input
        if (username.isBlank() || password.isBlank()) {
            errorMessage.value = "Username dan password tidak boleh kosong"
            return
        }

        isLoading.value = true
        errorMessage.value = null

        viewModelScope.launch {
            try {
                val apiService = RetrofitClient.getClient(context)
                val request = LoginRequest(username, password)
                val response = apiService.login(request)

                if (response.isSuccessful && response.body() != null) {
                    val loginResponse = response.body()!!

                    // Simpan ke session
                    val sessionManager = SessionManager(context)
                    sessionManager.saveAuthToken(
                        token = loginResponse.token,
                        username = loginResponse.username,
                        role = loginResponse.role
                    )

                    Log.d("AUTH", "Login berhasil: ${loginResponse.username}")
                    successMessage.value = "Login berhasil!"
                    onSuccess()
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("AUTH", "Login gagal: $errorBody")
                    errorMessage.value = "Login gagal: Username atau password salah"
                }
            } catch (e: Exception) {
                Log.e("AUTH", "Error: ${e.message}", e)
                errorMessage.value = "Koneksi gagal: ${e.localizedMessage}"
            } finally {
                isLoading.value = false
            }
        }
    }

    fun register(
        context: Context,
        namaLengkap: String,
        username: String,
        password: String,
        nomorTelefon: String,
        role: String,
        onSuccess: () -> Unit
    ) {
        // Validasi input
        if (namaLengkap.isBlank() || username.isBlank() || password.isBlank() ||
            nomorTelefon.isBlank()) {
            errorMessage.value = "Semua field harus diisi"
            return
        }

        if (password.length < 6) {
            errorMessage.value = "Password minimal 6 karakter"
            return
        }

        isLoading.value = true
        errorMessage.value = null

        viewModelScope.launch {
            try {
                val apiService = RetrofitClient.getClient(context)
                val request = RegisterRequest(
                    namaLengkap = namaLengkap,
                    username = username,
                    password = password,
                    nomorTelefon = nomorTelefon,
                    role = role
                )
                val response = apiService.register(request)

                if (response.isSuccessful && response.body() != null) {
                    val loginResponse = response.body()!!

                    // Simpan ke session
                    val sessionManager = SessionManager(context)
                    sessionManager.saveAuthToken(
                        token = loginResponse.token,
                        username = loginResponse.username,
                        role = loginResponse.role
                    )

                    Log.d("AUTH", "Registrasi berhasil: ${loginResponse.username}")
                    successMessage.value = "Registrasi berhasil!"
                    onSuccess()
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("AUTH", "Registrasi gagal: $errorBody")
                    errorMessage.value = "Registrasi gagal: Username mungkin sudah digunakan"
                }
            } catch (e: Exception) {
                Log.e("AUTH", "Error: ${e.message}", e)
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