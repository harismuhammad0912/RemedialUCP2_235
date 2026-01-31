package com.example.remedialucp2_235.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.remedialucp2_235.data.entity.Book
import com.example.remedialucp2_235.data.entity.Category
import com.example.remedialucp2_235.data.repository.LibraryRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LibraryViewModel(private val repository: LibraryRepository) : ViewModel() {

    // --- STATE UNTUK INPUT FORM ---
    var namaBuku by mutableStateOf("")
    var namaKategori by mutableStateOf("")
    var selectedKategoriId by mutableStateOf<Int?>(null)

    // --- DATA STREAM (Flow) ---
    // Mengambil semua kategori untuk Dropdown
    val listKategori: StateFlow<List<Category>> = repository.getAllCategories()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // --- FUNGSI AKSI ---
    fun simpanKategori() {
        if (namaKategori.isNotBlank()) {
            viewModelScope.launch {
                repository.insertCategory(Category(name = namaKategori))
                namaKategori = "" // Reset form
            }
        }
    }

    fun simpanBuku() {
        if (namaBuku.isNotBlank() && selectedKategoriId != null) {
            viewModelScope.launch {
                repository.insertBook(
                    Book(title = namaBuku, categoryId = selectedKategoriId!!)
                )
                namaBuku = "" // Reset form
            }
        }
    }
}