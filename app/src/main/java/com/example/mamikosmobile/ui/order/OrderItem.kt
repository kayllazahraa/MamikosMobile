package com.example.mamikosmobile.ui.order

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mamikosmobile.data.model.OrderResponse

@Composable
fun OrderItem(
    order: OrderResponse,
    onCancel: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Text(
                text = order.kosan.nama,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text("Tanggal Pesan: ${order.tanggalPesan}")
            Text("Status: ${order.status}")

            Spacer(modifier = Modifier.height(8.dp))

            if (order.status == "PENDING") {
                Button(
                    onClick = onCancel,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Batalkan Pesanan")
                }
            }
        }
    }
}
