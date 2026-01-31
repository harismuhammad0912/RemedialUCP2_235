package com.example.remedialucp2_235.ui.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.remedialucp2_235.ui.viewmodel.LibraryViewModel
import com.example.remedialucp2_235.ui.viewmodel.PenyediaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookFormScreen(
    navigateBack: () -> Unit,
    viewModel: LibraryViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Tambah Data") })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Form Tambah Kategori
            Text("Tambah Kategori Baru", style = MaterialTheme.typography.titleMedium)
            OutlinedTextField(
                value = viewModel.namaKategori,
                onValueChange = { viewModel.namaKategori = it },
                label = { Text("Nama Kategori") },
                modifier = Modifier.fillMaxWidth()
            )
            Button(
                onClick = {
                    viewModel.simpanKategori()
                    navigateBack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Simpan Kategori")
            }

            Divider()

            // Form Tambah Buku
            Text("Tambah Buku Baru", style = MaterialTheme.typography.titleMedium)
            OutlinedTextField(
                value = viewModel.namaBuku,
                onValueChange = { viewModel.namaBuku = it },
                label = { Text("Judul Buku") },
                modifier = Modifier.fillMaxWidth()
            )
            // Input ID Kategori Manual (Bisa diganti Dropdown jika sempat)
            OutlinedTextField(
                value = viewModel.selectedKategoriId?.toString() ?: "",
                onValueChange = {
                    viewModel.selectedKategoriId = it.toIntOrNull()
                },
                label = { Text("ID Kategori (Angka)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            Button(
                onClick = {
                    viewModel.simpanBuku()
                    navigateBack()
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = viewModel.namaBuku.isNotBlank() && viewModel.selectedKategoriId != null
            ) {
                Text("Simpan Buku")
            }
        }
    }
}