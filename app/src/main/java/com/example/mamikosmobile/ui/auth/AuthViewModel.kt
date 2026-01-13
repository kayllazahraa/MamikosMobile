package com.example.mamikosmobile.ui.auth

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mamikosmobile.data.model.LoginRequest
import com.example.mamikosmobile.data.model.RegisterRequest
import com.example.mamikosmobile.data.network.RetrofitClient
import com.example.mamikosmobile.data.session.SessionManager
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    var isLoading = mutableStateOf(false)
    var errorMessage = mutableStateOf<String?>(null)
    var successMessage = mutableStateOf<String?>(null)

    // ===============================
    // LOGIN
    // ===============================
    fun login(
        context: Context,
        username: String,
        password: String,
        onSuccess: () -> Unit
    ) {
        if (username.isBlank() || password.isBlank()) {
            errorMessage.value = "Username dan password tidak boleh kosong"
            return
        }

        isLoading.value = true
        errorMessage.value = null

        viewModelScope.launch {
            try {
                val apiService = RetrofitClient.getClient(context)
                val response = apiService.login(LoginRequest(username, password))

                if (response.isSuccessful && response.body() != null) {
                    val loginResponse = response.body()!!

                    // 🔥 NORMALISASI ROLE (INI KUNCI)
                    val normalizedRole = when (loginResponse.role.uppercase()) {
                        "PEMILIK", "OWNER", "ROLE_PEMILIK" -> "ROLE_PEMILIK"
                        else -> "ROLE_PENCARI"
                    }

                    Log.d(
                        "LOGIN_RESPONSE",
                        "username=${loginResponse.username}, rawRole=${loginResponse.role}, normalizedRole=$normalizedRole"
                    )

                    val sessionManager = SessionManager(context)
                    sessionManager.saveAuthToken(
                        token = loginResponse.token,
                        username = loginResponse.username,
                        role = normalizedRole
                    )

                    successMessage.value = "Login berhasil!"
                    onSuccess()
                } else {
                    Log.e("AUTH", "Login gagal: ${response.errorBody()?.string()}")
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

    // ===============================
    // REGISTER
    // ===============================
    fun register(
        context: Context,
        namaLengkap: String,
        username: String,
        password: String,
        nomorTelefon: String,
        role: String,
        onSuccess: () -> Unit
    ) {
        if (
            namaLengkap.isBlank() ||
            username.isBlank() ||
            password.isBlank() ||
            nomorTelefon.isBlank()
        ) {
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
                val response = apiService.register(
                    RegisterRequest(
                        namaLengkap = namaLengkap,
                        username = username,
                        password = password,
                        nomorTelefon = nomorTelefon,
                        role = role
                    )
                )

                if (response.isSuccessful && response.body() != null) {
                    val loginResponse = response.body()!!

                    // 🔥 NORMALISASI ROLE JUGA SAAT REGISTER
                    val normalizedRole = when (loginResponse.role.uppercase()) {
                        "PEMILIK", "OWNER", "ROLE_PEMILIK" -> "ROLE_PEMILIK"
                        else -> "ROLE_PENCARI"
                    }

                    Log.d(
                        "REGISTER_RESPONSE",
                        "username=${loginResponse.username}, rawRole=${loginResponse.role}, normalizedRole=$normalizedRole"
                    )

                    val sessionManager = SessionManager(context)
                    sessionManager.saveAuthToken(
                        token = loginResponse.token,
                        username = loginResponse.username,
                        role = normalizedRole
                    )

                    successMessage.value = "Registrasi berhasil!"
                    onSuccess()
                } else {
                    Log.e("AUTH", "Registrasi gagal: ${response.errorBody()?.string()}")
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
