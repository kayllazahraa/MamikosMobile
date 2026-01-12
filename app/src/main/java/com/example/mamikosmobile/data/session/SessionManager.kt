package com.example.mamikosmobile.data.session

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)

    companion object {
        const val USER_TOKEN = "user_token"
        const val USER_ROLE = "user_role"
        const val USERNAME = "username"
    }

    // Simpan token & data user setelah login sukses
    fun saveAuthToken(token: String, role: String, username: String) {
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.putString(USER_ROLE, role)
        editor.putString(USERNAME, username)
        editor.apply()
    }

    // Ambil token untuk dikirim ke API
    fun fetchAuthToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }

    // Logout: Hapus semua data
    fun clearSession() {
        val editor = prefs.edit()
        editor.clear()
        editor.apply()
    }
}