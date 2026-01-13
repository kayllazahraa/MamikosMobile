package com.example.mamikosmobile.ui.owner

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mamikosmobile.ui.order.OrderViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OwnerOrdersScreen(
    orderViewModel: OrderViewModel,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val rentals = orderViewModel.incomingRentals.value

    LaunchedEffect(Unit) {
        orderViewModel.loadOwnerRentals(context)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pesanan Masuk") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            if (orderViewModel.isLoading.value) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (rentals.isEmpty()) {
                Text("Belum ada pesanan masuk", modifier = Modifier.align(Alignment.Center))
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(rentals) { order ->
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(text = "Kos: ${order.kosan.nama}", fontWeight = FontWeight.Bold)
                                // PERBAIKAN: Menggunakan 'user' sesuai OrderResponse
                                Text(text = "Penyewa: ${order.user.namaLengkap}")
                                Text(text = "Status: ${order.status}")

                                if (order.status == "PENDING") {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.End
                                    ) {
                                        TextButton(onClick = {
                                            orderViewModel.updateRentalStatus(context, order.id, "REJECTED")
                                        }) {
                                            Text("Tolak", color = Color.Red)
                                        }
                                        Button(onClick = {
                                            orderViewModel.updateRentalStatus(context, order.id, "APPROVED")
                                        }) {
                                            Text("Terima")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}