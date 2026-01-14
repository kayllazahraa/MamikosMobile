package com.example.mamikosmobile.ui.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    profileViewModel: ProfileViewModel = viewModel(),
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val profile = profileViewModel.profile.value

    LaunchedEffect(Unit) {
        profileViewModel.loadProfile(context)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profil Akun") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Kembali ke Home"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            if (profileViewModel.isLoading.value) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = androidx.compose.ui.Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                profile?.let {
                    var namaLengkap by remember { mutableStateOf(it.namaLengkap) }
                    var username by remember { mutableStateOf(it.username) }
                    var nomor by remember { mutableStateOf(it.nomorTelefon ?: "") }
                    var password by remember { mutableStateOf("") }

                    OutlinedTextField(
                        value = namaLengkap,
                        onValueChange = { namaLengkap = it },
                        label = { Text("Nama Lengkap") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = username,
                        onValueChange = { username = it },
                        label = { Text("Username") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = nomor,
                        onValueChange = { nomor = it },
                        label = { Text("Nomor Telepon") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password Baru (opsional)") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            profileViewModel.updateProfile(
                                context,
                                namaLengkap,
                                username,
                                nomor,
                                password
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Update Profil")
                    }
                }

                profileViewModel.errorMessage.value?.let {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(it, color = MaterialTheme.colorScheme.error)
                }

                profileViewModel.successMessage.value?.let {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(it, color = MaterialTheme.colorScheme.primary)
                }
            }
        }
    }
}