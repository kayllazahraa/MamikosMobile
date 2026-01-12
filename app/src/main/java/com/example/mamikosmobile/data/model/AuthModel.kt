package com.example.mamikosmobile.data.model

data class LoginRequest(val username: String, val password: String)

data class LoginResponse(val username: String, val role: String, val token: String)

data class RegisterRequest(
    val namaLengkap: String,
    val username: String,
    val password: String,
    val nomorTelefon: String,
    val role: String
)

data class ProfileResponseDto(
    val id: Long,
    val namaLengkap: String,
    val username: String,
    val nomorTelefon: String?,
    val role: String
)

data class UpdateProfileRequest(
    val namaLengkap: String?,
    val username: String?,
    val password: String?,
    val nomorTelefon: String?
)

data class UserDto(
    val id: Long,
    val namaLengkap: String,
    val username: String,
    val role: String,
    val nomorTelefon: String?
)