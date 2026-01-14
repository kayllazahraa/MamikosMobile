package com.example.mamikosmobile.ui.order

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mamikosmobile.data.model.OrderResponse

@Composable
fun OrderItem(order: OrderResponse) {
    val statusColor = when (order.status) {
        "APPROVED" -> Color(0xFF4CAF50) // Disetujui
        "REJECTED" -> Color(0xFFF44336) // Ditolak
        else -> Color(0xFFFFC107) //Pending
    }

    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = order.kosan.nama, fontWeight = FontWeight.Bold)
            Text(text = "Tanggal: ${order.tanggalPesan}")

            Spacer(modifier = Modifier.height(8.dp))

            Surface(
                color = statusColor,
                shape = RoundedCornerShape(4.dp)
            ) {
                Text(
                    text = order.status,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    color = Color.White,
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}