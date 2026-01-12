package com.example.mamikosmobile.data.network

import com.example.mamikosmobile.data.model.*
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    // ==========================================
    // AUTH & PROFILE (AuthController)
    // ==========================================
    @POST("api/auth/register")
    fun register(@Body request: RegisterRequest): Call<LoginResponse>

    @POST("api/auth/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @GET("api/auth/me")
    fun getMyProfile(@Header("Authorization") token: String): Call<ProfileResponseDto>

    @PUT("api/auth/me")
    fun updateMyProfile(
        @Header("Authorization") token: String,
        @Body request: UpdateProfileRequest
    ): Call<ProfileResponseDto>


    // ==========================================
    // KOSAN (KosanController)
    // ==========================================
    @GET("api/kosan")
    fun getAllKosan(): Call<List<KosanResponse>>

    @GET("api/kosan/{id}")
    fun getKosanById(@Path("id") id: Long): Call<KosanResponse>

    @POST("api/kosan")
    fun createKosan(
        @Header("Authorization") token: String,
        @Body request: KosanRequest
    ): Call<KosanResponse>

    @PUT("api/kosan/{id}")
    fun updateKosan(
        @Header("Authorization") token: String,
        @Path("id") id: Long,
        @Body request: KosanRequest
    ): Call<KosanResponse>

    @DELETE("api/kosan/{id}")
    fun deleteKosan(
        @Header("Authorization") token: String,
        @Path("id") id: Long
    ): Call<Void>

    @GET("api/kosan/my-listings")
    fun getMyListings(@Header("Authorization") token: String): Call<List<KosanResponse>>


    // ==========================================
    // ORDERS (OrderController)
    // ==========================================
    @POST("api/orders")
    fun createOrder(
        @Header("Authorization") token: String,
        @Body request: OrderRequest
    ): Call<OrderResponse>

    @GET("api/orders/my-bookings")
    fun getMyBookings(@Header("Authorization") token: String): Call<List<OrderResponse>>

    @GET("api/orders/my-rentals")
    fun getMyRentals(@Header("Authorization") token: String): Call<List<OrderResponse>>

    @PUT("api/orders/{id}/status")
    fun updateOrderStatus(
        @Header("Authorization") token: String,
        @Path("id") id: Long,
        @Body statusUpdate: StatusUpdate // Mengirim { "status": "..." }
    ): Call<OrderResponse>

    @GET("api/orders/{id}")
    fun getOrderById(
        @Header("Authorization") token: String,
        @Path("id") id: Long
    ): Call<OrderResponse>


    // ==========================================
    // ULASAN (UlasanController)
    // ==========================================
    @GET("api/kosan/{kosanId}/ulasan")
    fun getUlasanByKosan(@Path("kosanId") kosanId: Long): Call<List<UlasanResponse>>

    @POST("api/kosan/{kosanId}/ulasan")
    fun createUlasan(
        @Header("Authorization") token: String,
        @Path("kosanId") kosanId: Long,
        @Body request: UlasanRequest
    ): Call<UlasanResponse>

    @DELETE("api/ulasan/{ulasanId}")
    fun deleteUlasan(
        @Header("Authorization") token: String,
        @Path("ulasanId") ulasanId: Long
    ): Call<Void>
}