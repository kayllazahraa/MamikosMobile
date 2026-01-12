package com.example.mamikosmobile.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mamikosmobile.data.model.KosanResponse
import com.example.mamikosmobile.ui.order.OrderViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailKosanScreen(kosan: KosanResponse, orderViewModel: OrderViewModel, onBack: () -> Unit) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail Kos") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues).padding(16.dp).fillMaxSize()) {
            Text(text = kosan.nama, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Text(text = kosan.alamat, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Harga", fontWeight = FontWeight.Bold)
            Text(text = "Rp ${kosan.hargaPerBulan} / bulan", color = MaterialTheme.colorScheme.primary)

            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Deskripsi", fontWeight = FontWeight.Bold)
            Text(text = kosan.deskripsi ?: "Tidak ada deskripsi")

            Spacer(modifier = Modifier.weight(1f))

            if (orderViewModel.isLoading.value) {
                CircularProgressIndicator(modifier = Modifier.fillMaxWidth())
            } else {
                Button(
                    onClick = { orderViewModel.createOrder(context, kosan.id) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = kosan.tersedia // Tombol mati jika kos penuh [cite: 696]
                ) {
                    Text(if (kosan.tersedia) "Pesan Sekarang" else "Kosan Penuh")
                }
            }

            orderViewModel.successMessage.value?.let { Text(it, color = MaterialTheme.colorScheme.primary) }
            orderViewModel.errorMessage.value?.let { Text(it, color = MaterialTheme.colorScheme.error) }
        }
    }
}