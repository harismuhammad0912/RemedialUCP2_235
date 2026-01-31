package com.example.remedialucp2_235.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
// import AutoStories dihapus (ganti List)
import androidx.compose.material.icons.filled.List
// import Bookmark dihapus (ganti Star)
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.remedialucp2_235.data.entity.Book
import com.example.remedialucp2_235.data.entity.Category
import com.example.remedialucp2_235.ui.viewmodel.LibraryViewModel
import com.example.remedialucp2_235.ui.viewmodel.PenyediaViewModel
import kotlinx.coroutines.launch

// --- PALET WARNA PERPUSTAKAAN KREATIF ---
val LibraryBrown = Color(0xFF5D4037)
val LibraryCream = Color(0xFFF5F5DC)
val LibraryGreen = Color(0xFF2E7D32)
val LibraryGold = Color(0xFFFFD700)
val PaperWhite = Color(0xFFFAFAFA)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navigateToEntry: () -> Unit,
    navigateToAuthors: () -> Unit,
    navigateToBooks: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LibraryViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val listKategori by viewModel.listKategori.collectAsState()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        containerColor = PaperWhite, // Background seperti kertas
        topBar = {
            // Header Kreatif dengan Lengkungan
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(LibraryBrown, Color(0xFF3E2723))
                        )
                    ),
                contentAlignment = Alignment.CenterStart
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text(
                        text = "Selamat Datang,",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 14.sp
                    )
                    Text(
                        text = "Perpustakaan Digital",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        },
        floatingActionButton = {
            LargeFloatingActionButton(
                onClick = navigateToEntry,
                containerColor = LibraryGreen,
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, contentDescription = "Tambah", modifier = Modifier.size(32.dp))
            }
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            // --- MENU NAVIGASI KREATIF ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                CreativeMenuCard(
                    title = "Penulis",
                    icon = Icons.Default.Person,
                    colorStart = Color(0xFF8D6E63),
                    colorEnd = Color(0xFF5D4037),
                    onClick = navigateToAuthors,
                    modifier = Modifier.weight(1f)
                )
                CreativeMenuCard(
                    title = "Katalog Buku",
                    icon = Icons.Default.List, // GANTI DISINI
                    colorStart = Color(0xFF66BB6A),
                    colorEnd = Color(0xFF2E7D32),
                    onClick = navigateToBooks,
                    modifier = Modifier.weight(1f)
                )
            }

            Text(
                text = "Rak Kategori",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = LibraryBrown,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            if (listKategori.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Rak masih kosong. Tambahkan koleksi!", color = Color.Gray)
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(bottom = 80.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    items(listKategori) { kategori ->
                        LibraryShelfItem(
                            kategori = kategori,
                            onDelete = { shouldDeleteBooks ->
                                viewModel.deleteCategoryWithOption(
                                    categoryId = kategori.id,
                                    deleteBooks = shouldDeleteBooks,
                                    onSuccess = {
                                        scope.launch { snackbarHostState.showSnackbar("Rak diperbarui.") }
                                    },
                                    onError = { msg ->
                                        scope.launch { snackbarHostState.showSnackbar("Gagal: $msg") }
                                    }
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

// --- KOMPONEN KREATIF: KARTU MENU ---
@Composable
fun CreativeMenuCard(
    title: String,
    icon: ImageVector,
    colorStart: Color,
    colorEnd: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        modifier = modifier.height(100.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.linearGradient(listOf(colorStart, colorEnd))),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(32.dp))
                Spacer(modifier = Modifier.height(4.dp))
                Text(title, color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}

// --- KOMPONEN KREATIF: ITEM RAK BUKU ---
@Composable
fun LibraryShelfItem(
    kategori: Category,
    onDelete: (Boolean) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            icon = { Icon(Icons.Default.Warning, contentDescription = null, tint = LibraryBrown) },
            title = { Text("Arsipkan Rak?", color = LibraryBrown) },
            text = { Text("Kategori '${kategori.name}' akan dihapus. Bagaimana nasib buku di dalamnya?") },
            confirmButton = {
                Button(
                    onClick = { onDelete(true); showDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) { Text("Musnahkan") }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { onDelete(false); showDialog = false }
                ) { Text("Pindahkan ke Gudang") }
            },
            containerColor = LibraryCream
        )
    }

    Card(
        shape = RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Strip Warna di Kiri (Seperti Jilid Buku)
            Box(
                modifier = Modifier
                    .width(8.dp)
                    .height(80.dp)
                    .background(LibraryBrown)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = kategori.name.uppercase(),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = LibraryBrown,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "Kode Rak: #00${kategori.id}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
                IconButton(onClick = { showDialog = true }) {
                    Icon(Icons.Default.Delete, contentDescription = "Hapus", tint = Color.Gray)
                }
            }
        }
    }
}

// =================================================================
// LAYAR TAMBAHAN (Author & BookList) - TEMA KONSISTEN
// =================================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthorScreen(
    navigateBack: () -> Unit,
    viewModel: LibraryViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val listPenulis by viewModel.listPenulis.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Daftar Penulis", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = LibraryBrown,
                    titleContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).padding(16.dp)) {
            // Input Kreatif
            OutlinedTextField(
                value = viewModel.namaPenulis,
                onValueChange = { viewModel.namaPenulis = it },
                label = { Text("Nama Pena Penulis") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { viewModel.simpanPenulis() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = LibraryBrown),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Registrasi Penulis")
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text("Direktori Penulis", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = LibraryBrown)

            LazyColumn(modifier = Modifier.fillMaxHeight().padding(top = 8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(listPenulis) { author ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = LibraryCream),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Person, contentDescription = null, tint = LibraryBrown)
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(text = author.name, fontWeight = FontWeight.Medium)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookListScreen(
    navigateBack: () -> Unit,
    viewModel: LibraryViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val listBuku by viewModel.listBuku.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Katalog Buku", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = LibraryGreen,
                    titleContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).padding(16.dp)) {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(listBuku) { book ->
                    // Kartu Buku Estetik
                    Card(
                        elevation = CardDefaults.cardElevation(4.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                // Ikon Bookmark diganti Star
                                Icon(Icons.Default.Star, contentDescription = null, tint = LibraryGold)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = book.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Divider(modifier = Modifier.padding(vertical = 12.dp), color = Color.LightGray)
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                FilterChip(
                                    selected = book.status == "Dipinjam",
                                    onClick = { },
                                    label = { Text(book.status) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = Color(0xFFFFEBEE),
                                        selectedLabelColor = Color.Red
                                    )
                                )
                                Button(
                                    onClick = { viewModel.toggleStatusBuku(book) },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (book.status == "Dipinjam") LibraryGreen else Color(0xFFE53935)
                                    ),
                                    shape = RoundedCornerShape(50)
                                ) {
                                    Text(if (book.status == "Dipinjam") "Kembalikan" else "Pinjam")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}