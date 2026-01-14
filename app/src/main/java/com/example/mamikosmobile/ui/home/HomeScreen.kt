package com.example.mamikosmobile.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.mamikosmobile.data.model.KosanResponse
import com.example.mamikosmobile.data.session.SessionManager
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: KosanViewModel,
    onKosanClick: (KosanResponse) -> Unit,
    onMyBookingsClick: () -> Unit,
    onMyKosanClick: () -> Unit,
    onProfileClick: () -> Unit,
    onOwnerOrdersClick: () -> Unit,
    onLogout: () -> Unit
) {
    val context = LocalContext.current
    val sessionManager = SessionManager(context)
    val role = sessionManager.getRole()

    LaunchedEffect(Unit) {
        viewModel.fetchAllKosan(context)
    }

    var showLogoutDialog by remember { mutableStateOf(false) }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Konfirmasi Logout") },
            text = { Text("Apakah Anda yakin ingin keluar dari aplikasi?") },
            confirmButton = {
                Button(
                    onClick = {
                        showLogoutDialog = false
                        onLogout() // Panggil fungsi logout asli
                    }
                ) { Text("Yakin") }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Batal")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Mamikos STIS")
                        Text(
                            text = "Selamat datang, ${sessionManager.getUsername() ?: "User"}",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                },
                actions = {
                    if (role == "ROLE_PEMILIK") {
                        IconButton(onClick = onOwnerOrdersClick) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "Pesanan Masuk"
                            )
                        }

                        IconButton(onClick = onMyKosanClick) {
                            Icon(Icons.Default.Home, contentDescription = "Kelola Kos")
                        }
                    }

                    if (role == "ROLE_PENCARI") {
                        IconButton(onClick = onMyBookingsClick) {
                            Icon(Icons.Default.List, contentDescription = "Pesanan Saya")
                        }
                    }

                    IconButton(onClick = onProfileClick) {
                        Icon(Icons.Default.Person, contentDescription = "Profil")
                    }

                    IconButton(onClick = { showLogoutDialog = true }) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Logout")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                viewModel.isLoading.value -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                viewModel.errorMessage.value != null -> {
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = viewModel.errorMessage.value!!,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { viewModel.refreshKosan(context) }) {
                            Text("Coba Lagi")
                        }
                    }
                }

                viewModel.kosList.value.isEmpty() -> {
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Belum ada kosan tersedia",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { viewModel.refreshKosan(context) }) {
                            Text("Refresh")
                        }
                    }
                }

                else -> {
                    KosanScrollableList(
                        kosanList = viewModel.kosList.value,
                        onKosanClick = onKosanClick
                    )
                }
            }
        }
    }
}