package com.example.remedialucp2_235.data.dao

import androidx.room.*
import com.example.remedialucp2_235.data.entity.AuditLog
import com.example.remedialucp2_235.data.entity.Book
import com.example.remedialucp2_235.data.entity.Category
import kotlinx.coroutines.flow.Flow

@Dao
interface LibraryDao {
    // 1. Insert Dasar
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCategory(category: Category)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertBook(book: Book)

    @Insert
    suspend fun insertAudit(log: AuditLog)

    // 2. Query REKURSIF (Soal: "Pencarian kategori induk menampilkan seluruh buku sub-kategori")
    // Mengambil semua ID kategori anak cucu dari parentId
    @Query("""
        WITH RECURSIVE CategoryHierarchy(id) AS (
            SELECT id FROM categories WHERE id = :categoryId AND isDeleted = 0
            UNION ALL
            SELECT c.id FROM categories c
            INNER JOIN CategoryHierarchy ch ON c.parentId = ch.id
            WHERE c.isDeleted = 0
        )
        SELECT * FROM books WHERE categoryId IN (SELECT id FROM CategoryHierarchy) AND isDeleted = 0
    """)
    fun getBooksByCategoryRecursive(categoryId: Int): Flow<List<Book>>

    @Query("SELECT * FROM categories WHERE isDeleted = 0")
    fun getAllCategories(): Flow<List<Category>>

    // 3. LOGIKA HAPUS (Soft Delete & Transaction)
    // Cek apakah ada buku dipinjam di kategori ini sebelum hapus
    @Query("SELECT COUNT(*) FROM books WHERE categoryId = :categoryId AND status = 'Dipinjam' AND isDeleted = 0")
    suspend fun countBorrowedBooks(categoryId: Int): Int

    @Query("UPDATE categories SET isDeleted = 1 WHERE id = :categoryId")
    suspend fun softDeleteCategory(categoryId: Int)

    // Transaction Block
    @Transaction
    suspend fun deleteCategorySafe(categoryId: Int) {
        val borrowedCount = countBorrowedBooks(categoryId)
        if (borrowedCount > 0) {
            // Ini akan memicu Rollback otomatis karena Exception
            throw IllegalStateException("Gagal: Ada buku yang sedang dipinjam di kategori ini!")
        } else {
            // Lakukan Soft Delete
            softDeleteCategory(categoryId)
            insertAudit(AuditLog(action = "SOFT_DELETE", details = "Kategori $categoryId dihapus"))
        }
    }
}