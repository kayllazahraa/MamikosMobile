package com.example.mamikosmobile.data.network

import com.example.mamikosmobile.data.model.*
import retrofit2.Response
import retrofit2.http.*
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface ApiService {

    // ==========================================
    // AUTH & PROFILE
    // ==========================================
    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<LoginResponse>

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @GET("api/auth/me")
    suspend fun getMyProfile(): Response<ProfileResponseDto>

    @PUT("api/auth/me")
    suspend fun updateMyProfile(@Body request: UpdateProfileRequest): Response<ProfileResponseDto>

    // ==========================================
    // KOSAN
    // ==========================================
    @GET("api/kosan")
    suspend fun getAllKosan(): Response<List<KosanResponse>>

    @GET("api/kosan/{id}")
    suspend fun getKosanById(@Path("id") id: Long): Response<KosanResponse>

    @POST("api/kosan")
    suspend fun createKosan(@Body request: KosanRequest): Response<KosanResponse>

    @PUT("api/kosan/{id}")
    suspend fun updateKosan(
        @Path("id") id: Long,
        @Body request: KosanRequest
    ): Response<KosanResponse>

    @DELETE("api/kosan/{id}")
    suspend fun deleteKosan(@Path("id") id: Long): Response<Void>

    @GET("api/kosan/my-listings")
    suspend fun getMyListings(): Response<List<KosanResponse>>

    // ==========================================
    // ORDERS
    // ==========================================
    @POST("api/orders")
    suspend fun createOrder(@Body request: OrderRequest): Response<OrderResponse>

    @GET("api/orders/my-bookings")
    suspend fun getMyBookings(): Response<List<OrderResponse>>

    @GET("api/orders/my-rentals")
    suspend fun getMyRentals(): Response<List<OrderResponse>>

    @PUT("api/orders/{id}/status")
    suspend fun updateOrderStatus(
        @Path("id") id: Long,
        @Body statusUpdate: StatusUpdate
    ): Response<OrderResponse>

    @GET("api/orders/{id}")
    suspend fun getOrderById(@Path("id") id: Long): Response<OrderResponse>

    // ==========================================
    // ULASAN
    // ==========================================
    @GET("api/kosan/{kosanId}/ulasan")
    suspend fun getUlasanByKosan(@Path("kosanId") kosanId: Long): Response<List<UlasanResponse>>

    @POST("api/kosan/{kosanId}/ulasan")
    suspend fun createUlasan(
        @Path("kosanId") kosanId: Long,
        @Body request: UlasanRequest
    ): Response<UlasanResponse>

    @DELETE("api/ulasan/{ulasanId}")
    suspend fun deleteUlasan(@Path("ulasanId") ulasanId: Long): Response<Void>
}