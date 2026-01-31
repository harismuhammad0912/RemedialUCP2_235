package com.example.remedialucp2_235.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.remedialucp2_235.data.dao.LibraryDao
import com.example.remedialucp2_235.data.entity.AuditLog
import com.example.remedialucp2_235.data.entity.Author
import com.example.remedialucp2_235.data.entity.Book
import com.example.remedialucp2_235.data.entity.Category

// PERBAIKAN: Menambahkan Author::class ke dalam entities
@Database(
    entities = [Category::class, Book::class, AuditLog::class, Author::class],
    version = 2, // Naikkan versi jika error migrasi, atau uninstall app di HP
    exportSchema = false
)
abstract class LibraryDatabase : RoomDatabase() {
    abstract fun libraryDao(): LibraryDao

    companion object {
        @Volatile
        private var Instance: LibraryDatabase? = null

        fun getDatabase(context: Context): LibraryDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, LibraryDatabase::class.java, "library_db_235")
                    // Mengizinkan penghancuran data lama jika skema berubah (PENTING SAAT DEV)
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}