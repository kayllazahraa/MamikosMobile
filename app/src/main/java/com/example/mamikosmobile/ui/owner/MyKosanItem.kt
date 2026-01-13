package com.example.mamikosmobile.ui.owner

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mamikosmobile.data.model.KosanResponse

@Composable
fun MyKosanItem(
    kosan: KosanResponse,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(kosan.nama, style = MaterialTheme.typography.titleMedium)
            Text("Rp ${kosan.hargaPerBulan} / bulan")

            Spacer(modifier = Modifier.height(8.dp))

            Row {
                Button(onClick = onEdit) {
                    Text("Edit")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = onDelete,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Hapus")
                }
            }
        }
    }
}
