package com.example.mamikosmobile.ui.order

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyBookingsScreen(
    orderViewModel: OrderViewModel = viewModel(),
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
                        Text("←")
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

            if (orderViewModel.isLoading.value) {
                CircularProgressIndicator()
                return@Column
            }

            if (orderViewModel.myOrders.value.isEmpty()) {
                Text("Anda belum memiliki pesanan")
                return@Column
            }

            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(orderViewModel.myOrders.value) { order ->
                    OrderItem(
                        order = order,
                        onCancel = {
                            orderViewModel.cancelOrder(context, order.id)
                        }
                    )
                }
            }

            orderViewModel.errorMessage.value?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(it, color = MaterialTheme.colorScheme.error)
            }

            orderViewModel.successMessage.value?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(it, color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}
