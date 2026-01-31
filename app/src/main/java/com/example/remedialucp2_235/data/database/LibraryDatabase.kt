package com.example.remedialucp2_235.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.remedialucp2_235.data.dao.LibraryDao
import com.example.remedialucp2_235.data.entity.AuditLog
import com.example.remedialucp2_235.data.entity.Book
import com.example.remedialucp2_235.data.entity.Category

@Database(entities = [Category::class, Book::class, AuditLog::class], version = 1, exportSchema = false)
abstract class LibraryDatabase : RoomDatabase() {
    abstract fun libraryDao(): LibraryDao

    companion object {
        @Volatile
        private var Instance: LibraryDatabase? = null

        fun getDatabase(context: Context): LibraryDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, LibraryDatabase::class.java, "library_db_235")
                    .build()
                    .also { Instance = it }
            }
        }
    }
}