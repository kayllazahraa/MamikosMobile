package com.example.mamikosmobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.example.mamikosmobile.data.model.KosanResponse
import com.example.mamikosmobile.data.session.SessionManager
import com.example.mamikosmobile.ui.auth.AuthViewModel
import com.example.mamikosmobile.ui.auth.LoginScreen
import com.example.mamikosmobile.ui.auth.RegisterScreen
import com.example.mamikosmobile.ui.home.DetailKosanScreen
import com.example.mamikosmobile.ui.home.HomeScreen
import com.example.mamikosmobile.ui.home.KosanViewModel
import com.example.mamikosmobile.ui.order.MyBookingsScreen
import com.example.mamikosmobile.ui.order.OrderViewModel
import com.example.mamikosmobile.ui.owner.AddEditKosanScreen
import com.example.mamikosmobile.ui.owner.MyKosanScreen
import com.example.mamikosmobile.ui.owner.OwnerKosanViewModel
import com.example.mamikosmobile.ui.theme.MamikosMobileTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MamikosMobileTheme {
                MamikosApp()
            }
        }
    }
}

@Composable
fun MamikosApp() {
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }

    // ===============================
    // VIEW MODELS
    // ===============================
    val authViewModel = remember { AuthViewModel() }
    val kosanViewModel = remember { KosanViewModel() }
    val orderViewModel = remember { OrderViewModel() }
    val ownerViewModel = remember { OwnerKosanViewModel() }

    // ===============================
    // GLOBAL NAVIGATION STATE
    // ===============================
    var currentScreen by remember {
        mutableStateOf(
            if (sessionManager.isLoggedIn()) "home" else "login"
        )
    }

    // ===============================
    // SHARED STATES
    // ===============================
    var selectedKosan by remember { mutableStateOf<KosanResponse?>(null) }

    // khusus PEMILIK
    var ownerScreen by remember { mutableStateOf("home") }
    // "home" | "my_kosan" | "add_edit_kosan"
    var selectedOwnerKosan by remember { mutableStateOf<KosanResponse?>(null) }

    // ===============================
    // ROOT SCREEN SWITCH
    // ===============================
    when (currentScreen) {

        // ===============================
        // LOGIN
        // ===============================
        "login" -> {
            LoginScreen(
                viewModel = authViewModel,
                onLoginSuccess = {
                    currentScreen = "home"
                },
                onNavigateToRegister = {
                    currentScreen = "register"
                }
            )
        }

        // ===============================
        // REGISTER
        // ===============================
        "register" -> {
            RegisterScreen(
                viewModel = authViewModel,
                onRegisterSuccess = {
                    currentScreen = "home"
                },
                onBackToLogin = {
                    currentScreen = "login"
                }
            )
        }

        // ===============================
        // HOME & FEATURE FLOW
        // ===============================
        "home" -> {
            val role = sessionManager.getRole()

            when {

                // =====================================================
                // ROLE PEMILIK - LIST KOS SAYA
                // =====================================================
                role == "ROLE_PEMILIK" && ownerScreen == "my_kosan" -> {
                    MyKosanScreen(
                        ownerViewModel = ownerViewModel,
                        onAdd = {
                            selectedOwnerKosan = null
                            ownerScreen = "add_edit_kosan"
                        },
                        onEdit = { kosanId ->
                            selectedOwnerKosan =
                                ownerViewModel.myKosanList.value.first { it.id == kosanId }
                            ownerScreen = "add_edit_kosan"
                        },
                        onBack = {
                            ownerScreen = "home"
                        }
                    )
                }

                // =====================================================
                // ROLE PEMILIK - ADD / EDIT KOS
                // =====================================================
                role == "ROLE_PEMILIK" && ownerScreen == "add_edit_kosan" -> {
                    AddEditKosanScreen(
                        ownerViewModel = ownerViewModel,
                        kosan = selectedOwnerKosan,
                        onBack = {
                            ownerScreen = "my_kosan"
                            ownerViewModel.loadMyKosan(context)
                        }
                    )
                }

                // =====================================================
                // HOME UTAMA (LIST SEMUA KOS)
                // =====================================================
                selectedKosan == null -> {
                    HomeScreen(
                        viewModel = kosanViewModel,
                        onLogout = {
                            sessionManager.clearSession()
                            currentScreen = "login"
                        },
                        onKosanClick = { kosan ->
                            selectedKosan = kosan
                        },
                        onMyBookingsClick = {
                            currentScreen = "my_bookings"
                        },
                        onMyKosanClick = {
                            ownerScreen = "my_kosan"
                        }
                    )
                }

                // =====================================================
                // DETAIL KOS (PENCARI)
                // =====================================================
                else -> {
                    DetailKosanScreen(
                        kosan = selectedKosan!!,
                        orderViewModel = orderViewModel,
                        onBack = {
                            selectedKosan = null
                            kosanViewModel.refreshKosan(context)
                        }
                    )
                }
            }
        }

        // ===============================
        // PESANAN SAYA (PENCARI)
        // ===============================
        "my_bookings" -> {
            MyBookingsScreen(
                onBack = {
                    currentScreen = "home"
                }
            )
        }
    }
}
