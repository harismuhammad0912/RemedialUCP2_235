package com.example.remedialucp2_235.data.container

import android.content.Context
import com.example.remedialucp2_235.data.database.LibraryDatabase
import com.example.remedialucp2_235.data.repository.LibraryRepository
import com.example.remedialucp2_235.data.repository.OfflineLibraryRepository

interface AppContainer {
    val libraryRepository: LibraryRepository
}

class DefaultAppContainer(private val context: Context) : AppContainer {
    // Inisialisasi Database hanya sekali (Singleton)
    override val libraryRepository: LibraryRepository by lazy {
        OfflineLibraryRepository(LibraryDatabase.getDatabase(context).libraryDao())
    }
}