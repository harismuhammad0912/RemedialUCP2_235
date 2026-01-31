package com.example.remedialucp2_235.ui.viewmodel

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.remedialucp2_235.LibraryApp

object PenyediaViewModel {
    val Factory = viewModelFactory {
        initializer {
            LibraryViewModel(libraryApplication().container.libraryRepository)
        }
    }
}

// Extension function untuk mempermudah akses ke Application
fun CreationExtras.libraryApplication(): LibraryApp =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as LibraryApp)