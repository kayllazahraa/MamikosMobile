package com.example.mamikosmobile.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.mamikosmobile.data.model.KosanResponse
import com.example.mamikosmobile.data.session.SessionManager
import com.example.mamikosmobile.ui.order.OrderViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailKosanScreen(
    kosan: KosanResponse,
    orderViewModel: OrderViewModel,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val sessionManager = SessionManager(context)
    val userRole = sessionManager.getRole()
    var showSuccessDialog by remember { mutableStateOf(false) }

    // Clear messages when screen opens
    LaunchedEffect(Unit) {
        orderViewModel.clearMessages()
    }

    // Show success dialog
    LaunchedEffect(orderViewModel.successMessage.value) {
        if (orderViewModel.successMessage.value != null) {
            showSuccessDialog = true
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail Kosan") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            if (!kosan.gambarUrl.isNullOrEmpty()) {
                AsyncImage(
                    model = kosan.gambarUrl,
                    contentDescription = "Gambar Kos",
                    modifier = Modifier.fillMaxWidth().height(250.dp),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Nama Kosan
            Text(
                text = kosan.nama,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Alamat
            Text(
                text = kosan.alamat,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Harga Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Harga Sewa",
                        style = MaterialTheme.typography.labelMedium
                    )
                    Text(
                        text = "Rp ${String.format("%,d", kosan.hargaPerBulan)} / bulan",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Status Ketersediaan
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = if (kosan.tersedia)
                        MaterialTheme.colorScheme.tertiaryContainer
                    else
                        MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Status",
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = if (kosan.tersedia) "Tersedia" else "Tidak Tersedia",
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Pemilik Info
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Informasi Pemilik",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Nama: ${kosan.pemilik.namaLengkap}")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Deskripsi
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Deskripsi",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = kosan.deskripsi ?: "Tidak ada deskripsi",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.height(16.dp))

            // Order Button (hanya untuk ROLE_PENCARI)
            if (userRole == "ROLE_PENCARI") {
                if (orderViewModel.isLoading.value) {
                    Button(
                        onClick = { },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        enabled = false
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                } else {
                    Button(
                        onClick = {
                            orderViewModel.createOrder(
                                context = context,
                                kosanId = kosan.id,
                                onSuccess = { }
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        enabled = kosan.tersedia
                    ) {
                        Text(
                            text = if (kosan.tersedia) "Pesan Sekarang" else "Kosan Tidak Tersedia",
                            fontSize = 16.sp
                        )
                    }
                }
            } else {
                // Info untuk non-pencari
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Text(
                        text = "Hanya pencari kos yang dapat memesan kosan ini",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            // Error Message
            orderViewModel.errorMessage.value?.let { error ->
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }

    // Success Dialog
    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { },
            title = { Text("Pesanan Berhasil!") },
            text = { Text("Pesanan Anda telah dibuat. Silakan tunggu konfirmasi dari pemilik kosan.") },
            confirmButton = {
                TextButton(onClick = {
                    showSuccessDialog = false
                    orderViewModel.clearMessages()
                    onBack()
                }) {
                    Text("OK")
                }
            }
        )
    }
}