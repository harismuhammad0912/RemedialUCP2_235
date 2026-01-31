package com.example.remedialucp2_235.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.remedialucp2_235.data.entity.Author
import com.example.remedialucp2_235.data.entity.Book
import com.example.remedialucp2_235.data.entity.Category
import com.example.remedialucp2_235.data.repository.LibraryRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LibraryViewModel(private val repository: LibraryRepository) : ViewModel() {

    // --- STATE FORM ---
    var namaBuku by mutableStateOf("")
    var namaKategori by mutableStateOf("")
    var namaPenulis by mutableStateOf("")

    // State untuk Dropdown Kategori (Menyimpan Nama Kategori yang dipilih, bukan cuma ID)
    var selectedKategoriId by mutableStateOf<Int?>(null)
    var selectedKategoriNama by mutableStateOf("")

    // --- DATA STREAMS ---
    val listKategori: StateFlow<List<Category>> = repository.getAllCategories()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val listPenulis: StateFlow<List<Author>> = repository.getAllAuthors()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val listBuku: StateFlow<List<Book>> = repository.getAllBooks()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // --- FUNGSI RESET FORM (Agar bersih saat pindah layar) ---
    fun resetForm() {
        namaBuku = ""
        namaKategori = ""
        namaPenulis = ""
        selectedKategoriId = null
        selectedKategoriNama = ""
    }

    // --- FUNGSI SIMPAN ---
    fun simpanKategori() {
        if (namaKategori.isNotBlank()) {
            viewModelScope.launch {
                repository.insertCategory(Category(name = namaKategori))
                namaKategori = ""
            }
        }
    }

    fun simpanBuku() {
        if (namaBuku.isNotBlank() && selectedKategoriId != null) {
            viewModelScope.launch {
                repository.insertBook(
                    Book(title = namaBuku, categoryId = selectedKategoriId!!)
                )
                resetForm()
            }
        }
    }

    fun simpanPenulis() {
        if (namaPenulis.isNotBlank()) {
            viewModelScope.launch {
                repository.insertAuthor(Author(name = namaPenulis))
                namaPenulis = ""
            }
        }
    }

    // --- FUNGSI MANAJEMEN ---
    fun toggleStatusBuku(book: Book) {
        viewModelScope.launch {
            val isCurrentlyBorrowed = book.status == "Dipinjam"
            repository.updateBookStatus(book.id, !isCurrentlyBorrowed)
        }
    }

    private val DEFAULT_CATEGORY_ID = 1

    fun deleteCategoryWithOption(
        categoryId: Int,
        deleteBooks: Boolean,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                if (deleteBooks) {
                    repository.softDeleteBooks(categoryId)
                    repository.deleteCategory(categoryId)
                } else {
                    if (categoryId == DEFAULT_CATEGORY_ID) {
                        onError("Tidak bisa menghapus Kategori Utama (Default)!")
                        return@launch
                    }
                    repository.moveBooks(oldCategoryId = categoryId, newCategoryId = DEFAULT_CATEGORY_ID)
                    repository.deleteCategory(categoryId)
                }
                onSuccess()
            } catch (e: Exception) {
                onError(e.message ?: "Gagal memproses data.")
            }
        }
    }
}