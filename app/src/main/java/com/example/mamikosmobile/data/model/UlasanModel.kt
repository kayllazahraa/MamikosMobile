package com.example.mamikosmobile.data.model

data class UlasanRequest(val rating: Int, val komentar: String)

data class UlasanResponse(
    val id: Long,
    val rating: Int,
    val komentar: String,
    val user: UserDto
)