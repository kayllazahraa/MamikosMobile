package com.example.mamikosmobile.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mamikosmobile.data.model.KosanResponse

@Composable
fun KosanScrollableList(
    kosanList: List<KosanResponse>,
    onKosanClick: (KosanResponse) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(kosanList) { kosan ->
            KosanItem(
                kosan = kosan,
                onClick = { onKosanClick(kosan) }
            )
        }
    }
}
