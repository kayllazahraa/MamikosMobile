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
import com.example.mamikosmobile.ui.order.OrderViewModel
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

    // ViewModels
    val authViewModel = remember { AuthViewModel() }
    val kosanViewModel = remember { KosanViewModel() }
    val orderViewModel = remember { OrderViewModel() }

    // Navigation State
    var currentScreen by remember {
        mutableStateOf(
            if (sessionManager.isLoggedIn()) "home" else "login"
        )
    }

    var selectedKosan by remember { mutableStateOf<KosanResponse?>(null) }

    when (currentScreen) {
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

        "home" -> {
            if (selectedKosan == null) {
                HomeScreen(
                    viewModel = kosanViewModel,
                    onLogout = {
                        sessionManager.clearSession()
                        currentScreen = "login"
                    },
                    onKosanClick = { kosan ->
                        selectedKosan = kosan
                    }
                )
            } else {
                DetailKosanScreen(
                    kosan = selectedKosan!!,
                    orderViewModel = orderViewModel,
                    onBack = {
                        selectedKosan = null
                        // Refresh list setelah booking
                        kosanViewModel.refreshKosan(context)
                    }
                )
            }
        }
    }
}