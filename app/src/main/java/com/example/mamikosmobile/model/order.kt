package com.example.mamikosmobile.model

class Order  // Constructor kosong
{
    // Getter dan Setter
    var id: Long? = null
    var kosId: Long? = null
    var kosName: String? = null
    var userId: Long? = null
    var userName: String? = null
    var userPhone: String? = null
    var checkInDate: String? = null
    var checkOutDate: String? = null
    var duration: Int = 0 // dalam bulan
    var totalPrice: Double = 0.0
    var status: String? = null // PENDING, APPROVED, REJECTED
    var createdAt: String? = null
}