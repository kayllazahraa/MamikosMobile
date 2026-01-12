package com.example.mamikosmobile.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.mamikosmobile.data.model.RegisterRequest

@Composable
fun RegisterScreen(viewModel: AuthViewModel, onRegisterSuccess: () -> Unit, onBackToLogin: () -> Unit) {
    val context = LocalContext.current
    var namaLengkap by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var nomorTelefon by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("ROLE_PENCARI") } // Default role

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Daftar Akun Mamikos", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        TextField(value = namaLengkap, onValueChange = { namaLengkap = it }, label = { Text("Nama Lengkap") })
        Spacer(modifier = Modifier.height(8.dp))

        TextField(value = username, onValueChange = { username = it }, label = { Text("Username") })
        Spacer(modifier = Modifier.height(8.dp))

        TextField(value = password, onValueChange = { password = it }, label = { Text("Password") }, visualTransformation = PasswordVisualTransformation())
        Spacer(modifier = Modifier.height(8.dp))

        TextField(value = nomorTelefon, onValueChange = { nomorTelefon = it }, label = { Text("Nomor Telepon") })
        Spacer(modifier = Modifier.height(16.dp))

        // Pilihan Role Sederhana
        Row(verticalAlignment = Alignment.CenterVertically) {
            RadioButton(selected = role == "ROLE_PENCARI", onClick = { role = "ROLE_PENCARI" })
            Text("Pencari")
            Spacer(modifier = Modifier.width(16.dp))
            RadioButton(selected = role == "ROLE_PEMILIK", onClick = { role = "ROLE_PEMILIK" })
            Text("Pemilik")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (viewModel.isLoading.value) {
            CircularProgressIndicator()
        } else {
            Button(onClick = {
                viewModel.register(context, RegisterRequest(namaLengkap, username, password, nomorTelefon, role), onRegisterSuccess)
            }) {
                Text("Daftar Sekarang")
            }
            TextButton(onClick = onBackToLogin) {
                Text("Sudah punya akun? Login")
            }

            viewModel.errorMessage.value?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = it, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}