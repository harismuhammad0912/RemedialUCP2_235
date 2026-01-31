package com.example.remedialucp2_235.ui.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.remedialucp2_235.data.entity.Category
import com.example.remedialucp2_235.ui.viewmodel.LibraryViewModel
import com.example.remedialucp2_235.ui.viewmodel.PenyediaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navigateToEntry: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LibraryViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val listKategori by viewModel.listKategori.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Daftar Kategori Perpustakaan") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = navigateToEntry) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Tambah")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            if (listKategori.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Belum ada data kategori.")
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(listKategori) { kategori ->
                        CardKategori(kategori)
                    }
                }
            }
        }
    }
}

@Composable
fun CardKategori(kategori: Category) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = kategori.name, style = MaterialTheme.typography.titleLarge)
            Text(text = "ID: ${kategori.id}", style = MaterialTheme.typography.bodySmall)
        }
    }
}