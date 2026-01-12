package com.example.mamikosmobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
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
            val context = LocalContext.current
            val sessionManager = remember { SessionManager(context) }

            val authViewModel = remember { AuthViewModel() }
            val kosanViewModel = remember { KosanViewModel() }

            var currentScreen by remember {
                mutableStateOf(if (sessionManager.fetchAuthToken() != null) "home" else "login")
            }

            var selectedKosan by remember { mutableStateOf<KosanResponse?>(null) }

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
                        onRegisterSuccess = { currentScreen = "home" },
                        onBackToLogin = { currentScreen = "login" }
                    )
                }
                "home" -> {
                    if (selectedKosan == null) {
                        val kosanViewModel = KosanViewModel()
                        HomeScreen(
                            viewModel = kosanViewModel,
                            onLogout = { sessionManager.clearSession(); currentScreen = "login" },
                            onKosanClick = { selectedKosan = it } // Simpan kosan yang diklik
                        )
                    } else {
                        val orderViewModel = OrderViewModel()
                        DetailKosanScreen(
                            kosan = selectedKosan!!,
                            orderViewModel = orderViewModel,
                            onBack = { selectedKosan = null } // Kembali ke list
                        )
                    }
                }
            }
        }
    }
}

