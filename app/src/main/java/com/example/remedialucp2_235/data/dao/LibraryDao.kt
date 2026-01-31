package com.example.remedialucp2_235.data.dao

import androidx.room.*
import com.example.remedialucp2_235.data.entity.AuditLog
import com.example.remedialucp2_235.data.entity.Author
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

    @Query("SELECT * FROM categories WHERE isDeleted = 0")
    fun getAllCategories(): Flow<List<Category>>

    // 2. Query REKURSIF
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

    // 3. LOGIKA HAPUS (Soft Delete & Transaction)
    @Query("SELECT COUNT(*) FROM books WHERE categoryId = :categoryId AND status = 'Dipinjam' AND isDeleted = 0")
    suspend fun countBorrowedBooks(categoryId: Int): Int

    @Query("UPDATE categories SET isDeleted = 1 WHERE id = :categoryId")
    suspend fun softDeleteCategory(categoryId: Int)

    // Opsi Dinamis: Pindahkan buku
    @Query("UPDATE books SET categoryId = :newCategoryId WHERE categoryId = :oldCategoryId AND isDeleted = 0")
    suspend fun moveBooksToCategory(oldCategoryId: Int, newCategoryId: Int)

    // Opsi Dinamis: Hapus buku
    @Query("UPDATE books SET isDeleted = 1 WHERE categoryId = :categoryId")
    suspend fun softDeleteBooksByCategory(categoryId: Int)

    @Transaction
    suspend fun deleteCategorySafe(categoryId: Int) {
        val borrowedCount = countBorrowedBooks(categoryId)
        if (borrowedCount > 0) {
            throw IllegalStateException("Gagal: Ada buku yang sedang dipinjam di kategori ini!")
        } else {
            softDeleteCategory(categoryId)
            insertAudit(AuditLog(action = "SOFT_DELETE", details = "Kategori $categoryId dihapus"))
        }
    }

    // 4. FITUR BARU: MANAJEMEN PENULIS & STATUS BUKU
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAuthor(author: Author)

    @Query("SELECT * FROM authors WHERE isDeleted = 0 ORDER BY name ASC")
    fun getAllAuthors(): Flow<List<Author>>

    @Query("SELECT * FROM books WHERE isDeleted = 0 ORDER BY title ASC")
    fun getAllBooks(): Flow<List<Book>>

    @Query("UPDATE books SET status = :newStatus WHERE id = :bookId")
    suspend fun updateBookStatus(bookId: Int, newStatus: String)
}