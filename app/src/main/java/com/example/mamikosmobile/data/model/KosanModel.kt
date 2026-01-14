package com.example.mamikosmobile.data.model

data class KosanRequest(
    val nama: String,
    val alamat: String,
    val hargaPerBulan: Int,
    val deskripsi: String?,
    val tersedia: Boolean
)

data class KosanResponse(
    val id: Long,
    val nama: String,
    val alamat: String,
    val hargaPerBulan: Int,
    val deskripsi: String?,
    val tersedia: Boolean,
    val pemilik: UserDto
)