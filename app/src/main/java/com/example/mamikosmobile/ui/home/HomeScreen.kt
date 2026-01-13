package com.example.mamikosmobile.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Refresh
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: KosanViewModel,
    onLogout: () -> Unit,
    onKosanClick: (KosanResponse) -> Unit,
    onMyBookingsClick: () -> Unit,
    onMyKosanClick: () -> Unit
) {
    val context = LocalContext.current
    val sessionManager = SessionManager(context)

    // Load data saat pertama kali dibuka
    LaunchedEffect(Unit) {
        viewModel.fetchAllKosan(context)
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
                    // Refresh Button
                    IconButton(onClick = { viewModel.refreshKosan(context) }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                    }

                    IconButton(onClick = { onMyBookingsClick() }) {
                        Icon(Icons.Default.List, contentDescription = "Pesanan Saya")
                    }

                    // Logout Button
                    IconButton(onClick = onLogout) {
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
                    // Loading State
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                viewModel.errorMessage.value != null -> {
                    // Error State
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
                    // Empty State
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
                    // Success State - Show List
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(viewModel.kosList.value) { kosan ->
                            KosanItem(
                                kosan = kosan,
                                onClick = { onKosanClick(kosan) }
                            )
                        }
                    }
                }
            }
        }
    }
}