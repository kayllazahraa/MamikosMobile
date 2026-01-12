package com.example.mamikosmobile.data.session

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREF_NAME = "mamikos_session"
        private const val KEY_TOKEN = "auth_token"
        private const val KEY_USERNAME = "username"
        private const val KEY_ROLE = "user_role"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
    }

    // Simpan data login
    fun saveAuthToken(token: String, username: String, role: String) {
        prefs.edit().apply {
            putString(KEY_TOKEN, token)
            putString(KEY_USERNAME, username)
            putString(KEY_ROLE, role)
            putBoolean(KEY_IS_LOGGED_IN, true)
            apply()
        }
    }

    // Ambil token
    fun fetchAuthToken(): String? {
        return prefs.getString(KEY_TOKEN, null)
    }

    // Ambil username
    fun getUsername(): String? {
        return prefs.getString(KEY_USERNAME, null)
    }

    // Ambil role
    fun getRole(): String? {
        return prefs.getString(KEY_ROLE, null)
    }

    // Cek apakah sudah login
    fun isLoggedIn(): Boolean {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    // Logout - hapus semua data
    fun clearSession() {
        prefs.edit().clear().apply()
    }
}