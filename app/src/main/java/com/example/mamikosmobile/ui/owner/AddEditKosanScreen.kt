package com.example.mamikosmobile.ui.owner

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.mamikosmobile.data.model.KosanRequest
import com.example.mamikosmobile.data.model.KosanResponse

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditKosanScreen(
    ownerViewModel: OwnerKosanViewModel,
    kosan: KosanResponse?,
    onBack: () -> Unit
) {
    val context = LocalContext.current

    var nama by remember { mutableStateOf(kosan?.nama ?: "") }
    var alamat by remember { mutableStateOf(kosan?.alamat ?: "") }
    var harga by remember { mutableStateOf(kosan?.hargaPerBulan?.toString() ?: "") }
    var deskripsi by remember { mutableStateOf(kosan?.deskripsi ?: "") }
    var tersedia by remember { mutableStateOf(kosan?.tersedia ?: true) }
    var gambarUrl by remember { mutableStateOf(kosan?.gambarUrl ?: "") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (kosan == null) "Tambah Kos" else "Edit Kos") },
                navigationIcon = {
                    IconButton(onClick = onBack) { Text("←") }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(nama, { nama = it }, label = { Text("Nama Kos") })
            OutlinedTextField(alamat, { alamat = it }, label = { Text("Alamat") })
            OutlinedTextField(harga, { harga = it }, label = { Text("Harga / bulan") })
            OutlinedTextField(deskripsi, { deskripsi = it }, label = { Text("Deskripsi") })
            OutlinedTextField(
                value = gambarUrl,
                onValueChange = { gambarUrl = it },
                label = { Text("URL Gambar Kos") },
                modifier = Modifier.fillMaxWidth()
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(tersedia, { tersedia = it })
                Text("Tersedia")
            }

            Button(onClick = {
                val request = KosanRequest(
                    nama, alamat, harga.toInt(), deskripsi, tersedia, gambarUrl
                )

                if (kosan == null) {
                    ownerViewModel.createKosan(context, request, onBack)
                } else {
                    ownerViewModel.updateKosan(context, kosan.id, request, onBack)
                }
            }) {
                Text("Simpan")
            }
        }
    }
}
