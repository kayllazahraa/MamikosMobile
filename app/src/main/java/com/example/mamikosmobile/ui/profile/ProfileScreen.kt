package com.example.mamikosmobile.ui.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ProfileScreen(profileViewModel: ProfileViewModel = viewModel()) {

    val context = LocalContext.current
    val profile = profileViewModel.profile.value

    LaunchedEffect(Unit) {
        profileViewModel.loadProfile(context)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text("Profil Akun", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(16.dp))

        if (profileViewModel.isLoading.value) {
            CircularProgressIndicator()
            return@Column
        }

        profile?.let {
            var namaLengkap by remember { mutableStateOf(it.namaLengkap) }
            var username by remember { mutableStateOf(it.username) }
            var nomor by remember { mutableStateOf(it.nomorTelefon ?: "") }
            var password by remember { mutableStateOf("") }

            OutlinedTextField(
                value = namaLengkap,
                onValueChange = { namaLengkap = it },
                label = { Text("Nama Lengkap") }
            )

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") }
            )

            OutlinedTextField(
                value = nomor,
                onValueChange = { nomor = it },
                label = { Text("Nomor Telepon") }
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password Baru (opsional)") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    profileViewModel.updateProfile(
                        context,
                        namaLengkap,
                        username,
                        nomor,
                        password
                    )
                }
            ) {
                Text("Update Profil")
            }
        }

        profileViewModel.errorMessage.value?.let {
            Text(it, color = MaterialTheme.colorScheme.error)
        }

        profileViewModel.successMessage.value?.let {
            Text(it, color = MaterialTheme.colorScheme.primary)
        }
    }
}
