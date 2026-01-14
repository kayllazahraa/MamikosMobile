package com.example.mamikosmobile.ui.owner

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.input.KeyboardType
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (kosan == null) "Tambah Kos" else "Edit Kos"
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Kembali"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            OutlinedTextField(
                value = nama,
                onValueChange = { nama = it },
                label = { Text("Nama Kos") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = alamat,
                onValueChange = { alamat = it },
                label = { Text("Alamat") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = harga,
                onValueChange = { harga = it },
                label = { Text("Harga / bulan") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                )
            )

            OutlinedTextField(
                value = deskripsi,
                onValueChange = { deskripsi = it },
                label = { Text("Deskripsi") },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 100.dp),
                maxLines = 4
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { tersedia = !tersedia },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = tersedia,
                    onCheckedChange = { tersedia = it }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Tersedia")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    val request = KosanRequest(
                        nama,
                        alamat,
                        harga.toInt(),
                        deskripsi,
                        tersedia
                    )

                    if (kosan == null) {
                        ownerViewModel.createKosan(context, request, onBack)
                    } else {
                        ownerViewModel.updateKosan(context, kosan.id, request, onBack)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text("Simpan")
            }
        }
    }

}
