package com.example.remedialucp2_235.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.remedialucp2_235.ui.viewmodel.LibraryViewModel
import com.example.remedialucp2_235.ui.viewmodel.PenyediaViewModel

// Warna tema
val FormBrown = Color(0xFF5D4037)
val FormBg = Color(0xFFF5F5F5)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookFormScreen(
    navigateBack: () -> Unit,
    viewModel: LibraryViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val listKategori by viewModel.listKategori.collectAsState()
    var expanded by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = FormBg,
        topBar = {
            TopAppBar(
                title = { Text("Formulir Koleksi Baru", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = FormBrown,
                    titleContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(20.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // --- KARTU KATEGORI ---
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("1. Kategori Buku", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = FormBrown)
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = viewModel.namaKategori,
                        onValueChange = { viewModel.namaKategori = it },
                        label = { Text("Buat Kategori Baru (Jika belum ada)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = { viewModel.simpanKategori() },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8D6E63))
                    ) {
                        Text("Tambahkan Rak Kategori")
                    }
                }
            }

            // --- KARTU BUKU ---
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("2. Detail Buku", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = FormBrown)
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = viewModel.namaBuku,
                        onValueChange = { viewModel.namaBuku = it },
                        label = { Text("Judul Buku") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // DROPDOWN STYLISH
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = viewModel.selectedKategoriNama.ifEmpty { "Pilih Rak Kategori" },
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Lokasi Rak") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                            modifier = Modifier.menuAnchor().fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp)
                        )

                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier = Modifier.background(Color.White)
                        ) {
                            if (listKategori.isEmpty()) {
                                DropdownMenuItem(
                                    text = { Text("Belum ada rak tersedia") },
                                    onClick = { expanded = false }
                                )
                            } else {
                                listKategori.forEach { kategori ->
                                    DropdownMenuItem(
                                        text = { Text(kategori.name) },
                                        onClick = {
                                            viewModel.selectedKategoriId = kategori.id
                                            viewModel.selectedKategoriNama = kategori.name
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            viewModel.simpanBuku()
                            navigateBack()
                        },
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        enabled = viewModel.namaBuku.isNotBlank() && viewModel.selectedKategoriId != null,
                        colors = ButtonDefaults.buttonColors(containerColor = FormBrown)
                    ) {
                        Text("Simpan ke Katalog", fontSize = 16.sp)
                    }
                }
            }
        }
    }
}