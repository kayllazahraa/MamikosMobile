package com.example.mamikosmobile.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.mamikosmobile.data.model.KosanResponse

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: KosanViewModel,
    onLogout: () -> Unit,
    onKosanClick: (KosanResponse) -> Unit // TAMBAHKAN PARAMETER INI
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.fetchAllKosan(context)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Daftar Kos STIS") },
                actions = {
                    IconButton(onClick = onLogout) {
                        Text("Logout")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
            if (viewModel.isLoading.value) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)
            ) {
                items(viewModel.kosList.value) { kosan ->
                    // Di sini kita panggil KosanItem dan teruskan datanya ke onKosanClick
                    KosanItem(
                        kosan = kosan,
                        onClick = { onKosanClick(kosan) }
                    )
                }
            }

            viewModel.errorMessage.value?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}