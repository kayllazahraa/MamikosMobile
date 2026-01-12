package com.example.mamikosmobile.ui.auth

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.mamikosmobile.data.model.LoginRequest
import com.example.mamikosmobile.data.model.LoginResponse
import com.example.mamikosmobile.data.model.RegisterRequest
import com.example.mamikosmobile.data.network.RetrofitClient
import com.example.mamikosmobile.data.session.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthViewModel : ViewModel() {
    var isLoading = mutableStateOf(false)
    var loginResponse = mutableStateOf<LoginResponse?>(null)
    var errorMessage = mutableStateOf<String?>(null)

    fun login(context: Context, request: LoginRequest, onSuccess: () -> Unit) {
        isLoading.value = true
        val apiService = RetrofitClient.getClient(context)
        val sessionManager = SessionManager(context)

        apiService.login(request).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                isLoading.value = false
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        // Simpan token ke session [cite: 554]
                        sessionManager.saveAuthToken(body.token, body.role, body.username)
                        loginResponse.value = body
                        onSuccess()
                    }
                } else {
                    val errorMsg = response.errorBody()?.string() ?: "Unknown Error"
                    Log.e("AUTH_ERROR", "Server Error: $errorMsg (Code: ${response.code()})")
                    errorMessage.value = "Server Error: ${response.code()}"
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                isLoading.value = false
                Log.e("AUTH_ERROR", "Network/Parsing Failure: ${t.message}", t)
                errorMessage.value = "Koneksi Gagal: ${t.localizedMessage}"
            }
        })
    }

    // Tambahkan fungsi ini di dalam class AuthViewModel
    fun register(context: Context, request: RegisterRequest, onSuccess: () -> Unit) {
        isLoading.value = true
        val apiService = RetrofitClient.getClient(context)

        apiService.register(request).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                isLoading.value = false
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        val sessionManager = SessionManager(context)
                        sessionManager.saveAuthToken(body.token, body.role, body.username)
                        onSuccess()
                    }
                } else {
                    errorMessage.value = "Registrasi Gagal: Username mungkin sudah ada"
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                isLoading.value = false
                errorMessage.value = "Error: ${t.message}"
            }
        })
    }
}