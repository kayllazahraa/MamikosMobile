package com.example.mamikosmobile.data.model

data class OrderRequest(val kosanId: Long)

data class OrderResponse(
    val id: Long,
    val tanggalPesan: String, // LocalDate dari Java dikirim sebagai String
    val status: String,
    val user: UserDto,
    val kosan: SimpleKosanDto
)

data class SimpleKosanDto(val id: Long, val nama: String)

// Untuk update status order { "status": "APPROVED" }
data class StatusUpdate(val status: String)