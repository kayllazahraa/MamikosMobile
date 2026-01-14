package com.example.mamikosmobile.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mamikosmobile.data.model.KosanResponse
import com.example.mamikosmobile.data.session.SessionManager
import com.example.mamikosmobile.ui.order.OrderViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailKosanScreen(
    kosan: KosanResponse,
    orderViewModel: OrderViewModel,
    ulasanViewModel: UlasanViewModel,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val sessionManager = SessionManager(context)
    val userRole = sessionManager.getRole()
    val currentUsername = sessionManager.getUsername()
    var showSuccessDialog by remember { mutableStateOf(false) }

    var showReviewDialog by remember { mutableStateOf(false) }
    var ratingInput by remember { mutableIntStateOf(5) }
    var komentarInput by remember { mutableStateOf("") }

    // State lokal untuk transisi setelah klik pesan sebelum data di-refresh
    var isWaitingApproval by remember { mutableStateOf(false) }

    // Mencari apakah sudah ada pesanan untuk kosan ini di daftar pesanan pengguna
    val existingOrder = orderViewModel.myOrders.value.find { it.kosan.id == kosan.id }

    val canReview = existingOrder?.status == "APPROVED"

    LaunchedEffect(Unit) {
        orderViewModel.clearMessages()
        ulasanViewModel.fetchUlasan(context, kosan.id)
        if (userRole == "ROLE_PENCARI") {
            orderViewModel.loadMyBookings(context)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail Kosan") },
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
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Text(
                text = kosan.nama,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = kosan.alamat,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Harga Sewa",
                        style = MaterialTheme.typography.labelMedium
                    )
                    Text(
                        text = "Rp ${String.format("%,d", kosan.hargaPerBulan)} / bulan",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = if (kosan.tersedia)
                        MaterialTheme.colorScheme.tertiaryContainer
                    else
                        MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Status",
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = if (kosan.tersedia) "Tersedia" else "Tidak Tersedia",
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Informasi Pemilik",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Nama: ${kosan.pemilik.namaLengkap}")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Deskripsi",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = kosan.deskripsi ?: "Tidak ada deskripsi",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Ulasan Pengguna",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                if (canReview) {
                    TextButton(onClick = { showReviewDialog = true }) {
                        Text("Tambah Ulasan")
                    }
                }
            }

            if (ulasanViewModel.isLoading.value) {
                CircularProgressIndicator(modifier = Modifier.padding(16.dp))
            } else if (ulasanViewModel.ulasanList.value.isEmpty()) {
                Text(
                    text = "Belum ada ulasan.",
                    modifier = Modifier.padding(vertical = 8.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                ulasanViewModel.ulasanList.value.forEach { ulasan ->
                    UlasanItem(
                        ulasan = ulasan,
                        currentUsername = currentUsername,
                        onDelete = { ulasanViewModel.deleteUlasan(context, ulasan.id, kosan.id) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (userRole == "ROLE_PENCARI") {
                if (orderViewModel.isLoading.value) {
                    Button(
                        onClick = { },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        enabled = false
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                } else {
                    // Logika penentuan teks dan status tombol berdasarkan data pesanan yang ada
                    val hasExistingRequest = existingOrder != null || isWaitingApproval
                    val buttonText = when {
                        existingOrder?.status == "APPROVED" -> "Pesanan disetujui"
                        existingOrder?.status == "PENDING" || isWaitingApproval -> "Menunggu persetujuan pemilik"
                        kosan.tersedia -> "Pesan Sekarang"
                        else -> "Kosan Tidak Tersedia"
                    }

                    Button(
                        onClick = {
                            orderViewModel.createOrder(
                                context = context,
                                kosanId = kosan.id,
                                onSuccess = {
                                    isWaitingApproval = true
                                    showSuccessDialog = true
                                }
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        enabled = kosan.tersedia && !hasExistingRequest,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (hasExistingRequest)
                                MaterialTheme.colorScheme.surfaceVariant
                            else
                                MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text(
                            text = buttonText,
                            fontSize = 16.sp,
                            color = if (hasExistingRequest)
                                MaterialTheme.colorScheme.onSurfaceVariant
                            else
                                MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            } else {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Text(
                        text = "Hanya pencari kos yang dapat memesan kosan ini",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            orderViewModel.errorMessage.value?.let { error ->
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }

    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { },
            title = { Text("Pesanan Berhasil!") },
            text = { Text("Pesanan Anda telah dibuat. Silakan tunggu konfirmasi dari pemilik kosan.") },
            confirmButton = {
                TextButton(onClick = {
                    showSuccessDialog = false
                    orderViewModel.clearMessages()
                }) {
                    Text("OK")
                }
            }
        )
    }

    if (showReviewDialog) {
        AlertDialog(
            onDismissRequest = { showReviewDialog = false },
            title = { Text("Beri Ulasan") },
            text = {
                Column {
                    Text("Rating (1-5)")
                    Slider(value = ratingInput.toFloat(), onValueChange = { ratingInput = it.toInt() }, valueRange = 1f..5f, steps = 3)
                    OutlinedTextField(value = komentarInput, onValueChange = { komentarInput = it }, label = { Text("Komentar") }, modifier = Modifier.fillMaxWidth())
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        ulasanViewModel.createUlasan(context, kosan.id, ratingInput, komentarInput) {
                            showReviewDialog = false
                            komentarInput = ""
                        }
                    },
                    enabled = komentarInput.isNotBlank()
                ) { Text("Kirim") }
            },
            dismissButton = {
                TextButton(onClick = { showReviewDialog = false }) { Text("Batal") }
            }
        )
    }
}