package com.example.mamikosmobile.ui.order

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyBookingsScreen(
    orderViewModel: OrderViewModel,
    onBack: () -> Unit
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        orderViewModel.loadMyBookings(context)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pesanan Saya") },
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
                .padding(16.dp)
        ) {
            Box(modifier = Modifier.weight(1f)) {
                when {
                    orderViewModel.isLoading.value -> {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                    orderViewModel.myOrders.value.isEmpty() -> {
                        Text(
                            "Anda belum memiliki pesanan",
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    else -> {
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            items(orderViewModel.myOrders.value) { order ->
                                OrderItem(order = order)
                            }
                        }
                    }
                }
            }

            orderViewModel.errorMessage.value?.let {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                ) {
                    Text(it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(8.dp))
                }
            }

            orderViewModel.successMessage.value?.let {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                ) {
                    Text(it, color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(8.dp))
                }
            }
        }
    }
}