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
import com.example.mamikosmobile.ui.home.UlasanViewModel
import com.example.mamikosmobile.ui.order.MyBookingsScreen
import com.example.mamikosmobile.ui.order.OrderViewModel
import com.example.mamikosmobile.ui.owner.AddEditKosanScreen
import com.example.mamikosmobile.ui.owner.MyKosanScreen
import com.example.mamikosmobile.ui.owner.OwnerKosanViewModel
import com.example.mamikosmobile.ui.profile.ProfileScreen
import com.example.mamikosmobile.ui.profile.ProfileViewModel
import com.example.mamikosmobile.ui.theme.MamikosMobileTheme
import com.example.mamikosmobile.ui.order.OwnerOrdersScreen

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

    val authViewModel = remember { AuthViewModel() }
    val kosanViewModel = remember { KosanViewModel() }
    val orderViewModel = remember { OrderViewModel() }
    val ownerViewModel = remember { OwnerKosanViewModel() }
    val profileViewModel = remember { ProfileViewModel() }
    val ulasanViewModel = remember { UlasanViewModel() }

    var currentScreen by remember {
        mutableStateOf(if (sessionManager.isLoggedIn()) "home" else "login")
    }

    var selectedKosan by remember { mutableStateOf<KosanResponse?>(null) }
    var ownerScreen by remember { mutableStateOf("my_kosan") }
    var selectedOwnerKosan by remember { mutableStateOf<KosanResponse?>(null) }

    when (currentScreen) {
        "login" -> {
            LoginScreen(
                viewModel = authViewModel,
                onLoginSuccess = { currentScreen = "home" },
                onNavigateToRegister = { currentScreen = "register" }
            )
        }

        "register" -> {
            RegisterScreen(
                viewModel = authViewModel,
                onRegisterSuccess = { currentScreen = "login" },
                onBackToLogin = { currentScreen = "login" }
            )
        }

        "home" -> {
            val role = sessionManager.getRole()

            when {
                role == "ROLE_PEMILIK" && ownerScreen != "home" -> {
                    if (ownerScreen == "my_kosan") {
                        MyKosanScreen(
                            ownerViewModel = ownerViewModel,
                            onAdd = {
                                selectedOwnerKosan = null
                                ownerScreen = "add_edit_kosan"
                            },
                            onEdit = { kosanId ->
                                selectedOwnerKosan = ownerViewModel.myKosanList.value.first { it.id == kosanId }
                                ownerScreen = "add_edit_kosan"
                            },
                            onBack = { ownerScreen = "home" }
                        )
                    } else if (ownerScreen == "add_edit_kosan") {
                        AddEditKosanScreen(
                            ownerViewModel = ownerViewModel,
                            kosan = selectedOwnerKosan,
                            onBack = {
                                ownerScreen = "my_kosan"
                                ownerViewModel.loadMyKosan(context)
                            }
                        )
                    }
                }

                selectedKosan != null -> {
                    DetailKosanScreen(
                        kosan = selectedKosan!!,
                        orderViewModel = orderViewModel,
                        ulasanViewModel = ulasanViewModel,
                        onBack = {
                            selectedKosan = null
                            kosanViewModel.refreshKosan(context)
                        }
                    )
                }

                else -> {
                    HomeScreen(
                        viewModel = kosanViewModel,
                        onLogout = {
                            sessionManager.clearSession()
                            currentScreen = "login"
                        },
                        onKosanClick = { kosan -> selectedKosan = kosan },
                        onMyBookingsClick = { currentScreen = "my_bookings" },
                        onMyKosanClick = { ownerScreen = "my_kosan" },
                        onProfileClick = { currentScreen = "profile" },
                        onOwnerOrdersClick = { currentScreen = "owner_orders" }
                    )
                }
            }
        }

        "my_bookings" -> {
            MyBookingsScreen(
                orderViewModel = orderViewModel,
                onBack = { currentScreen = "home" }
            )
        }

        "profile" -> {
            ProfileScreen(
                profileViewModel = profileViewModel,
                onBack = {
                    currentScreen = "home"
                }
            )
        }

        "owner_orders" -> {
            OwnerOrdersScreen(
                orderViewModel = orderViewModel,
                onBack = {
                    currentScreen = "home"
                }
            )
        }
    }
}