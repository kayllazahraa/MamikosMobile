package com.example.mamikosmobile.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.mamikosmobile.data.model.KosanResponse

@Composable
fun KosanItem(kosan: KosanResponse, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        onClick = onClick, // Tambahkan ini agar bisa diklik
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            // Tampilkan gambar jika URL tersedia
            if (!kosan.gambarUrl.isNullOrEmpty()) {
                AsyncImage(
                    model = kosan.gambarUrl,
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth().height(150.dp),
                    contentScale = ContentScale.Crop
                )
            }
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = kosan.nama, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text(text = kosan.alamat, fontSize = 14.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Rp ${kosan.hargaPerBulan} / bulan",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Badge(containerColor = if (kosan.tersedia) Color.Green else Color.Red) {
                    Text(text = if (kosan.tersedia) "Tersedia" else "Penuh", color = Color.White)
                }
            }
        }
    }
}