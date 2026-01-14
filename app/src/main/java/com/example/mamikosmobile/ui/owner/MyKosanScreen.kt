package com.example.mamikosmobile.ui.owner

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyKosanScreen(
    ownerViewModel: OwnerKosanViewModel = viewModel(),
    onAdd: () -> Unit,
    onEdit: (Long) -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        ownerViewModel.loadMyKosan(context)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Kos Saya") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Kembali"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onAdd) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Tambah Kos"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        when {
            ownerViewModel.isLoading.value -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            ownerViewModel.myKosanList.value.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Anda belum memiliki kos")
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(ownerViewModel.myKosanList.value) { kos ->
                        MyKosanItem(
                            kosan = kos,
                            onEdit = { onEdit(kos.id) },
                            onDelete = {
                                ownerViewModel.deleteKosan(context, kos.id)
                            }
                        )
                    }
                }
            }
        }

        ownerViewModel.errorMessage.value?.let { error ->
            Snackbar(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(error)
            }
        }
    }
}
